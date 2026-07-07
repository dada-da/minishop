package dev.dada.minishop.user.dto;

public record AuthResponse(String accessToken, String refreshToken, String tokenType) {
}
