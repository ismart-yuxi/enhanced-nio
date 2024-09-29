package io.github.yx.socket.practice.handler;

import io.github.yx.socket.practice.core.ChannelHandlerContext;

import java.io.IOException;

/**
 * ChannelHandler接口用于处理通道事件。
 */
public interface ChannelHandler {
    default void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
    } // 处理读取事件

    default void channelWrite(ChannelHandlerContext ctx, Object msg) throws IOException {
    } // 处理写入事件

    default void channelInactive(ChannelHandlerContext ctx) throws IOException {
    } // 处理通道不活跃事件

    default void channelActive(ChannelHandlerContext ctx) throws IOException {
    } // 处理通道活跃事件

    default void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    } // 处理异常事件
}
