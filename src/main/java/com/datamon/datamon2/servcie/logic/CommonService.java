package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.LpgeCodeDto;
import com.datamon.datamon2.servcie.repository.UserCdbtMappingService;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonService {
    private JwtUtil jwtUtil;
    private UserCdbtMappingService userCdbtMappingService;

    public CommonService(JwtUtil jwtUtil, UserCdbtMappingService userCdbtMappingService) {
        this.jwtUtil = jwtUtil;
        this.userCdbtMappingService = userCdbtMappingService;
    }

    @Transactional
    public List<Map<String, String>> getDBListByUserIdForSession(HttpServletRequest request) throws Exception{
        List<Map<String, String>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession());

        String token = httpSessionUtil.getAttribute("jwt").toString();
        String userIdStr = (String) jwtUtil.getClaims(token).get("sub");
        int userId = Integer.parseInt(userIdStr);

        userCdbtMappingService.getuserCdbtListByUserId(userId).forEach(dto -> {
            Map<String, String> map = new HashMap<>();
            switch (dto.getCdbtCode()){
                case "LPGE":
                    List<LpgeCodeDto> lpgecodes = CommonCodeCache.getLpgeCodes().stream()
                            .filter(lpgeCode -> lpgeCode.getCodeFullName().equals(dto.getCdbtLowCode()))
                            .collect(Collectors.toList());
                    if(lpgecodes.size() > 0){
                        map.put("DBName", lpgecodes.get(0).getCodeValue());
                        map.put("code", lpgecodes.get(0).getCodeFullName());
                        map.put("Type", dto.getCdbtCode());
                    }
                    result.add(map);
                    break;
                default:
                    break;
            }
        });

        return result;
    }

}
