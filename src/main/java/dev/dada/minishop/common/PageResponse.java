package dev.dada.minishop.common;

import java.util.List;

/**
 * TASK MS-25: DTO phan trang { content, page, size, totalElements, totalPages }.
 * Dung cho list product, list order.
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
