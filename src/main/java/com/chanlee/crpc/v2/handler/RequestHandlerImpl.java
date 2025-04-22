package com.chanlee.crpc.v2.handler;

import com.chanlee.crpc.v2.dto.RpcRequestDTO;
import com.chanlee.crpc.v2.dto.RpcResponseDTO;
import com.chanlee.crpc.v2.registry.ServiceRegistry;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

/**
 * 请求处理器实现
 */
@AllArgsConstructor
public class RequestHandlerImpl implements RequestHandler{

    private final ServiceRegistry serviceRegistry;

    public  RpcResponseDTO handle(RpcRequestDTO request) {

        try {
            //获取服务名称
            String serviceName = request.getInterfaceName();
            //从注册中心获取服务实例
            Object service = serviceRegistry.getService(serviceName);
            //获取方法并调用
            if(service == null){
                return RpcResponseDTO.failure("对应服务不存在：" +  serviceName);
            }
            Method method = service.getClass().getMethod(request.getMethod(), request.getParamsTypes());
            Object invoke = method.invoke(service, request.getParams());
            //返回成功执行结果
            return RpcResponseDTO.success(invoke);
        } catch (Exception e) {
            return RpcResponseDTO.failure("服务执行错误：" +  e.getMessage());
        }
    }
}
