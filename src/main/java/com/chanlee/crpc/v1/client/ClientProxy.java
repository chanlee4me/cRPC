package com.chanlee.crpc.v1.client;

import com.chanlee.crpc.v1.common.RpcRequestDTO;
import com.chanlee.crpc.v1.common.RpcResponseDTO;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static com.chanlee.crpc.v1.client.IOClient.sendRequest;

/**
 * 客户端代理类
 */
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    /**
     * 服务端 IP
     */
    private String host;

    /**
     * 服务端端口号
     */
    private int port;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request请求
        RpcRequestDTO request = RpcRequestDTO.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .method(method.getName())
                .paramsTypes(method.getParameterTypes())
                .params(args)
                .build();
        //发送请求并获取响应
        RpcResponseDTO<Object> response = sendRequest(host, port, request);
        //返回结果数据
        return response.getData();
    }
    public <T> T getProxy(Class<T> tClass){
        Object o = Proxy.newProxyInstance(
                tClass.getClassLoader(),
                new Class[]{tClass},
                this
        );
        return (T)o;
    }
}
