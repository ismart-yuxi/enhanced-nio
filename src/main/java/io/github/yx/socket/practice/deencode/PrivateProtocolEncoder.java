package io.github.yx.socket.practice.deencode;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * PrivateProtocolEncoder类实现了Encoder接口，用于编码PrivateMessage。
 */
public class PrivateProtocolEncoder implements Encoder<PrivateMessage> {
    @Override
    public ByteBuffer encode(PrivateMessage message) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(18 + message.getData().length);
        buffer.put(message.getVersion());
        buffer.put(message.getType());
        buffer.putInt(message.getSequence());
        buffer.putLong(message.getTimestamp());
        buffer.putInt(message.getData().length);
        buffer.putInt(calculateChecksum(message.getData()));
        buffer.put(message.getData());
        buffer.flip(); // 切换到读取模式
        return buffer;
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