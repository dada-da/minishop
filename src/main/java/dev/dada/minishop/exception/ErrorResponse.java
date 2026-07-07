package dev.dada.minishop.exception;

import java.time.Instant;
import java.util.Map;

/**
 * TASK MS-24: Cau truc loi tra ve client.
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}
