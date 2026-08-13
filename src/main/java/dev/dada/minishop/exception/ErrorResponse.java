package dev.dada.minishop.exception;

import java.util.Map;

/**
 * TASK MS-24: Cau truc loi tra ve client.
 */
public record ErrorResponse(
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}
