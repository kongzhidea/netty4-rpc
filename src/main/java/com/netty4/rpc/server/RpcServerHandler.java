package com.netty4.rpc.server;

import com.alibaba.fastjson.JSON;
import com.netty4.rpc.core.protocol.RpcRequest;
import com.netty4.rpc.core.protocol.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @auther zhihui.kzh
 * @create 15/9/1719:57
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
        }

        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final RpcRequest request) throws Exception {
        RpcServer.submit(new Runnable() {
                             @Override
                             public void run() {
                                 if (logger.isDebugEnabled()) {
//                                     logger.debug("Receive request:" + request.getRequestId());
                                 }

                                 RpcResponse response = new RpcResponse();
                                 response.setRequestId(request.getRequestId());
                                 try {
                                     Object result = handle(request);
                                     response.setResult(result);
                                 } catch (Exception e) {
                                     response.setException(e);
                                     logger.error("RPC Server handle request error:" + e.getMessage(), e);
                                 }
                                 ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                                     @Override
                                     public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                                         logger.debug("Send response for request " + request.getRequestId());
                                     }
                                 });
                             }
                         }
        );
    }

    private Object handle(RpcRequest request) throws Exception {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        // JDK reflect
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        return method.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);

        super.exceptionCaught(ctx, cause);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("RamoteAddress : " + ctx.channel().remoteAddress() + " inActive !");
        }
        super.channelInactive(ctx);
    }
}
