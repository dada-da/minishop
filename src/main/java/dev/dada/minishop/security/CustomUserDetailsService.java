package dev.dada.minishop.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * TASK MS-12: Load user tu DB theo email cho Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    // TODO MS-12: loadUserByUsername(email)
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) {
        return null;
    }
}
