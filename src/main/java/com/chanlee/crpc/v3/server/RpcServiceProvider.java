package com.chanlee.crpc.v3.server;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务提供类
 * 主要功能是服务的注册和发现
 */
public class RpcServiceProvider {

    /**
     * 服务注册表
     */
    private Map<String, Object> serviceMap = new HashMap<>();

    /**
     * 服务注册
     * @param serviceName 服务名称
     * @param serviceImpl 服务实现
     */
    public void register(String serviceName, Object serviceImpl) {
        serviceMap.put(serviceName, serviceImpl);
    }

    /**
     * 服务发现
     * @param serviceName 服务名称
     * @return 服务实现
     */
    public Object getService(String serviceName) {
        return serviceMap.get(serviceName);
    }
}
