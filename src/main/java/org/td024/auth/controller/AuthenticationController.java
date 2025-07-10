package org.td024.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
    public AuthenticationDto login(@RequestBody LoginDto loginDto) {
        return authenticationService.login(loginDto);
    }
}
