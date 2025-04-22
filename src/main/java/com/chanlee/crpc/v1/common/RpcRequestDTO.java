package com.chanlee.crpc.v1.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 请求对象体
 */
@Builder
@Data
public class RpcRequestDTO implements Serializable {
    /**
     * 接口名
     */
    private String interfaceName;

    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    private Object[] params;

    /**
     * 参数类型
     */
    private Class<?>[] paramsTypes;
}
