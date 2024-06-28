package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.user.CopanyAndCdbtDto;
import com.datamon.datamon2.dto.input.user.CopanyListAndCdbtDto;
import com.datamon.datamon2.dto.input.user.UserAndCdbtDto;
import com.datamon.datamon2.dto.input.userAuth.UserListForUserCdbtByCdbtLowCodeDto;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.*;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserAuthService {
    private JwtUtil jwtUtil;
    private UserBaseService userBaseService;
    private UserCdbtMappingService userCdbtMappingService;
    private UserPermissionInfomationService userPermissionInfomationService;
    private CompanyInfomationService companyInfomationService;
    private MemberInfomationService memberInfomationService;

    public UserAuthService(JwtUtil jwtUtil, UserBaseService userBaseService, UserCdbtMappingService userCdbtMappingService, UserPermissionInfomationService userPermissionInfomationService, CompanyInfomationService companyInfomationService, MemberInfomationService memberInfomationService) {
        this.jwtUtil = jwtUtil;
        this.userBaseService = userBaseService;
        this.userCdbtMappingService = userCdbtMappingService;
        this.userPermissionInfomationService = userPermissionInfomationService;
        this.companyInfomationService = companyInfomationService;
        this.memberInfomationService = memberInfomationService;
    }

    @Transactional
    public List<Map<String, String>> getUserCdbtLowCodeList (HttpServletRequest request) throws Exception{
        List<Map<String, String>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());
        userCdbtMappingService.getUserCdbtListByUserId(userId).forEach(dto -> {
            Map<String, String> rows = new HashMap<>();
            switch (dto.getCdbtCode()){
                case "LPGE":
                    LpgeCodeDto lpgeCodeDto = CommonCodeCache.getLpgeCodes().stream()
                            .filter(LpgeCodeDto::getUseYn)
                            .filter(lpge -> !lpge.getDelYn())
                            .filter(lpge -> lpge.getCodeFullName().equals(dto.getCdbtLowCode()))
                            .findFirst().orElse(new LpgeCodeDto());

                    rows.put("name", lpgeCodeDto.getCodeValue());
                    rows.put("code", lpgeCodeDto.getCodeFullName());

                    result.add(rows);
                    break;
                default:
                    break;
            }
        });

        return result;
    }

    @Transactional
    public Map<String, List> getUserListForUserCdbtByCdbtLowCode (UserListForUserCdbtByCdbtLowCodeDto userListForUserCdbtByCdbtLowCodeDto) {
        Map<String, List> result = new HashMap<>();
        List<Map<String, String>> rows = new ArrayList<>();
        userCdbtMappingService.getUserCdbtListByCdbtLowCode(userListForUserCdbtByCdbtLowCodeDto.getCdbtCode()).forEach(dto -> {
            Map<String, String> resultRow = new HashMap<>();
            UserBaseDto userBaseById = userBaseService.getUserBaseById(dto.getUserId());
            List<UserPermissionInfomationDto> userPermissionInfomationByUserId = userPermissionInfomationService.getUserPermissionInfomationByUserId(dto.getUserId());

            resultRow.put("ID", userBaseById.getUserId());
            resultRow.put("userIdx", String.valueOf(userBaseById.getIdx()));
            resultRow.put("전체열람", String.valueOf(userPermissionInfomationByUserId.stream()
                    .filter(code -> code.getUsatCode().equals("AUTH_USAT_0000000001"))
                    .findFirst().orElse(new UserPermissionInfomationDto()).getUseYn()));
            resultRow.put("열람", String.valueOf(userPermissionInfomationByUserId.stream()
                    .filter(code -> code.getUsatCode().equals("AUTH_USAT_0000000002"))
                    .findFirst().orElse(new UserPermissionInfomationDto()).getUseYn()));
            resultRow.put("수정", String.valueOf(userPermissionInfomationByUserId.stream()
                    .filter(code -> code.getUsatCode().equals("AUTH_USAT_0000000003"))
                    .findFirst().orElse(new UserPermissionInfomationDto()).getUseYn()));

            rows.add(resultRow);
        });

        List<String> keyList = new ArrayList<>();

        keyList.add("ID");
        keyList.add("전체열람");
        keyList.add("열람");
        keyList.add("수정");

        result.put("rows", rows);
        result.put("keyList", keyList);

        return result;
    }
    @Transactional
    public List<Map<String, String>> getUserListByCopanyAndCdbt(HttpServletRequest request, CopanyAndCdbtDto copanyAndCdbtDto) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        List<String> companyUserCode = new ArrayList<>();
        companyUserCode.add("USTY_MAST");
        companyUserCode.add("USTY_CLNT");
        companyUserCode.add("USTY_ADAC");
        companyUserCode.add("USTY_CRAC");
        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto sessionUser = userBaseService.getUserBaseById(userId);

        int companyId;
        if (companyUserCode.contains(sessionUser.getUserType())) {
            CompanyInfomationDto companyInfomationByUserId = companyInfomationService.getCompanyInfomationByUserId(userId);
            companyId = companyInfomationByUserId.getIdx();
        } else {
            MemberInfomationDto memberInfomationByUserId = memberInfomationService.getMemberInfomationByUserId(userId);
            companyId = memberInfomationByUserId.getCompanyId();
        }

        List<Integer> userIdxList = userCdbtMappingService.getUserCdbtListByCdbtLowCode(copanyAndCdbtDto.getCdbtLowCode()).stream()
                .map(dto -> {
                    return dto.getUserId();
                })
                .collect(Collectors.toList());

        List<Integer> inviteUserIdLowData = memberInfomationService.getMemberInfomationDtoListByCompanyId(companyId).stream()
                .filter(dto -> !userIdxList.contains(dto.getIdx()))
                .map(dto -> {
                    return dto.getUserId();
                })
                .collect(Collectors.toList());


        userBaseService.getUserBaseByIdxList(inviteUserIdLowData).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .collect(Collectors.toList())
                .forEach(dto -> {
                    MemberInfomationDto memberInfomationByUserId = memberInfomationService.getMemberInfomationByUserId(dto.getIdx());

                    if(memberInfomationByUserId.getIdx() != null){
                        Map<String, String> resultRows = new HashMap<>();
                        resultRows.put("Idx", String.valueOf(dto.getIdx()));
                        resultRows.put("Id", dto.getUserId());

                        resultRows.put("name", memberInfomationByUserId.getName());

                        result.add(resultRows);
                    }

                });

        return result;
    }

    @Transactional
    public String createUserCdbtMappingByCopanyAndCdbt(CopanyListAndCdbtDto copanyListAndCdbtDto) throws Exception {
        UserCdbtMappingDto userCdbtMappingDto = new UserCdbtMappingDto();
        userCdbtMappingDto.setCdbtCode("LPGE");
        userCdbtMappingDto.setCdbtLowCode(copanyListAndCdbtDto.getCdbtLowCode());
        List<Integer> idxs = copanyListAndCdbtDto.getIdxs();
        idxs.forEach(idx -> {
            userCdbtMappingDto.setUserId(idx);
            userCdbtMappingService.save(userCdbtMappingDto);
        });
        return "success";
    }

    @Transactional
    public String deleteUserCdbtMappingByCopanyAndCdbt(UserAndCdbtDto userAndCdbtDto) throws Exception {
        userCdbtMappingService.getUserCdbtListByCdbtLowCode(userAndCdbtDto.getCdbtCode()).stream()
                .filter(dto -> dto.getUserId().equals(userAndCdbtDto.getUserId()) )
                .collect(Collectors.toList()).forEach(dto -> {
                    userCdbtMappingService.delete(dto);
                });
        return "success";
    }
}
