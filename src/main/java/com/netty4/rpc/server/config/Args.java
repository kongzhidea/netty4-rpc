package com.netty4.rpc.server.config;

import io.netty.util.NettyRuntime;

/**
 * @auther zhihui.kzh
 * @create 12/9/1720:39
 */
public class Args {

    public int port = 9301;
    public int workerThreadNum = NettyRuntime.availableProcessors() + 4;

    // true 则注册到zk上，false则不注册。
    public boolean registryFlag = Boolean.parseBoolean(System.getProperty("com.netty4.socket.registry.flag", "true"));

    public int SO_RCVBUF = Integer.parseInt(System.getProperty("com.netty4.socket.rcvbuf.size", "65535"));
    public int SO_SNDBUF = Integer.parseInt(System.getProperty("com.netty4.socket.sndbuf.size", "65535"));

    public int SO_BACKLOG = Integer.parseInt(System.getProperty("com.netty4.socket.backlog.size", "1024"));
    public boolean TCP_NODELAY = Boolean.parseBoolean(System.getProperty("com.netty4.socket.tcpnodelay", "true"));


}
