package io.github.yx.socket.practice.deencode;


import java.io.IOException;
import java.nio.ByteBuffer;

// 定义编码器和解码器接口
public interface Encoder<T> {
    ByteBuffer encode(T message) throws IOException;
}
