package dev.dada.minishop.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TASK MS-06 + MS-25: JpaSpecificationExecutor de filter dong (theo category, khoang gia, keyword).
 */
public interface ProductRepository
        extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
}
