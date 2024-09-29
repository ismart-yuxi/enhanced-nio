package io.github.yx.socket.practice.deencode;

// 私有协议消息类
public class PrivateMessage {
    private byte version;
    private byte type;
    private int sequence;
    private long timestamp;
    private int checksum;
    private byte[] data;

    public PrivateMessage() {
    }

    public PrivateMessage(byte version, byte type, int sequence, long timestamp, int checksum, byte[] data) {
        this.version = version;
        this.type = type;
        this.sequence = sequence;
        this.timestamp = timestamp;
        this.checksum = checksum;
        this.data = data;
    }

    public byte getVersion() {
        return version;
    }

    public byte getType() {
        return type;
    }

    public int getSequence() {
        return sequence;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getChecksum() {
        return checksum;
    }

    public byte[] getData() {
        return data;
    }
}