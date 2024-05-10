package com.datamon.datamon2.servcie.common;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String createToken(String userId) throws Exception{
        Claims claims = (Claims) Jwts.claims().setSubject(userId);
//        claims.put("roles", roles);
        Date now = new Date();

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

        String result = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(secretKeySpec, SignatureAlgorithm.HS256)
                .compact();
        return result;
    }

    // 토큰 만료 시간이 현재 시간을 지났는지 검증
    public boolean validateToken(Jws<Claims> claims) {

        return !claims.getBody().getExpiration().before(new Date());
    }

    public Jws<Claims> getClaims(String jwt) {
        try {
            return Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(jwt);
        }catch(SignatureException e) {
            return null;
        }
    }
}
