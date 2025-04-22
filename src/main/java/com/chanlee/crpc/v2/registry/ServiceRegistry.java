package com.chanlee.crpc.v2.registry;

/**
 * 服务注册接口
 */
public interface ServiceRegistry {

    /**
     * 注册服务
     * @param serviceName 服务名称
     * @param serviceImpl 服务实现
     */
    void register(String serviceName, Object serviceImpl);

    /**
     * 获取服务
     * @param serviceName 服务名称
     * @return 服务实现
     */
    Object getService(String serviceName);
}
