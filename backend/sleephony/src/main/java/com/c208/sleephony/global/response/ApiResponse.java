package com.c208.sleephony.global.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    private int status;
    private String code;
    private String message;
    private T results;

    public static <T> ApiResponse<T> success(HttpStatus status, T results) {
        return ApiResponse.<T>builder()
                .status(status.value())
                .code("SU")
                .message(DEFAULT_SUCCESS_MESSAGE)
                .results(results)
                .build();
    }

    public static <T> ApiResponse<T> fail(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .status(status.value())
                .code(String.valueOf(status.value()))
                .message(message)
                .results(null)
                .build();
    }

    public static <T> ApiResponse<T> fail(HttpStatus status, String code, String message) {
        return ApiResponse.<T>builder()
                .status(status.value())
                .code(code)
                .message(message)
                .results(null)
                .build();
    }
}