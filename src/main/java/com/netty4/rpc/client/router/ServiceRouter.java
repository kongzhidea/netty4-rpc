package com.netty4.rpc.client.router;

import com.netty4.rpc.client.transport.RpcClient;

/**
 * @author zhihui.kzh
 * @create 15/11/201821:10
 */
public interface ServiceRouter {

    public RpcClient routeService(String className);
}

