package com.netty4.rpc.client.factory;

import com.netty4.rpc.client.config.Constants;
import com.netty4.rpc.client.invocation.ClientInvocationHandler;
import com.netty4.rpc.client.router.DefaultServiceRouter;
import com.netty4.rpc.client.router.ServiceRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * @author zhihui.kzh
 * @create 15/11/201821:02
 */
public class DefaultServiceFactory implements IServiceFactory {
    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceFactory.class);


    private ServiceRouter serviceRouter = new DefaultServiceRouter();

    @Override
    public <T> T getService(Class<T> interfaceClass) {
        return getService(interfaceClass, Constants.TIMEOUT);

    }

    @Override
    public <T> T getService(Class<T> interfaceClass, long timeout) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ClientInvocationHandler(interfaceClass, timeout, serviceRouter)
        );
    }

}
