package com.UMS.University_Management_System.auth;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.UMS.University_Management_System.entity.AppUser;
import com.UMS.University_Management_System.repository.AppUserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final boolean googleEnabled;

    public AuthController(AppUserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            @Value("${app.auth.google.enabled:false}") boolean googleEnabled) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.googleEnabled = googleEnabled;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String name = request.name() == null ? "" : request.name().trim();
        String email = normaliseEmail(request.email());
        String password = request.password() == null ? "" : request.password();
        if (name.isBlank() || !email.contains("@") || password.length() < 8) {
            return ResponseEntity.badRequest().body(Map.of("message", "Enter a name, valid email, and password of at least 8 characters."));
        }
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "An account with that email already exists."));
        }
        AppUser user = userRepository.save(new AppUser(name, email, passwordEncoder.encode(password), "LOCAL"));
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String email = normaliseEmail(request.email());
        AppUser user = userRepository.findByEmail(email).orElse(null);
        if (user == null || user.getPassword() == null || !passwordEncoder.matches(request.password() == null ? "" : request.password(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Email or password is incorrect."));
        }
        return ResponseEntity.ok(authResponse(user));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authentication is required."));
        }
        return userRepository.findByEmail(authentication.getName())
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(UserResponse.from(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User account was not found.")));
    }

    @GetMapping("/google-enabled")
    public Map<String, Boolean> googleEnabled() {
        return Map.of("enabled", googleEnabled);
    }

    private AuthResponse authResponse(AppUser user) {
        return new AuthResponse(jwtService.generateToken(user), UserResponse.from(user));
    }

    private String normaliseEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }
}
