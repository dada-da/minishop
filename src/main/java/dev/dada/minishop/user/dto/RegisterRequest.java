package dev.dada.minishop.user.dto;

/**
 * TASK MS-13: { email, password, fullName } + Bean Validation (@Email, @NotBlank, @Size).
 */
public record RegisterRequest(String email, String password, String fullName) {
}
