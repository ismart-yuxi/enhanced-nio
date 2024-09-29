package io.github.yx.socket.practice.core;

import io.github.yx.socket.practice.handler.ChannelHandler;
import io.github.yx.socket.practice.plugin.Plugin;
import io.github.yx.socket.practice.plugin.PluginChain;
import io.github.yx.socket.practice.session.SessionContext;
import io.github.yx.socket.practice.session.SessionManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Bootstrap类用于启动服务端和客户端。
 */
public class Bootstrap {
    private final List<ChannelHandler> handlers;
    private boolean sessionManagementEnabled = false;
    private PluginChain pluginChain = new PluginChain();
    private ExecutorService ioExecutor;
    private ExecutorService workerExecutor;

    private Bootstrap(Builder builder) {
        this.handlers = builder.handlers;
        this.sessionManagementEnabled = builder.sessionManagementEnabled;
        this.pluginChain = builder.pluginChain;
        this.ioExecutor = builder.ioExecutor;
        this.workerExecutor = builder.workerExecutor;
    }

    /**
     * 启动服务端。
     *
     * @param port 服务端口
     * @throws IOException IO异常
     */
    public void startServer(int port) throws IOException {
        SessionManager sessionManager = null;
        if (sessionManagementEnabled) {
            sessionManager = new DefaultSessionManager();
            SessionContext.initialize(sessionManager);
        }

        EventLoop eventLoop = new EventLoop(sessionManager, pluginChain, ioExecutor, workerExecutor);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port)); // 绑定端口
        serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式
        eventLoop.registerChannel(serverSocketChannel, handlers); // 注册服务端通道
        eventLoop.run(); // 运行事件循环
    }

    /**
     * 启动客户端。
     *
     * @param host 客户端连接的主机
     * @param port 客户端连接的端口
     * @throws IOException IO异常
     */
    public void startClient(String host, int port) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(host, port)); // 连接到服务端
        socketChannel.configureBlocking(false); // 设置为非阻塞模式

        EventLoop eventLoop = new EventLoop(null, pluginChain, ioExecutor, workerExecutor);
        eventLoop.registerChannel(socketChannel, handlers); // 注册客户端通道
        eventLoop.run(); // 运行事件循环
    }

    public static class Builder {
        private final List<ChannelHandler> handlers = new ArrayList<>();
        private boolean sessionManagementEnabled = false;
        private PluginChain pluginChain = new PluginChain();
        private ExecutorService ioExecutor = Executors.newSingleThreadExecutor();
        private ExecutorService workerExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        public Builder addHandler(ChannelHandler handler) {
            handlers.add(handler); // 添加处理器
            return this;
        }

        public Builder enableSessionManagement() {
            this.sessionManagementEnabled = true; // 启用会话管理
            return this;
        }

        public Builder addPlugin(Plugin plugin) {
            pluginChain.addPlugin(plugin); // 添加插件
            return this;
        }

        public Builder setIoExecutor(ExecutorService ioExecutor) {
            this.ioExecutor = ioExecutor; // 设置IO线程池
            return this;
        }

        public Builder setWorkerExecutor(ExecutorService workerExecutor) {
            this.workerExecutor = workerExecutor; // 设置工作线程池
            return this;
        }

        public Bootstrap build() {
            return new Bootstrap(this); // 构建Bootstrap实例
        }
    }
}
