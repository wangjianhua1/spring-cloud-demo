package com.wjh.demo.xieyi;

public class Constants {
    /**
     * 会话id
     */
    public static final int SESSION_ID_LENGTH = 8;
    /**
     * 主版本号
     */
    public static final int MAIN_VERSION = 1;
    /**
     * 次版本号
     */
    public static final int SUB_VERSION = 1;
    /**
     * 魔数,固定数字用于指定当前字节序列是当前类型的协议比如java生成的class文件起始用0xCAFEBABE作为标识服，这里将其定义为0x1314
     */
    public static final int MAGIC_NUMBER = 4;
    /**
     * 修订版本号
     */
    public static final int MODIFY_VERSION = 1;
}
