package com.chanlee.crpc.v3.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.AllArgsConstructor;

/**
 * Netty RPC 服务器初始化器
 */
@AllArgsConstructor
public class NettyRpcInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 服务提供类
     */
    private final RpcServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //数据Inbound解码器：用于拆包
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
            Integer.MAX_VALUE,   // 最大数据包长度（包含长度字段）
            0,// 长度字段偏移量（字节数）（从包头开始算）
            4,// 长度字段长度（字节数）（此处是int，所以占 4 字节）
            0, // 从长度字段结束位置到实际数据开始位置的偏移（此处无偏移）
            4 // 跳过长度字段本身（解码后，从数据包头部开始跳过多少字节才将内容交给下一个 Handler）
        ));
        //反序列化
        pipeline.addLast(new KryoDecoder());
        //数据Outbound编码器：确保能被正确拆包
        pipeline.addLast(new LengthFieldPrepender(4));//给每个出站的数据头部添加 4 字节的长度信息
        //序列化
        pipeline.addLast(new KryoEncoder());
        //添加处理器
        pipeline.addLast(new NettyRpcServerHandler(serviceProvider));
    }
}
