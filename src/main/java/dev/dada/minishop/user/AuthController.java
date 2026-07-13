package dev.dada.minishop.user;

import dev.dada.minishop.common.ApiResponse;
import dev.dada.minishop.user.dto.AuthResponse;
import dev.dada.minishop.user.dto.LoginRequest;
import dev.dada.minishop.user.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TASK MS-13: POST /api/auth/register, POST /api/auth/login, POST /api/auth/refresh
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // TODO MS-13
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        return ApiResponse.ok();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }
}
