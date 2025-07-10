package org.td024.auth.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.td024.auth.enums.TokenType;
import org.td024.auth.enums.UserRole;
import org.td024.auth.dao.UserRepository;
import org.td024.auth.dto.LoginDto;
import org.td024.auth.dto.RegisterDto;
import org.td024.auth.entity.AppUser;
import org.td024.auth.model.AuthenticationDto;
import org.td024.exception.ConflictException;
import org.td024.exception.UnauthorizedException;

@Service
public class AuthenticationService {

    @Value("${org.td024.auth.session-timeout}")
    private long sessionTimeout;

    @Value("${org.td024.auth.refresh-timeout}")
    private long refreshTimeout;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterDto dto) {
        String username = dto.getUsername();
        if (userRepository.existsById(username)) throw new ConflictException("Username Already Taken!");

        String password = dto.getPassword();
        String repeatPassword = dto.getRepeatPassword();
        if (!password.equals(repeatPassword)) throw new ConflictException("Passwords Do Not Match!");

        password = passwordEncoder.encode(password);

        AppUser user = new AppUser();

        user.setUsername(username);
        user.setPassword(password);
        user.setRole(UserRole.USER);

        userRepository.save(user);
    }

    @Transactional
    public AuthenticationDto login(LoginDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        if (!userRepository.existsById(username)) throw new UnauthorizedException("Couldn't Authenticate");

        AppUser user = userRepository.getReferenceById(username);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new UnauthorizedException("Couldn't Authenticate");

        int sessionNo = user.getSessionNo() + 1;

        String accessSubject = user.getUsername() + ":" + TokenType.ACCESS + ":" + sessionNo;
        String refreshSubject = user.getUsername() + ":" + TokenType.REFRESH + ":" + sessionNo;

        String accessToken = jwtService.generateToken(accessSubject, sessionTimeout);
        String refreshToken = jwtService.generateToken(refreshSubject, refreshTimeout);

        AuthenticationDto authenticationDto = new AuthenticationDto();

        authenticationDto.setAccessToken(accessToken);
        authenticationDto.setRefreshToken(refreshToken);
        authenticationDto.setUsername(username);

        user.setSessionNo(sessionNo);
        userRepository.save(user);

        return authenticationDto;
    }
}
