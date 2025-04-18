package com.chanlee.crpc.v1.common;

import com.chanlee.crpc.v1.common.convention.BaseErrorCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class RpcResponse<T> implements Serializable {
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

    public static RpcResponse<Void> success(){
        return new RpcResponse<Void>()
                .setCode(SUCCESS_CODE);
    }

    public static <T> RpcResponse<T> success(T data){
        return new RpcResponse<T>()
                .setCode(SUCCESS_CODE)
                .setData(data);
    }

    public static RpcResponse<Void> failure(){
        return new RpcResponse<Void>()
                .setMessage(BaseErrorCode.SERVER_ERROR.code())
                .setCode(BaseErrorCode.SERVER_ERROR.message());
    }
}
