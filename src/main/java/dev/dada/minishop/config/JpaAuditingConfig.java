package dev.dada.minishop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * TASK MS-02: Bat JPA Auditing de tu dong dien createdAt/updatedAt cho BaseEntity.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
