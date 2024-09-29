package io.github.yx.socket.practice.core;


import java.io.IOException;
import java.nio.channels.SocketChannel;

// ChannelHandlerContext类
public class ChannelHandlerContext {
    private final SocketChannel channel;
    private final ChannelPipeline pipeline;

    public ChannelHandlerContext(SocketChannel channel, ChannelPipeline pipeline) {
        this.channel = channel;
        this.pipeline = pipeline;
    }

    public SocketChannel channel() {
        return channel;
    }

    public ChannelPipeline pipeline() {
        return pipeline;
    }

    public void fireChannelRead(Object msg) throws IOException {
        pipeline.fireChannelRead(msg);
    }

    public void fireChannelWrite(Object msg) throws IOException {
        pipeline.fireChannelWrite(msg);
    }

    public void fireChannelInactive() throws IOException {
        pipeline.fireChannelInactive();
    }

    public void fireChannelActive() throws IOException {
        pipeline.fireChannelActive();
    }
}