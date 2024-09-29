package io.github.yx.socket.practice.deencode;


import java.io.IOException;
import java.nio.ByteBuffer;

// 私有协议解码器
public class PrivateProtocolDecoder implements Decoder<Object> {
    @Override
    public PrivateMessage decode(ByteBuffer buffer) throws IOException {
        byte version = buffer.get();
        byte type = buffer.get();
        int sequence = buffer.getInt();
        long timestamp = buffer.getLong();
        int length = buffer.getInt();
        int checksum = buffer.getInt();
        byte[] data = new byte[length];
        buffer.get(data);

        if (checksum != calculateChecksum(data)) {
            throw new IOException("Checksum mismatch");
        }

        return new PrivateMessage(version, type, sequence, timestamp, checksum, data);
    }

    private int calculateChecksum(byte[] data) {
        int checksum = 0;
        for (byte b : data) {
            checksum += b;
        }
        return checksum;
    }
}