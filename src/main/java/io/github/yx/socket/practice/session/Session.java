package io.github.yx.socket.practice.session;


import io.github.yx.socket.practice.core.ChannelPipeline;

import java.nio.channels.SocketChannel;

// Session类
public class Session {
    private final SocketChannel channel;
    private final ChannelPipeline pipeline;

    public Session(SocketChannel channel, ChannelPipeline pipeline) {
        this.channel = channel;
        this.pipeline = pipeline;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public ChannelPipeline getPipeline() {
        return pipeline;
    }
}