package dev.dada.minishop.security;

import org.springframework.stereotype.Service;

/**
 * TASK MS-12: Sinh & xac thuc JWT.
 * - generateAccessToken(user) / generateRefreshToken(user)
 * - extractUsername(token), isTokenValid(token, userDetails)
 * - doc secret + expiration tu app.jwt.* (application.yml)
 */
@Service
public class JwtService {
    // TODO MS-12
}
