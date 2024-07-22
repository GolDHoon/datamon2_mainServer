package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.user.CopanyAndCdbtDto;
import com.datamon.datamon2.dto.input.user.CopanyListAndCdbtDto;
import com.datamon.datamon2.dto.input.user.UserAndCdbtDto;
import com.datamon.datamon2.dto.input.userAuth.UserAuthModifyDto;
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
    public Map<String, List> getUserListForUserCdbtByCdbtLowCode (HttpServletRequest request, UserListForUserCdbtByCdbtLowCodeDto userListForUserCdbtByCdbtLowCodeDto)  throws Exception {
        Map<String, List> result = new HashMap<>();
        List<Map<String, String>> rows = new ArrayList<>();

        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto sessionUser = userBaseService.getUserBaseById(userId);
        int sessionCompanyId = Integer.parseInt((String) httpSessionUtil.getAttribute("companyId"));

        List<String> commonPermission = CommonCodeCache.getCommonPermissionCodes().stream()
                .map(code -> {return code.getCodeFullName();})
                .collect(Collectors.toList());

        String viewerCode = CommonCodeCache.getCommonPermissionCodes().stream()
                .filter(code -> code.getCodeValue().equals("뷰어"))
                .map(code -> {return code.getCodeFullName();})
                .findFirst().orElse("");

        List<String> companyCodes = CommonCodeCache.getCompanyCode().stream()
                .map(code -> {
                    return code.getCodeFullName();
                })
                .collect(Collectors.toList());

        List<String> memberCodes = CommonCodeCache.getMemberCodes().stream()
                .map(code -> {
                    return code.getCodeFullName();
                })
                .collect(Collectors.toList());

        List<String> masterCodes = CommonCodeCache.getMasterCodes().stream()
                .map(code -> {
                    return code.getCodeFullName();
                })
                .collect(Collectors.toList());

        List<String> finalKeyList = new ArrayList<>();
        userCdbtMappingService.getUserCdbtListByCdbtLowCode(userListForUserCdbtByCdbtLowCodeDto.getCdbtCode()).forEach(dto -> {
            Map<String, String> resultRow = new HashMap<>();
            UserBaseDto userBaseById = userBaseService.getUserBaseById(dto.getUserId());
            if(!userBaseById.getUseYn() || userBaseById.getDelYn()){
                return;
            }
            if(memberCodes.contains(userBaseById.getUserType())){
                List<UserPermissionInfomationDto> userPermissionInfomationByUserId = userPermissionInfomationService.getUserPermissionInfomationByUserId(dto.getUserId());
                int userCompanyId;
                if(companyCodes.contains(userBaseById.getUserType())){
                    userCompanyId = userBaseById.getIdx();
                }else{
                    userCompanyId = companyInfomationService.getCompanyInfomationById(memberInfomationService.getMemberInfomationByUserId(userBaseById.getIdx()).getCompanyId()).getUserId();
                }

                boolean customCellParam = (sessionCompanyId == userCompanyId);

                if(masterCodes.contains(sessionUser.getUserType())){
                    customCellParam = true;
                }

                resultRow.put("ID", userBaseById.getUserId());
                resultRow.put("idx", String.valueOf(userBaseById.getIdx()));
                resultRow.put("customCellParam", String.valueOf(customCellParam));
                resultRow.put("권한", userPermissionInfomationByUserId.stream()
                        .filter(UserPermissionInfomationDto::getUseYn)
                        .filter(permission -> commonPermission.contains(permission.getUsatCode()))
                        .map(permission -> {return permission.getUsatCode();})
                        .findFirst().orElse(viewerCode));

                finalKeyList.add("ID");
                finalKeyList.add("권한");

                CommonCodeCache.getUsatCodes().stream()
                        .filter(code -> !commonPermission.contains(code.getCodeFullName()))
                        .collect(Collectors.toList())
                        .forEach(usatCode -> {
                            resultRow.put(usatCode.getCodeValue(), String.valueOf(userPermissionInfomationByUserId.stream()
                                    .filter(code -> code.getUsatCode().equals(usatCode.getCodeFullName()))
                                    .findFirst().orElse(new UserPermissionInfomationDto()).getUseYn()));


                            finalKeyList.add(usatCode.getCodeValue());
                        });
                rows.add(resultRow);
            }
        });

        List<String> keyList = finalKeyList.stream().distinct().collect(Collectors.toList());

        result.put("rows", rows);
        result.put("keyList", keyList);

        return result;
    }
    @Transactional
    public List<Map<String, String>> getUserListByCompanyAndCdbt(HttpServletRequest request, CopanyAndCdbtDto copanyAndCdbtDto) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto sessionUser = userBaseService.getUserBaseById(userId);

        List<String> companyCodes = CommonCodeCache.getCompanyCode().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        int companyId;
        if (companyCodes.contains(sessionUser.getUserType())) {
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
                .filter(dto -> !userIdxList.contains(dto.getUserId()))
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
    public String createUserCdbtMappingByCopanyAndCdbt(HttpServletRequest request, CopanyListAndCdbtDto copanyListAndCdbtDto) throws Exception {
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<String> commonPermissionCodes = CommonCodeCache.getCommonPermissionCodes().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        String cdbtLowCode = copanyListAndCdbtDto.getCdbtLowCode().substring(5, 9);


        UserCdbtMappingDto userCdbtMappingDto = new UserCdbtMappingDto();
        userCdbtMappingDto.setCdbtCode(cdbtLowCode);
        userCdbtMappingDto.setCdbtLowCode(copanyListAndCdbtDto.getCdbtLowCode());
        List<Integer> idxs = copanyListAndCdbtDto.getIdxs();

        List<UserPermissionInfomationDto> userPermissionInfomationDtoList = userPermissionInfomationService.getUserPermissionInfomationByUserId(userId).stream()
                .filter(dto -> dto.getCdbtLowCode().equals(copanyListAndCdbtDto.getCdbtLowCode()))
                .filter(dto -> !commonPermissionCodes.contains(dto.getUsatCode()))
                .collect(Collectors.toList());

        idxs.forEach(idx -> {
            userCdbtMappingDto.setUserId(idx);
            userCdbtMappingService.save(userCdbtMappingDto);

            userPermissionInfomationDtoList.forEach(dto -> {
                dto.setUserId(idx);
                dto.create(userId);
                userPermissionInfomationService.save(dto);
            });

            UserPermissionInfomationDto userPermissionInfomationDto = new UserPermissionInfomationDto();
            userPermissionInfomationDto.setUsatCode("AUTH_USAT_0000000003");
            userPermissionInfomationDto.setCdbtCode(copanyListAndCdbtDto.getCdbtLowCode());
            userPermissionInfomationDto.setUserId(idx);
            userPermissionInfomationDto.setCdbtLowCode(cdbtLowCode);
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

    @Transactional
    public String modifyUserAuth(HttpServletRequest request, UserAuthModifyDto userAuthModifyDto) throws Exception {
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<UserPermissionInfomationDto> userPermissionInfomationByUserId = userPermissionInfomationService.getUserPermissionInfomationByUserId(userAuthModifyDto.getIdx()).stream()
                .filter(dto -> dto.getCdbtLowCode().equals(userAuthModifyDto.getCdbtLowCode()))
                .collect(Collectors.toList());

        List<String> commonPermissionCode = CommonCodeCache.getCommonPermissionCodes().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        if(commonPermissionCode.contains(userAuthModifyDto.getUsatCode())){
            List<UserPermissionInfomationDto> userPermissionInfomation = userPermissionInfomationByUserId.stream()
                    .filter(dto -> commonPermissionCode.contains(userAuthModifyDto.getUsatCode()))
                    .collect(Collectors.toList());

            if(userPermissionInfomation.size() > 0){
                userPermissionInfomation.get(0).setUsatCode(userAuthModifyDto.getUsatCode());
                userPermissionInfomation.get(0).modify(userId);
                userPermissionInfomationService.save(userPermissionInfomation.get(0));
            }else{
                UserPermissionInfomationDto userPermissionInfomationDto = new UserPermissionInfomationDto();
                userPermissionInfomationDto.setUserId(userAuthModifyDto.getIdx());
                userPermissionInfomationDto.setUsatCode(userAuthModifyDto.getUsatCode());
                userPermissionInfomationDto.setCdbtLowCode(userAuthModifyDto.getCdbtLowCode());
                userPermissionInfomationDto.setCdbtCode(userAuthModifyDto.getCdbtLowCode().replace("CDBT_", ""));
                userPermissionInfomationDto.create(userId);
                userPermissionInfomationService.save(userPermissionInfomationDto);
            }
        }else{
            List<UserPermissionInfomationDto> userPermissionInfomation = userPermissionInfomationByUserId.stream()
                    .filter(dto -> dto.getUsatCode().equals(userAuthModifyDto.getUsatCode()))
                    .collect(Collectors.toList());

            if(userPermissionInfomation.size() > 0){
                if(userAuthModifyDto.isValue()){
                    userPermissionInfomation.get(0).setUseYn(true);
                }else{
                    userPermissionInfomation.get(0).setUseYn(false);
                }
                    userPermissionInfomation.get(0).modify(userId);
                    userPermissionInfomationService.save(userPermissionInfomation.get(0));
            }else{
                UserPermissionInfomationDto userPermissionInfomationDto = new UserPermissionInfomationDto();
                userPermissionInfomationDto.setUserId(userAuthModifyDto.getIdx());
                userPermissionInfomationDto.setUsatCode(userAuthModifyDto.getUsatCode());
                userPermissionInfomationDto.setCdbtLowCode(userAuthModifyDto.getCdbtLowCode());
                userPermissionInfomationDto.setCdbtCode(userAuthModifyDto.getCdbtLowCode().replace("CDBT_", ""));
                userPermissionInfomationDto.create(userId);
                if(userAuthModifyDto.isValue()){
                    userPermissionInfomationDto.setUseYn(true);
                }else{
                    userPermissionInfomationDto.setUseYn(false);
                }
                userPermissionInfomationService.save(userPermissionInfomationDto);
            }
        }

        return "success";
    }
}
