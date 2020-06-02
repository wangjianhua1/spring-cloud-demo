package com.wjh.demo.xieyi;

public enum MessageTypeEnum {
    /**
     * 请求消息
     */
    REQUEST((byte) 1),
    /**
     * 响应消息
     */
    RESPONSE((byte) 2),
    /**
     * ping
     */
    PING((byte) 3),
    /**
     * pong
     */
    PONG((byte) 4),
    /**
     * 空消息
     */
    EMPTY((byte) 5);

    private byte type;

    MessageTypeEnum(byte type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static MessageTypeEnum get(byte type) {
        for (MessageTypeEnum value : values()) {
            if (value.type == type) {
                return value;
            }
        }

        throw new RuntimeException("unsupported type: " + type);
    }
}
