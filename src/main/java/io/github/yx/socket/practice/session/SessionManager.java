package io.github.yx.socket.practice.session;


import io.github.yx.socket.practice.core.ChannelPipeline;

import java.nio.channels.SocketChannel;
import java.util.Collection;

// SessionManager接口
public interface SessionManager {
    void addSession(SocketChannel channel, ChannelPipeline pipeline);

    Session getSession(SocketChannel channel);

    void removeSession(SocketChannel channel);

    Collection<Session> getAllSessions();
}