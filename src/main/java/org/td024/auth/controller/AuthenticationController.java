package org.td024.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.td024.auth.dto.LoginDto;
import org.td024.auth.dto.RegisterDto;
import org.td024.auth.model.AuthenticationDto;
import org.td024.auth.service.AuthenticationService;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterDto registerDto) {
        authenticationService.register(registerDto);
    }

    @PostMapping("/login")
    public AuthenticationDto login(@RequestBody LoginDto loginDto) throws JsonProcessingException {
        return authenticationService.login(loginDto);
    }

    @GetMapping("/refresh-token")
    public AuthenticationDto refresh(@RequestHeader("Refresh-Token") String refreshToken) throws JsonProcessingException {
        return authenticationService.refresh(refreshToken);
    }
}
