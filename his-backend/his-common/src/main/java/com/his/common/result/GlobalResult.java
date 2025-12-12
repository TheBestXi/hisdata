package com.his.common.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 全局统一响应类
 */
@Data
@NoArgsConstructor
public class GlobalResult<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;
    private long timestamp;

    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 500;

    private GlobalResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> GlobalResult<T> success() {
        return new GlobalResult<>(SUCCESS_CODE, "操作成功", null);
    }

    public static <T> GlobalResult<T> success(T data) {
        return new GlobalResult<>(SUCCESS_CODE, "操作成功", data);
    }

    public static <T> GlobalResult<T> success(String message, T data) {
        return new GlobalResult<>(SUCCESS_CODE, message, data);
    }

    public static <T> GlobalResult<T> error(String message) {
        return new GlobalResult<>(ERROR_CODE, message, null);
    }

    public static <T> GlobalResult<T> error(Integer code, String message) {
        return new GlobalResult<>(code, message, null);
    }
}
