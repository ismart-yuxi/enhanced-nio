package io.github.yx.socket.practice.deencode;

import io.github.yx.socket.practice.util.Config;
import io.github.yx.socket.practice.util.DynamicByteBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * PrivateProtocolDecoder类实现了Decoder接口，用于解码PrivateMessage。
 */
public class PrivateProtocolDecoder implements Decoder<PrivateMessage> {
    private DynamicByteBuffer buffer = new DynamicByteBuffer(Config.getIntProperty("buffer.size"));

    @Override
    public PrivateMessage decode(ByteBuffer inputBuffer) throws IOException {
        buffer.put(inputBuffer);
        ByteBuffer byteBuffer = buffer.getBuffer();

        if (byteBuffer.remaining() < 18) {
            return null; // Not enough data to read the header
        }

        byte version = byteBuffer.get();
        byte type = byteBuffer.get();
        int sequence = byteBuffer.getInt();
        long timestamp = byteBuffer.getLong();
        int length = byteBuffer.getInt();
        int checksum = byteBuffer.getInt();

        if (byteBuffer.remaining() < length) {
            byteBuffer.position(byteBuffer.position() - 18); // Rewind to the start of the header
            return null; // Not enough data to read the body
        }

        byte[] data = new byte[length];
        byteBuffer.get(data);

        if (checksum != calculateChecksum(data)) {
            throw new IOException("Checksum mismatch");
        }

        return new PrivateMessage(version, type, sequence, timestamp, checksum, data);
    }

    /**
     * 计算消息数据的校验和。
     *
     * @param data 消息数据
     * @return 校验和
     */
    private int calculateChecksum(byte[] data) {
        int checksum = 0;
        for (byte b : data) {
            checksum += b;
        }
        return checksum;
    }
}