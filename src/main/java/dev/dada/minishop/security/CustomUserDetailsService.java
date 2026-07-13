package dev.dada.minishop.security;

import dev.dada.minishop.user.CustomUserDetails;
import dev.dada.minishop.user.User;
import dev.dada.minishop.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * TASK MS-12: Load user tu DB theo email cho Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    // TODO MS-12: loadUserByUsername(email)
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new CustomUserDetails(user.get());
    }
}
