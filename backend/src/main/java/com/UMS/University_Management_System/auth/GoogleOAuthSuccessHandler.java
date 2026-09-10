package com.UMS.University_Management_System.auth;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.UMS.University_Management_System.entity.AppUser;
import com.UMS.University_Management_System.repository.AppUserRepository;

@Component
public class GoogleOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AppUserRepository userRepository;
    private final JwtService jwtService;
    private final String successRedirectUri;

    public GoogleOAuthSuccessHandler(AppUserRepository userRepository, JwtService jwtService,
            @Value("${app.auth.google.success-redirect-uri:http://localhost:5173/}") String successRedirectUri) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.successRedirectUri = successRedirectUri;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");
        if (email == null || email.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Google did not provide an email address.");
            return;
        }
        String name = principal.getAttribute("name");
        AppUser user = userRepository.findByEmail(email.toLowerCase()).orElseGet(() ->
                userRepository.save(new AppUser(name == null || name.isBlank() ? email : name, email.toLowerCase(), null, "GOOGLE")));
        String redirect = UriComponentsBuilder.fromUriString(successRedirectUri)
                .queryParam("token", jwtService.generateToken(user))
                .build()
                .encode()
                .toUriString();
        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, redirect);
    }
}
