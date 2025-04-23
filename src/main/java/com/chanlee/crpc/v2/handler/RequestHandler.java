package com.chanlee.crpc.v2.handler;

import com.chanlee.crpc.v2.dto.RpcRequestDTO;
import com.chanlee.crpc.v2.dto.RpcResponseDTO;

/**
 * 请求处理器接口
 */
public interface RequestHandler {
    /**
     * 处理RPC请求
     * @param request RPC请求对象
     * @return RPC响应对象
     */
    public RpcResponseDTO handle(RpcRequestDTO request);
}
