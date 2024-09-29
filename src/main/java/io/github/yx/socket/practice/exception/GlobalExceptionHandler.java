package io.github.yx.socket.practice.exception;

// 全局异常处理器
public class GlobalExceptionHandler {
    public static void handle(Throwable cause) {
        System.err.println("Global exception caught: " + cause.getMessage());
        cause.printStackTrace();
    }
}