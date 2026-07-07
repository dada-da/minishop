package dev.dada.minishop.user;

import dev.dada.minishop.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TASK MS-10: Entity User (email unique, passwordHash, fullName, role, enabled).
 */
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "email",unique = true)
    private String email;
    // TODO MS-10: cac field + @Enumerated role
}
