package io.github.yx.socket.practice.deencode;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Decoder接口定义了解码消息的方法。
 */
public interface Decoder<T> {
    T decode(ByteBuffer buffer) throws IOException; // 从ByteBuffer解码消息
}