package dev.dada.minishop.category;

import dev.dada.minishop.common.BaseEntity;
import dev.dada.minishop.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * TASK MS-04: Entity Category (name, slug). Quan he 1-N voi Product.
 */
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name="slug", nullable = false, unique = true)
    private String slug;
}
