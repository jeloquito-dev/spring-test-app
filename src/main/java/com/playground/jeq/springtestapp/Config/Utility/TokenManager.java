package com.playground.jeq.springtestapp.Config.Utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.playground.jeq.springtestapp.Service.AppUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class TokenManager {

    public static final long HOUR = 60 * 60;
    public static final long JWT_TOKEN_VALIDITY = 1 * HOUR; //1 Hour
    public static final long REFRESH_TOKEN_VALIDITY = 6 * HOUR; //6 Hours

    private final AppUserService appUserService;

    public TokenManager(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    public Map<String, String> generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();

        Date issuedDateTime = new Date(System.currentTimeMillis());
        Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey.getBytes());

        Function<Date, String> generateAccessToken = date -> JWT.create()
                .withSubject(username)
                .withIssuedAt(issuedDateTime)
                .withExpiresAt(date)
                .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        Function<Date, String> generateRefreshToken = date -> JWT.create()
                        .withSubject(username)
                        .withIssuedAt(issuedDateTime)
                        .withExpiresAt(date)
                        .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", generateAccessToken.apply(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)));
        tokens.put("refresh_token", generateRefreshToken.apply(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY * 1000)));

        return tokens;
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token) throws Exception {

        Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        boolean isTokenExpired = decodedJWT.getExpiresAt().before(new Date());

        if (isTokenExpired) {
            throw new Exception("Access token already expired.");
        } else {
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
    }

}
