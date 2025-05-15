package com.chanlee.crpc.v3.server;

/**
 * RPC 服务器
 */
public interface RpcServer {
    /**
     * 启动服务器
     */
    void start(int port);

    /**
     * 停止服务器
     */
    void stop();
}
