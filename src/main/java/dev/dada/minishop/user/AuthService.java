package dev.dada.minishop.user;

import dev.dada.minishop.exception.BusinessException;
import dev.dada.minishop.security.JwtService;
import dev.dada.minishop.user.dto.AuthResponse;
import dev.dada.minishop.user.dto.LoginRequest;
import dev.dada.minishop.user.dto.RegisterRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TASK MS-13: register (hash password, check trung email), login (xac thuc + sinh token).
 */
@Service
public class AuthService {
    // TODO MS-13
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new BusinessException("Email already exists");
        }

        User user = new User();
        user.setEmail(registerRequest.email());
        user.setFullName(registerRequest.fullName());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.password()));
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        Authentication auth = authenticationManager.authenticate(authToken);

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        String accessToken = jwtService.generateAccessToken(userDetails.getUsername(), userDetails.getUser().getRole().getAuthorityName());
        String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }
}
