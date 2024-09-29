package io.github.yx.socket.practice.core;


import io.github.yx.socket.practice.session.Session;
import io.github.yx.socket.practice.session.SessionManager;

import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 默认的SessionManager实现
public class DefaultSessionManager implements SessionManager {
    private final Map<SocketChannel, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public void addSession(SocketChannel channel, ChannelPipeline pipeline) {
        sessions.put(channel, new Session(channel, pipeline));
    }

    @Override
    public Session getSession(SocketChannel channel) {
        return sessions.get(channel);
    }

    @Override
    public void removeSession(SocketChannel channel) {
        sessions.remove(channel);
    }

    @Override
    public Collection<Session> getAllSessions() {
        return sessions.values();
    }
}