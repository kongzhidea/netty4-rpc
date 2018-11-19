package com.netty4.rpc.client.definition;

import com.netty4.rpc.core.protocol.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhihui.kzh
 * @create 15/11/201811:17
 */
public class ResponseFutureManager {

    /**
     * Singleton
     */
    private volatile static ResponseFutureManager _instance;

    private ResponseFutureManager() {
    }

    public static ResponseFutureManager getInstance() {
        if (_instance == null) {
            synchronized (ResponseFutureManager.class) {
                if (_instance == null) {
                    _instance = new ResponseFutureManager();
                }
            }
        }
        return _instance;
    }

    private Map<String, ResponseFuture> rpcFutureMap = new ConcurrentHashMap<String, ResponseFuture>();

    public void registerFuture(ResponseFuture rpcResponseFuture) {
        rpcFutureMap.put(rpcResponseFuture.getRequestId(), rpcResponseFuture);
    }

    public void futureDone(RpcResponse response) {
        rpcFutureMap.remove(response.getRequestId()).done(response);
    }


}
