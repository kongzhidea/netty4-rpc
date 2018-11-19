package com.netty4.rpc.client.config;

/**
 * @author zhihui.kzh
 * @create 14/11/201821:47
 */
public class Constants {

    public static final int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    public static final long TIMEOUT = 3000;
}
