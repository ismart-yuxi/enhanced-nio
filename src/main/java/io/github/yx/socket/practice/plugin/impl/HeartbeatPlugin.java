package io.github.yx.socket.practice.plugin.impl;

import io.github.yx.socket.practice.core.ChannelHandlerContext;
import io.github.yx.socket.practice.plugin.Plugin;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * HeartbeatPlugin类用于发送心跳消息。
 */
public class HeartbeatPlugin implements Plugin {
    private final ByteBuffer heartbeatMessage = ByteBuffer.wrap("HEARTBEAT".getBytes());

    @Override
    public void execute(ChannelHandlerContext ctx, int bytes) throws IOException {
        ctx.channel().write(heartbeatMessage.duplicate()); // 发送心跳消息
    }
}
