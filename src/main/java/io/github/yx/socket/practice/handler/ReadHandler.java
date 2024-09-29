package io.github.yx.socket.practice.handler;


import io.github.yx.socket.practice.core.ChannelHandlerContext;
import io.github.yx.socket.practice.deencode.Decoder;

import java.io.IOException;
import java.nio.ByteBuffer;

// 读处理器
public class ReadHandler implements ChannelHandler {
    private final Decoder<Object> decoder;

    public ReadHandler(Decoder<Object> decoder) {
        this.decoder = decoder;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        ByteBuffer buffer = (ByteBuffer) msg;
        Object decoded = decoder.decode(buffer);
        ctx.fireChannelRead(decoded);
    }
}