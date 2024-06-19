package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.embeddable.UserLpgeMappingEntityIdDto;
import com.datamon.datamon2.servcie.repository.UserLpgeMappingService;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonService {
    private JwtUtil jwtUtil;
    private UserLpgeMappingService userLpgeMappingService;

    public CommonService(JwtUtil jwtUtil, UserLpgeMappingService userLpgeMappingService) {
        this.jwtUtil = jwtUtil;
        this.userLpgeMappingService = userLpgeMappingService;
    }

    @Transactional
    public List<Map<String, String>> getDBListByUserIdForSession(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession());

        String token = httpSessionUtil.getAttribute("jwt").toString();
        String userIdStr = (String) jwtUtil.getClaims(token).get("sub");
        int userId = Integer.parseInt(userIdStr);

        List<String> lpgeCodeList = userLpgeMappingService.getUserLpgeMappingByUserId(userId).stream()
                .map(UserLpgeMappingEntityIdDto::getLpgeCode)
                .collect(Collectors.toList());

        List<Map<String, String>> result = CommonCodeCache.getLpgeCodes().stream()
                .filter(dto -> lpgeCodeList.contains(dto.getCodeFullName()))
                .map(dto -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("DBName", dto.getCodeValue());
                    map.put("code", dto.getCodeFullName());
                    map.put("Type", "LPGE");
                    return map;
                })
                .collect(Collectors.toList());

        return result;
    }
}
