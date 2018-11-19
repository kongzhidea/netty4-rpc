package com.netty4.demo.helloworld.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelloServer {

    /**
     * 服务端监听的端口地址
     */
    private static final int portNumber = 7878;

    public static void main(String[] args) throws InterruptedException, IOException {


        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup(1);
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {

                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workerGroup);
                    b.channel(NioServerSocketChannel.class);

                    // socket等待队列长度
//            b.option(ChannelOption.SO_BACKLOG, 100);

//            b.handler(new LoggingHandler(LogLevel.INFO));

                    b.childHandler(new HelloServerInitializer());

                    // 服务器绑定端口监听
                    ChannelFuture f = b.bind(portNumber).sync();
                    // 监听服务器关闭监听
                    f.channel().closeFuture().sync();


                    // 可以简写为
            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }
        });

        th.start();


        // 控制台输入
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (; ; ) {
            String line = in.readLine();
            if (line == null) {
                continue;
            }

            HelloServerHandler.sendAllMsg(line);
        }
    }
}