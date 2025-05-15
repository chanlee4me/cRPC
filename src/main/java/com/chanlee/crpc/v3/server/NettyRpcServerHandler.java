package com.chanlee.crpc.v3.server;

import com.chanlee.crpc.v3.dto.RpcRequestDTO;
import com.chanlee.crpc.v3.dto.RpcResponseDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

/**
 * Netty RPC 服务器处理器
 */
@AllArgsConstructor
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequestDTO> {
    /**
     * 服务提供类
     */
    private final RpcServiceProvider serviceProvider;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequestDTO requestDTO) throws Exception {
        //根据requestDTO来知道调用哪一个方法
        RpcResponseDTO response = handle(requestDTO);
        channelHandlerContext.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        cause.printStackTrace();
        ctx.close();
    }

    public RpcResponseDTO handle(RpcRequestDTO request) {
        try {
            //获取服务名称
            String serviceName = request.getInterfaceName();
            //从注册中心获取服务实例
            Object service = serviceProvider.getService(serviceName);
            //获取方法并调用
            if (service == null) {
                return RpcResponseDTO.failure("对应服务不存在：" + serviceName);
            }
            Method method = service.getClass().getMethod(request.getMethod(), request.getParamsTypes());
            Object invoke = method.invoke(service, request.getParams());
            //返回成功执行结果
            return RpcResponseDTO.success(invoke);
        } catch (Exception e) {
            return RpcResponseDTO.failure("服务执行错误：" + e.getMessage());
        }
    }
}
