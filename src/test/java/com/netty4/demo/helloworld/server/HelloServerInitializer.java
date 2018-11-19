package com.netty4.demo.helloworld.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    private Charset charset = Charset.forName("UTF-8");

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 以("\n")为结尾分割的 解码器，DelimiterBasedFrameDecoder 为可以指定分隔符的解码器。
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//        pipeline.addLast("framer", new LineBasedFrameDecoder(8192, true, true)); // 换行分割，也可以这样写


        // 字符串解码 和 编码
        pipeline.addLast("decoder", new StringDecoder(charset));
        pipeline.addLast("encoder", new StringEncoder(charset));

        // 自己的逻辑Handler
        pipeline.addLast("handler", new HelloServerHandler());
    }
}