package io.github.yx.socket.practice.core;


import io.github.yx.socket.practice.handler.ChannelHandler;
import io.github.yx.socket.practice.plugin.Plugin;
import io.github.yx.socket.practice.plugin.PluginChain;
import io.github.yx.socket.practice.session.SessionContext;
import io.github.yx.socket.practice.session.SessionManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Bootstrap类
public class Bootstrap {
    private final List<ChannelHandler> handlers;
    private final ExecutorService ioExecutor;
    private final ExecutorService workerExecutor;
    private boolean sessionManagementEnabled = false;
    private PluginChain pluginChain = new PluginChain();

    private Bootstrap(Builder builder) {
        this.handlers = builder.handlers;
        this.sessionManagementEnabled = builder.sessionManagementEnabled;
        this.pluginChain = builder.pluginChain;
        this.ioExecutor = builder.ioExecutor;
        this.workerExecutor = builder.workerExecutor;
    }

    public void startServer(int port) throws IOException {
        SessionManager sessionManager = null;
        if (sessionManagementEnabled) {
            sessionManager = new DefaultSessionManager();
            SessionContext.initialize(sessionManager);
        }

        EventLoop eventLoop = new EventLoop(sessionManager, pluginChain, ioExecutor, workerExecutor);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        eventLoop.registerChannel(serverSocketChannel, handlers);
        eventLoop.run();
    }

    public static class Builder {
        private final List<ChannelHandler> handlers = new ArrayList<>();
        private final PluginChain pluginChain = new PluginChain();
        private boolean sessionManagementEnabled = false;
        private ExecutorService ioExecutor = Executors.newSingleThreadExecutor();
        private ExecutorService workerExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        public Builder addHandler(ChannelHandler handler) {
            handlers.add(handler);
            return this;
        }

        public Builder enableSessionManagement() {
            this.sessionManagementEnabled = true;
            return this;
        }

        public Builder addPlugin(Plugin plugin) {
            pluginChain.addPlugin(plugin);
            return this;
        }

        public Builder setIoExecutor(ExecutorService ioExecutor) {
            this.ioExecutor = ioExecutor;
            return this;
        }

        public Builder setWorkerExecutor(ExecutorService workerExecutor) {
            this.workerExecutor = workerExecutor;
            return this;
        }

        public Bootstrap build() {
            return new Bootstrap(this);
        }
    }
}