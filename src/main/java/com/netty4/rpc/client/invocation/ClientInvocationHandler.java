package com.netty4.rpc.client.invocation;

import com.netty4.rpc.client.definition.ResponseFuture;
import com.netty4.rpc.client.definition.ResponseFutureManager;
import com.netty4.rpc.client.router.ServiceRouter;
import com.netty4.rpc.client.transport.RpcClient;
import com.netty4.rpc.core.protocol.RpcRequest;
import com.netty4.rpc.core.protocol.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @auther zhihui.kzh
 * @create 18/9/1716:48
 */
public class ClientInvocationHandler implements InvocationHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Class<?> clazz;
    // 毫秒
    private long timeout;

    private ServiceRouter serviceRouter;

    public ClientInvocationHandler(Class<?> clazz, long timeout, ServiceRouter serviceRouter) {
        this.clazz = clazz;
        this.timeout = timeout;
        this.serviceRouter = serviceRouter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        if ("equals".equals(methodName)) {
            return proxy == args[0];
        } else if ("hashCode".equals(methodName)) {
            return System.identityHashCode(proxy);
        } else if ("toString".equals(methodName)) {
            return proxy.getClass().getName() + "@" +
                    Integer.toHexString(System.identityHashCode(proxy)) +
                    ", with InvocationHandler " + this;
        }

        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(this.clazz.getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        RpcClient client = serviceRouter.routeService(request.getClassName());

        client.sendRequest(request);

        ResponseFuture future = new ResponseFuture(request.getRequestId());
        ResponseFutureManager.getInstance().registerFuture(future);

        RpcResponse rpcResponse = future.get(timeout);
        if (rpcResponse.hasException()) {
            throw rpcResponse.getException();
        } else {
            return rpcResponse.getResult();
        }
    }
}
