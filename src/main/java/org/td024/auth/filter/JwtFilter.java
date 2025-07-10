package org.td024.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.td024.auth.dao.UserRepository;
import org.td024.auth.entity.AppUser;
import org.td024.auth.enums.TokenType;
import org.td024.auth.service.JwtService;
import org.td024.exception.CustomException;
import org.td024.exception.GoneException;
import org.td024.exception.UnauthorizedException;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);

            if (token != null) {
                jwtService.validate(token);
                String subject = jwtService.getSubject(token);
                String[] parts = subject.split(":");

                TokenType tokenType = TokenType.valueOf(parts[1]);
                if (tokenType != TokenType.ACCESS) throw new UnauthorizedException("Unauthorized");

                if (parts.length != 3) throw new UnauthorizedException("Unauthorized");
                String username = parts[0];

                if (!userRepository.existsById(username)) throw new UnauthorizedException("Unauthorized");
                AppUser user = userRepository.findById(username).get();

                int sessionNo = Integer.parseInt(parts[2]);
                if (user.getSessionNo() != sessionNo)
                    throw new GoneException("Session Expired: You've logged in somewhere else");

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (CustomException e) {
            response.sendError(e.getStatus().value(), e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) return null;
        if (!authorizationHeader.startsWith("Bearer ")) throw new UnauthorizedException("Unauthorized");
        return authorizationHeader.substring(7);
    }
}
