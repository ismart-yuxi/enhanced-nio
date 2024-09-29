package io.github.yx.socket.practice.plugin.impl;

import io.github.yx.socket.practice.core.ChannelHandlerContext;
import io.github.yx.socket.practice.plugin.Plugin;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * PerformanceMonitorPlugin类用于监控性能。
 */
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
            printBytes(buffer.array(), bytes);
            System.out.println("Total read bytes: " + totalReadBytes);
        } else {
            ctx.channel().write(buffer);
            totalWriteBytes += bytes;
            printBytes(buffer.array(), bytes);
            System.out.println("Total write bytes: " + totalWriteBytes);
        }
    }

    private void printBytes(byte[] data, int length) {
        for (int i = 0; i < length; i++) {
            if (i % 16 == 0) {
                System.out.printf("%n%04x: ", i);
            }
            System.out.printf("%02x ", data[i]);
        }
        System.out.println();
    }
}