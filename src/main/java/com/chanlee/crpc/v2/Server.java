package com.chanlee.crpc.v2;

import com.chanlee.crpc.v2.common.config.ServerConfig;
import com.chanlee.crpc.v2.handler.RequestHandler;
import com.chanlee.crpc.v2.handler.RequestHandlerImpl;
import com.chanlee.crpc.v2.registry.ServiceRegistry;
import com.chanlee.crpc.v2.registry.ServiceRegistryImpl;

/**
 * 服务端启动类
 */
public class Server {
    public static void main(String[] args) {
        //创建服务注册中心
        ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
        //注册服务
        serviceRegistry.register("com.chanlee.crpc.v2.service.BlogService", new com.chanlee.crpc.v2.serviceImpl.BlogServiceImpl());
        serviceRegistry.register("com.chanlee.crpc.v2.service.UserService", new com.chanlee.crpc.v2.serviceImpl.UserServiceImpl());
        //创建请求处理器
        RequestHandler requestHandler = new RequestHandlerImpl(serviceRegistry);

        //创建服务器配置
        ServerConfig serverConfig = new ServerConfig(8005);

        //创建并启动服务器
        RpcServer server = new RpcServer(serviceRegistry, requestHandler, serverConfig);
        server.start();
    }
}
