package io.github.yx.socket.practice.plugin;


import io.github.yx.socket.practice.core.ChannelHandlerContext;

import java.io.IOException;

// 通用插件接口
@FunctionalInterface
public interface Plugin {
    void execute(ChannelHandlerContext ctx, int bytes) throws IOException;
}