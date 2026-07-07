package dev.dada.minishop.common;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * TASK MS-02: Base entity chua id, createdAt, updatedAt (dung @CreatedDate/@LastModifiedDate).
 * Cac entity khac extend class nay.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    // TODO MS-02: id (Long, @Id @GeneratedValue)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name="updated_at")
    @LastModifiedDate
    private Instant updatedAt;
    // TODO MS-02: createdAt, updatedAt voi @EntityListeners(AuditingEntityListener.class)
}
