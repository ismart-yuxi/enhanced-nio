package io.github.yx.socket.practice.handler;


import io.github.yx.socket.practice.core.ChannelHandlerContext;

import java.io.IOException;

// 定义通用的ChannelHandler接口
public interface ChannelHandler {
    default void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
    }

    default void channelWrite(ChannelHandlerContext ctx, Object msg) throws IOException {
    }

    default void channelInactive(ChannelHandlerContext ctx) throws IOException {
    }

    default void channelActive(ChannelHandlerContext ctx) throws IOException {
    }

    default void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    }
}