package io.github.yx.socket.practice.handler;

import io.github.yx.socket.practice.core.ChannelHandlerContext;
import io.github.yx.socket.practice.deencode.Decoder;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * ReadHandler类用于处理读取的消息。
 */
public class ReadHandler<T> implements ChannelHandler {
    private final Decoder<T> decoder;

    public ReadHandler(Decoder<T> decoder) {
        this.decoder = decoder;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        ByteBuffer buffer = (ByteBuffer) msg;
        T decoded = decoder.decode(buffer);
        if (decoded != null) {
            ctx.fireChannelRead(decoded); // 传递解码后的消息
        }
    }
}