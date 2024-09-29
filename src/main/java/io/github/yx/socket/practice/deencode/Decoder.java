package io.github.yx.socket.practice.deencode;


import java.io.IOException;
import java.nio.ByteBuffer;

public interface Decoder<T> {
    T decode(ByteBuffer buffer) throws IOException;
}
