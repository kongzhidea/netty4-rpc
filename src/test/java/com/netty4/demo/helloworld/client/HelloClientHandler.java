package com.netty4.demo.helloworld.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HelloClientHandler extends SimpleChannelInboundHandler<String> {

    // 收到服务端消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        
        System.out.println("Server say : " + msg);
    }

    // 连接上服务端
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active ");
        super.channelActive(ctx);
    }

    // 服务端关闭
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client close ");
        super.channelInactive(ctx);
    }
}