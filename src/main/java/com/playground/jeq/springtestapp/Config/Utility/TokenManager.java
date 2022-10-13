package com.playground.jeq.springtestapp.Config.Utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.playground.jeq.springtestapp.Service.AppUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.playground.jeq.springtestapp.Config.Utility.StringReference.*;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
                .withClaim(CLAIM_USE, ACCESS_TOKEN)
                .withIssuedAt(issuedDateTime)
                .withExpiresAt(date)
                .withClaim(CLAIM_ROLE, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        Function<Date, String> generateRefreshToken = date -> JWT.create()
                        .withSubject(username)
                        .withClaim(CLAIM_USE, REFRESH_TOKEN)
                        .withIssuedAt(issuedDateTime)
                        .withExpiresAt(date)
                        .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put(ACCESS_TOKEN, generateAccessToken.apply(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)));
        tokens.put(REFRESH_TOKEN, generateRefreshToken.apply(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY * 1000)));

        return tokens;
    }

    public String identifyToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        return (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) ?
                authorizationHeader.substring(BEARER.length()) :
                null;
    }
    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token) throws Exception {

        DecodedJWT decodedJWT = decodeToken(token);

        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim(CLAIM_ROLE).asArray(String.class);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        boolean isTokenExpired = decodedJWT.getExpiresAt().before(new Date());

        if (isTokenExpired) {
            throw new Exception("Access token already expired.");
        } else {
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
    }

    /**
     * Returns the subject from the token
     * @param token
     * @return String username
     */
    public String getUsernameFromToken(String token) {
        return decodeToken(token).getSubject();
    }

    /**
     * Validates if the token is a valid refresh token
     * @param token
     * @return boolean true or false
     */
    public boolean validateRefreshToken(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        String usage = decodedJWT.getClaim(CLAIM_USE).asString();
        return usage.equals(REFRESH_TOKEN);
    }

    /**
     * Returns the verified token using the specified algorithm
     * @param token
     * @return DecodedJWT
     */
    private DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(jwtSecretKey.getBytes()))
                .build()
                .verify(token);
    }



}
