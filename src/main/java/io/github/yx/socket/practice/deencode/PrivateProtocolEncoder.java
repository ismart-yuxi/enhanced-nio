package io.github.yx.socket.practice.deencode;

import java.nio.ByteBuffer;

// 私有协议编码器
public class PrivateProtocolEncoder implements Encoder<Object> {
    @Override
    public ByteBuffer encode(Object data) {
        PrivateMessage message = (PrivateMessage) data;
        ByteBuffer buffer = ByteBuffer.allocate(18 + message.getData().length);
        buffer.put(message.getVersion());
        buffer.put(message.getType());
        buffer.putInt(message.getSequence());
        buffer.putLong(message.getTimestamp());
        buffer.putInt(message.getData().length);
        buffer.putInt(calculateChecksum(message.getData()));
        buffer.put(message.getData());
        buffer.flip();
        return buffer;
    }

    private int calculateChecksum(byte[] data) {
        int checksum = 0;
        for (byte b : data) {
            checksum += b;
        }
        return checksum;
    }
}
