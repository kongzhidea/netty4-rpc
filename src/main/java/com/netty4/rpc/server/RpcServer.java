package com.netty4.rpc.server;

import com.netty4.rpc.core.codec.HessianDecoder;
import com.netty4.rpc.core.codec.HessianEncoder;
import com.netty4.rpc.core.registry.IRegistry;
import com.netty4.rpc.core.registry.Node;
import com.netty4.rpc.core.registry.RegistryFactory;
import com.netty4.rpc.core.util.IpUtil;
import com.netty4.rpc.server.config.Args;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @auther zhihui.kzh
 * @create 12/9/1720:35
 */
public class RpcServer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Args args;

    // 需要暴漏的服务 实例
    private Map<String, Object> handlerMap = new HashMap<String, Object>();

    private static ThreadPoolExecutor threadPoolExecutor = null;

    private IRegistry registry = RegistryFactory.getInstance().getDefaultRegistry();

    public static void submit(Runnable worker) {
        threadPoolExecutor.execute(worker);
    }

    public Args getArgs() {
        return args;
    }

    public void setArgs(Args args) {
        this.args = args;
    }

    public Map<String, Object> getHandlerMap() {
        return handlerMap;
    }

    public void setHandlerMap(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    // clz为暴漏出去的接口
    public void addHandlerMap(Class clz, Object handler) {
        if (handler == null) {
            throw new RuntimeException(clz.getName() + " handler can not be null!");
        }
        if (!clz.isInstance(handler)) {
            throw new RuntimeException(clz.getName() + " handler can not support!");
        }
        String interfaceName = clz.getName();
        handlerMap.put(interfaceName, handler);
    }

    public void export() throws Exception {
        init();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);

            // 启动 netty 内存池, 4.1.14 默认启用   https://github.com/kongzhidea/java-util/blob/master/netty4/netty4-demo/README.md

            b.option(ChannelOption.SO_BACKLOG, args.SO_BACKLOG);
            b.childOption(ChannelOption.TCP_NODELAY, args.TCP_NODELAY);
            b.childOption(ChannelOption.SO_RCVBUF, args.SO_RCVBUF);
            b.childOption(ChannelOption.SO_SNDBUF, args.SO_SNDBUF);

            b.handler(new LoggingHandler(LogLevel.INFO));


            b.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast("decoder", new HessianDecoder(Integer.MAX_VALUE));
                    pipeline.addLast("encoder", new HessianEncoder());

                    pipeline.addLast("handler", new RpcServerHandler(handlerMap));

                }
            });

            // 服务器绑定端口监听
            ChannelFuture f = b.bind(args.port).sync();

            registryServer();

            // 监听服务器关闭监听
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void registryServer() throws Exception {
        String ip = IpUtil.getLocalAddr();
        Node node = new Node(ip, args.port);

        for (String className : handlerMap.keySet()) {
            registry.registryServer(className, node);
        }
    }

    private void unRegistryServer() throws Exception {
        String ip = IpUtil.getLocalAddr();
        Node node = new Node(ip, args.port);

        for (String className : handlerMap.keySet()) {
            registry.unRegistryServer(className, node);
        }
    }



    private void init() {
        if (args == null) {
            args = new Args();
        }
        if (threadPoolExecutor == null) {
            synchronized (RpcServer.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = new ThreadPoolExecutor(args.workerThreadNum, args.workerThreadNum, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
                }
            }
        }

        // 优雅关机， 关机命令需要是   kill pid， 不能是 kill -9
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    unRegistryServer();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }
}
