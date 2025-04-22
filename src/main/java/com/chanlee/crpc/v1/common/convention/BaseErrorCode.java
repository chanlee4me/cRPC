package com.chanlee.crpc.v1.common.convention;

/**
 * 基础错误码
 */
public enum BaseErrorCode implements ErrorCode {
    SERVER_ERROR("A000001", "服务端内部错误");

    private final String code;

    private final String message;

    BaseErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
