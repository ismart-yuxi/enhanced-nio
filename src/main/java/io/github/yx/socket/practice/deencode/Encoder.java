package io.github.yx.socket.practice.deencode;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Encoder接口定义了编码消息的方法。
 */
public interface Encoder<T> {
    ByteBuffer encode(T message) throws IOException; // 编码消息为ByteBuffer
}