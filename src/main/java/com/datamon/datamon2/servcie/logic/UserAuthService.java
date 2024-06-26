package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.userAuth.UserListForUserCdbtByCdbtLowCodeDto;
import com.datamon.datamon2.dto.repository.LpgeCodeDto;
import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.dto.repository.UserCdbtMappingDto;
import com.datamon.datamon2.dto.repository.UserPermissionInfomationDto;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.servcie.repository.UserCdbtMappingService;
import com.datamon.datamon2.servcie.repository.UserPermissionInfomationService;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserAuthService {
    private JwtUtil jwtUtil;
    private UserBaseService userBaseService;
    private UserCdbtMappingService userCdbtMappingService;
    private UserPermissionInfomationService userPermissionInfomationService;

    public UserAuthService(JwtUtil jwtUtil, UserBaseService userBaseService, UserCdbtMappingService userCdbtMappingService, UserPermissionInfomationService userPermissionInfomationService) {
        this.jwtUtil = jwtUtil;
        this.userBaseService = userBaseService;
        this.userCdbtMappingService = userCdbtMappingService;
        this.userPermissionInfomationService = userPermissionInfomationService;
    }

    @Transactional
    public List<Map<String, String>> getUserCdbtLowCodeList (HttpServletRequest request) throws Exception{
        List<Map<String, String>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession());

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
}
