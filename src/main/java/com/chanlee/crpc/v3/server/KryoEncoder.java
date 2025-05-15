package com.chanlee.crpc.v3.server;

import com.chanlee.crpc.v3.serializer.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Kryo编码类
 */
public class KryoEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] bytes = KryoSerializer.serialize(msg);
        out.writeBytes(bytes);
    }
}
