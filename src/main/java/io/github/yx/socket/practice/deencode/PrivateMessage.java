package io.github.yx.socket.practice.deencode;

/**
 * PrivateMessage类表示私有协议消息的结构。
 */
public class PrivateMessage {
    private byte version; // 协议版本
    private byte type; // 消息类型
    private int sequence; // 消息序列号
    private long timestamp; // 时间戳
    private int checksum; // 校验和
    private byte[] data; // 消息数据

    public PrivateMessage(byte version, byte type, int sequence, long timestamp, int checksum, byte[] data) {
        this.version = version;
        this.type = type;
        this.sequence = sequence;
        this.timestamp = timestamp;
        this.checksum = checksum;
        this.data = data;
    }

    // Getter方法
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
