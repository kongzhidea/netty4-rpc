package com.netty4.rpc.client.transport;

import com.netty4.rpc.client.definition.ResponseFutureManager;
import com.netty4.rpc.core.protocol.RpcResponse;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @auther zhihui.kzh
 * @create 19/9/1710:33
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        ResponseFutureManager.getInstance().futureDone(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);

        super.exceptionCaught(ctx, cause);
    }
}
