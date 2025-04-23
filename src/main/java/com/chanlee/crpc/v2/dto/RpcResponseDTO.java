package com.chanlee.crpc.v2.dto;

import com.chanlee.crpc.v2.common.convention.BaseErrorCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应对象体
 */
@Data
@Accessors(chain = true)
public class RpcResponseDTO<T> implements Serializable {
    /**
     * 正确返回码
     */
    public static final String SUCCESS_CODE = "200";

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public static RpcResponseDTO<Void> success(){
        return new RpcResponseDTO<Void>()
                .setCode(SUCCESS_CODE);
    }

    public static <T> RpcResponseDTO<T> success(T data){
        return new RpcResponseDTO<T>()
                .setCode(SUCCESS_CODE)
                .setData(data);
    }

    public static RpcResponseDTO<Void> failure(){
        return new RpcResponseDTO<Void>()
                .setMessage(BaseErrorCode.SERVER_ERROR.code())
                .setCode(BaseErrorCode.SERVER_ERROR.message());
    }
    public static <T> RpcResponseDTO<T> failure(T data){
        return new RpcResponseDTO<T>()
                .setMessage(BaseErrorCode.SERVER_ERROR.code())
                .setCode(BaseErrorCode.SERVER_ERROR.message())
                .setData(data);
    }
}
