package io.github.yx.socket.practice.handler;


import io.github.yx.socket.practice.core.ChannelHandlerContext;
import io.github.yx.socket.practice.deencode.Encoder;

import java.io.IOException;
import java.nio.ByteBuffer;

// 写处理器
public class WriteHandler implements ChannelHandler {
    private final Encoder<Object> encoder;

    public WriteHandler(Encoder<Object> encoder) {
        this.encoder = encoder;
    }

    @Override
    public void channelWrite(ChannelHandlerContext ctx, Object msg) throws IOException {
        ByteBuffer encoded = encoder.encode(msg);
        ctx.channel().write(encoded);
    }
}
