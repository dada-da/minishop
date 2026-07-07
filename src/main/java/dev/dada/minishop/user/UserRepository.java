package dev.dada.minishop.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * TASK MS-10
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
