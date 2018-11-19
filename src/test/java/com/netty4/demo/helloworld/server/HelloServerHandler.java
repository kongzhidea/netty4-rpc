package com.netty4.demo.helloworld.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class HelloServerHandler extends SimpleChannelInboundHandler<String> {

    private static Map<String, Channel> channelMap = new HashMap<String, Channel>();

    // 收到客户端消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 收到消息直接打印输出
        System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);

        // 返回客户端消息 - 我已经接收到了你的消息
        ctx.writeAndFlush("Received your message !\n");
    }

    // 读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }


    /*
     * 
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     * 
     * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelMap.put(ctx.channel().remoteAddress().toString(), ctx.channel());

        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

        ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");

        super.channelActive(ctx);
    }


    // 服务端关闭
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        channelMap.remove(ctx.channel().remoteAddress().toString());
        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " close !");
        super.channelInactive(ctx);
    }

    public static void sendAllMsg(String msg) {
        for (String key : channelMap.keySet()) {
            System.out.println("debug:" + key);

            Channel channel = channelMap.get(key);
            channel.writeAndFlush(msg + "\n");
        }
    }
}