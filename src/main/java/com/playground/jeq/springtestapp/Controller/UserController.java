package com.playground.jeq.springtestapp.Controller;

import com.playground.jeq.springtestapp.Config.Utility.JwtUserDetailsService;
import com.playground.jeq.springtestapp.Config.Utility.TokenManager;
import com.playground.jeq.springtestapp.Model.BaseResponse;
import com.playground.jeq.springtestapp.Model.UserRequest;
import com.playground.jeq.springtestapp.Model.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserController {

    private JwtUserDetailsService userDetailsService;

    private AuthenticationManager authenticationManager;

    private TokenManager tokenManager;

    public UserController(JwtUserDetailsService userDetailsService, AuthenticationManager authenticationManager, TokenManager tokenManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/login")
    public ResponseEntity createToken(@RequestBody UserRequest
                                                request) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwtToken = tokenManager.generateJwtToken(userDetails);

        return ResponseEntity.ok(new UserResponse(jwtToken));
    }

    @ExceptionHandler({BadCredentialsException.class, DisabledException.class})
    public ResponseEntity badCredentialExceptionHandler(Exception e) {
        return ResponseEntity
                .badRequest().body(new BaseResponse<>(null, e.getMessage()));
    }
}
