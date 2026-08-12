package dev.dada.minishop.common;

import dev.dada.minishop.exception.ErrorResponse;

/**
 * TASK MS-03: Wrapper response chung { success, message, data }.
 * Optional - de API tra ve format dong nhat.
 */
public record ApiResponse<T>(boolean success, String message, T data) {
    // TODO MS-03: static factory methods ok(...) / error(...)
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, null, data);
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(true, null, null);
    }

    public static ApiResponse<ErrorResponse> error(ErrorResponse errorResponse) {
        return new ApiResponse<>(false, null, errorResponse);
    }
}
