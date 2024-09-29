package io.github.yx.socket.practice.plugin.impl;


import io.github.yx.socket.practice.core.ChannelHandlerContext;
import io.github.yx.socket.practice.plugin.Plugin;

import java.io.IOException;
import java.nio.ByteBuffer;

// 性能监控插件
public class PerformanceMonitorPlugin implements Plugin {
    private long totalReadBytes = 0;
    private long totalWriteBytes = 0;

    @Override
    public synchronized void execute(ChannelHandlerContext ctx, int bytes) throws IOException {
        if (bytes >= 0) {
            processBytes(ctx, bytes, true);
        } else {
            processBytes(ctx, -bytes, false);
        }
    }

    private void processBytes(ChannelHandlerContext ctx, int bytes, boolean isRead) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        if (isRead) {
            ctx.channel().read(buffer);
            totalReadBytes += bytes;
            System.out.println("Read bytes: " + new String(buffer.array(), 0, bytes));
            System.out.println("Total read bytes: " + totalReadBytes);
        } else {
            ctx.channel().write(buffer);
            totalWriteBytes += bytes;
            System.out.println("Written bytes: " + new String(buffer.array(), 0, bytes));
            System.out.println("Total write bytes: " + totalWriteBytes);
        }
    }
}