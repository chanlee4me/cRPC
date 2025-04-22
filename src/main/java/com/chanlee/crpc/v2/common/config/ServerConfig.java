package com.chanlee.crpc.v2.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 服务端配置
 */
@Data
@AllArgsConstructor
public class ServerConfig {
    private int port = 8005;
}
