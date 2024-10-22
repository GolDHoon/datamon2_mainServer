package com.datamon.datamon2.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String createToken(String id, List<Map<String,Object>> claimsList) throws Exception{
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("sub", id);

        claimsList.forEach(map -> {
            claimsMap.put((String) map.get("key"), map.get("value"));
        });

        Claims claims = Jwts.claims(claimsMap);
        Date now = new Date();

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

        String result = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, secretKeySpec)
                .compact();
        return result;
    }

    // 토큰 만료 시간이 현재 시간을 지났는지 검증
    public boolean validateToken(Claims claims) {

        return !claims.getExpiration().before(new Date());
    }

    public Claims getClaims(String token) throws Exception{
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());


        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }


    public int getUserId(String token) throws Exception{
        String userIdStr = (String) getClaims(token).get("sub");
        return Integer.parseInt(userIdStr);
    }
}
