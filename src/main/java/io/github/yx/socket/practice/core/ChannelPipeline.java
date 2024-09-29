package io.github.yx.socket.practice.core;


import io.github.yx.socket.practice.exception.GlobalExceptionHandler;
import io.github.yx.socket.practice.handler.ChannelHandler;

import java.util.ArrayList;
import java.util.List;

// ChannelPipeline类
public class ChannelPipeline {
    private final List<ChannelHandler> handlers = new ArrayList<>();
    private int readIndex = 0;
    private int writeIndex = 0;

    public void addLast(ChannelHandler handler) {
        handlers.add(handler);
    }

    public void fireChannelRead(Object msg) {
        try {
            if (readIndex < handlers.size()) {
                handlers.get(readIndex++).channelRead(new ChannelHandlerContext(null, this), msg);
            }
        } catch (Exception e) {
            fireExceptionCaught(e);
        }
    }

    public void fireChannelWrite(Object msg) {
        try {
            if (writeIndex < handlers.size()) {
                handlers.get(writeIndex++).channelWrite(new ChannelHandlerContext(null, this), msg);
            }
        } catch (Exception e) {
            fireExceptionCaught(e);
        }
    }

    public void fireChannelInactive() {
        handlers.forEach(handler -> {
            try {
                handler.channelInactive(new ChannelHandlerContext(null, this));
            } catch (Exception e) {
                fireExceptionCaught(e);
            }
        });
    }

    public void fireChannelActive() {
        handlers.forEach(handler -> {
            try {
                handler.channelActive(new ChannelHandlerContext(null, this));
            } catch (Exception e) {
                fireExceptionCaught(e);
            }
        });
    }

    private void fireExceptionCaught(Throwable cause) {
        handlers.forEach(handler -> {
            try {
                handler.exceptionCaught(new ChannelHandlerContext(null, this), cause);
            } catch (Exception e) {
                GlobalExceptionHandler.handle(e);
            }
        });
    }
}