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

// EventLoop类
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
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, handlers);
    }

    public void run() {
        ioExecutor.submit(() -> {
            while (true) {
                try {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            handleAccept(key);
                        } else if (key.isReadable()) {
                            handleRead(key);
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
        socketChannel.configureBlocking(false);

        List<ChannelHandler> handlers = (List<ChannelHandler>) key.attachment();
        ChannelPipeline pipeline = new ChannelPipeline();
        handlers.forEach(pipeline::addLast);

        if (sessionManager != null) {
            sessionManager.addSession(socketChannel, pipeline);
        }
        socketChannel.register(selector, SelectionKey.OP_READ, pipeline);
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
                    sessionManager.removeSession(channel);
                }
                channel.close();
                pipeline.fireChannelInactive();
            } else {
                buffer.flip();
                if (pluginChain != null) {
                    pluginChain.executeAll(new ChannelHandlerContext(channel, pipeline), bytesRead);
                }
                workerExecutor.submit(() -> {
                    pipeline.fireChannelRead(buffer);
                });
            }
        } catch (IOException e) {
            GlobalExceptionHandler.handle(e);
        }
    }

    public void write(SocketChannel channel, ByteBuffer buffer) throws IOException {
        int bytesWritten = channel.write(buffer);
        if (pluginChain != null) {
            pluginChain.executeAll(new ChannelHandlerContext(channel, null), -bytesWritten);
        }
    }
}