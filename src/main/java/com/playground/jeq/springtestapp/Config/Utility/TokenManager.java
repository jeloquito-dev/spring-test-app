package com.playground.jeq.springtestapp.Config.Utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenManager {

    public static final long TOKEN_VALIDITY = 12 * 60 * 60; //12 Hours

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    public String generateJwtToken(UserDetails userDetails) {
        Date issuedDateTime = new Date(System.currentTimeMillis());
        Date expirationDateTime = new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000);
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDateTime)
                .setExpiration(expirationDateTime)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey).compact();
    }
    public Boolean validateJwtToken(String token, UserDetails userDetails) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        boolean isTokenExpired = claims.getExpiration().before(new Date());
        boolean isSameUsername = username.equals(userDetails.getUsername());

        return (isSameUsername && !isTokenExpired);
    }

    public String getUsernameFromToken(String token) {
        //Return the subject which contains the username
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
