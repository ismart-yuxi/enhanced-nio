package io.github.yx.socket.practice.util;

import java.nio.ByteBuffer;

/**
 * DynamicByteBuffer类用于动态管理ByteBuffer的大小。
 */
public class DynamicByteBuffer {
    private ByteBuffer buffer;

    public DynamicByteBuffer(int initialCapacity) {
        buffer = ByteBuffer.allocate(initialCapacity);
    }

    /**
     * 将数据放入缓冲区。
     *
     * @param src 要放入的ByteBuffer
     */
    public void put(ByteBuffer src) {
        ensureCapacity(src.remaining());
        buffer.put(src);
    }

    /**
     * 获取当前缓冲区的ByteBuffer。
     *
     * @return 当前的ByteBuffer
     */
    public ByteBuffer getBuffer() {
        buffer.flip(); // 切换到读取模式
        return buffer;
    }

    /**
     * 确保缓冲区有足够的容量。
     *
     * @param additionalCapacity 额外需要的容量
     */
    private void ensureCapacity(int additionalCapacity) {
        if (buffer.remaining() < additionalCapacity) {
            int newCapacity = buffer.capacity() * 2 + additionalCapacity;
            ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
            buffer.flip();
            newBuffer.put(buffer);
            buffer = newBuffer;
        }
    }
}
