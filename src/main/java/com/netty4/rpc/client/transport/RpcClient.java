package com.netty4.rpc.client.transport;

import com.netty4.rpc.client.config.Constants;
import com.netty4.rpc.core.registry.Node;
import com.netty4.rpc.core.protocol.RpcRequest;
import com.netty4.rpc.core.codec.HessianDecoder;
import com.netty4.rpc.core.codec.HessianEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author zhihui.kzh
 * @create 14/11/201821:45
 */
public class RpcClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // copy from   https://github.com/apache/incubator-dubbo/blob/6938d487a388cc8cd0a2bc8740fe9ee8d378767e/dubbo-remoting/dubbo-remoting-netty4/src/main/java/org/apache/dubbo/remoting/transport/netty4/NettyClient.java
    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(Constants.DEFAULT_IO_THREADS, new DefaultThreadFactory("NettyClientWorker", true));

    private Bootstrap bootstrap;

    private Channel channel;

    private Node node;

    public synchronized void connect(final Node node) throws InterruptedException {
        this.bootstrap = new Bootstrap();
        this.node = node;
        this.bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        pipeline.addLast("decoder", new HessianDecoder(Integer.MAX_VALUE));
                        pipeline.addLast("encoder", new HessianEncoder());

                        pipeline.addLast("handler", new RpcClientHandler());

                    }
                });

        this.channel = this.bootstrap.connect(node.getHost(), node.getPort()).sync().channel();

        logger.debug("create netty client:" + node.getIdentity());

        this.channel.closeFuture().addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("client disconnect:" + node.toString());
            }
        });
    }

    public boolean isActive() {
        return this.channel.isActive();
    }

    public synchronized void reConnect() throws InterruptedException {
        connect(this.node);
    }

    public void sendRequest(RpcRequest request) {
//        logger.info("channel isActive:" + channel.isActive());

        final CountDownLatch latch = new CountDownLatch(1);

        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
