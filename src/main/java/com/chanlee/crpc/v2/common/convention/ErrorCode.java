package com.chanlee.crpc.v2.common.convention;

/**
 * 错误码接口
 */
public interface ErrorCode {
    /**
     * 错误码
     */
    String code();

    /**
     * 错误信息
     */
    String message();
}