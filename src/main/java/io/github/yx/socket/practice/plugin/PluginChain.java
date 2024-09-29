package io.github.yx.socket.practice.plugin;


import io.github.yx.socket.practice.core.ChannelHandlerContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 插件链
public class PluginChain {
    private final List<Plugin> plugins = new ArrayList<>();

    public void addPlugin(Plugin plugin) {
        plugins.add(plugin);
    }

    public void executeAll(ChannelHandlerContext ctx, int bytes) throws IOException {
        for (Plugin plugin : plugins) {
            plugin.execute(ctx, bytes);
        }
    }
}
