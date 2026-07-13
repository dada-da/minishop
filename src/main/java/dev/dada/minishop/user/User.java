package dev.dada.minishop.user;

import dev.dada.minishop.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

/**
 * TASK MS-10: Entity User (email unique, passwordHash, fullName, role, enabled).
 */
@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email",unique = true)
    @Email(message = "Invalid email format", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "active")
    private Boolean active;

    // TODO MS-10: cac field + @Enumerated role
}
