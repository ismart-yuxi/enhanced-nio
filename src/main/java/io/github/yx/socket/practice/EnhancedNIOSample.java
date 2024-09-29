package io.github.yx.socket.practice;


import io.github.yx.socket.practice.core.Bootstrap;
import io.github.yx.socket.practice.deencode.PrivateProtocolDecoder;
import io.github.yx.socket.practice.deencode.PrivateProtocolEncoder;
import io.github.yx.socket.practice.handler.ReadHandler;
import io.github.yx.socket.practice.handler.WriteHandler;
import io.github.yx.socket.practice.plugin.impl.HeartbeatPlugin;

import java.io.IOException;

// 主类
public class EnhancedNIOSample {
    public static void main(String[] args) throws IOException {
        Bootstrap bootstrap = new Bootstrap.Builder()
                .addHandler(new ReadHandler(new PrivateProtocolDecoder()))
                .addHandler(new WriteHandler(new PrivateProtocolEncoder()))
                .enableSessionManagement()
                .addPlugin(new HeartbeatPlugin())
//                .addPlugin(new PerformanceMonitorPlugin())
                .build();

        bootstrap.startServer(8080);
    }
}
