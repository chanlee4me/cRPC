package com.chanlee.crpc.v2.registry;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的服务注册实现
 */
@Data
public class ServiceRegistryImpl implements ServiceRegistry{
    private final Map<String, Object> serviceMap = new HashMap<>();

    public void register(String serviceName, Object serviceImpl) {
        serviceMap.put(serviceName, serviceImpl);
    }

    public Object getService(String serviceName) {
        return serviceMap.get(serviceName);
    }
}
