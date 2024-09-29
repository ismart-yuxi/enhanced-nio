package io.github.yx.socket.practice.core;

import io.github.yx.socket.practice.exception.GlobalExceptionHandler;
import io.github.yx.socket.practice.handler.ChannelHandler;
import io.github.yx.socket.practice.plugin.PluginChain;
import io.github.yx.socket.practice.session.SessionManager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * EventLoop类用于处理I/O事件。
 */
public class EventLoop {
    private final Selector selector;
    private final ExecutorService ioExecutor;
    private final ExecutorService workerExecutor;
    private final SessionManager sessionManager;
    private final PluginChain pluginChain;

    public EventLoop(SessionManager sessionManager, PluginChain pluginChain,
                     ExecutorService ioExecutor, ExecutorService workerExecutor) throws IOException {
        this.selector = Selector.open();
        this.ioExecutor = ioExecutor;
        this.workerExecutor = workerExecutor;
        this.sessionManager = sessionManager;
        this.pluginChain = pluginChain;
    }

    public void registerChannel(ServerSocketChannel serverSocketChannel, List<ChannelHandler> handlers) throws IOException {
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, handlers); // 注册服务端通道
    }

    public void registerChannel(SocketChannel socketChannel, List<ChannelHandler> handlers) throws IOException {
        socketChannel.register(selector, SelectionKey.OP_READ, handlers); // 注册客户端通道
    }

    public void run() {
        ioExecutor.submit(() -> {
            while (true) {
                try {
                    selector.select(); // 选择准备好的通道
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            handleAccept(key); // 处理接受连接
                        } else if (key.isReadable()) {
                            handleRead(key); // 处理读取数据
                        }
                    }
                } catch (IOException e) {
                    GlobalExceptionHandler.handle(e);
                }
            }
        });
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false); // 设置为非阻塞模式

        List<ChannelHandler> handlers = (List<ChannelHandler>) key.attachment();
        ChannelPipeline pipeline = new ChannelPipeline();
        handlers.forEach(pipeline::addLast);

        if (sessionManager != null) {
            sessionManager.addSession(socketChannel, pipeline); // 添加会话
        }
        socketChannel.register(selector, SelectionKey.OP_READ, pipeline); // 注册读取事件
        pipeline.fireChannelActive(); // 触发连接建立事件
    }

    private void handleRead(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ChannelPipeline pipeline = (ChannelPipeline) key.attachment();
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024); // 使用直接缓冲区
        try {
            int bytesRead = channel.read(buffer);
            if (bytesRead == -1) {
                if (sessionManager != null) {
                    sessionManager.removeSession(channel); // 移除会话
                }
                channel.close(); // 关闭通道
                pipeline.fireChannelInactive(); // 触发连接关闭事件
            } else {
                buffer.flip(); // 切换到读取模式
                if (pluginChain != null) {
                    pluginChain.executeAll(new ChannelHandlerContext(channel, pipeline), bytesRead); // 执行插件
                }
                workerExecutor.submit(() -> {
                    pipeline.fireChannelRead(buffer); // 传递读取的数据
                });
            }
        } catch (IOException e) {
            GlobalExceptionHandler.handle(e);
        }
    }

    public void write(SocketChannel channel, ByteBuffer buffer) throws IOException {
        int bytesWritten = channel.write(buffer); // 写入数据
        if (pluginChain != null) {
            pluginChain.executeAll(new ChannelHandlerContext(channel, null), -bytesWritten); // 执行插件
        }
    }
}
