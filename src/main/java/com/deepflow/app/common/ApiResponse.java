package com.deepflow.app.common;

public record ApiResponse<T>(boolean success, T data, String code, String message) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, data, null, message);
    }

    public static ApiResponse<Void> error(String message) {
        return error(ErrorCode.BAD_REQUEST, message);
    }

    public static ApiResponse<Void> error(ErrorCode code, String message) {
        return new ApiResponse<>(false, null, code.name(), message);
    }
}
