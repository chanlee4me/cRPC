package com.chanlee.crpc.v3.server;

import com.chanlee.crpc.v3.serializer.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Kryo解码类
 */
public class KryoDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 由于前面已使用 LengthFieldBasedFrameDecoder 做了拆包，这里拿到的就是整包数据
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        Object obj = KryoSerializer.deserialize(bytes);
        out.add(obj);
    }
}
