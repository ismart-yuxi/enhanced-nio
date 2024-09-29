package io.github.yx.socket.practice.handler;

import io.github.yx.socket.practice.core.ChannelHandlerContext;
import io.github.yx.socket.practice.deencode.Encoder;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * WriteHandler类用于处理写入的消息。
 */
public class WriteHandler<T> implements ChannelHandler {
    private final Encoder<T> encoder;

    public WriteHandler(Encoder<T> encoder) {
        this.encoder = encoder;
    }

    @Override
    public void channelWrite(ChannelHandlerContext ctx, Object msg) throws IOException {
        ByteBuffer encoded = encoder.encode((T) msg);
        ctx.channel().write(encoded); // 写入编码后的消息
    }
}