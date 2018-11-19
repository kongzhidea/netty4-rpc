package com.netty4.demo.helloworld.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class HelloClientInitializer extends ChannelInitializer<SocketChannel> {

    private Charset charset = Charset.forName("UTF-8");

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 这个地方的 必须和服务端对应上。否则无法正常解码和编码
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        //  pipeline.addLast("framer", new LineBasedFrameDecoder(8192, true, true)); // 换行分割，也可以这样写


        pipeline.addLast("decoder", new StringDecoder(charset));
        pipeline.addLast("encoder", new StringEncoder(charset));

        // 客户端的逻辑
        pipeline.addLast("handler", new HelloClientHandler());
    }
}