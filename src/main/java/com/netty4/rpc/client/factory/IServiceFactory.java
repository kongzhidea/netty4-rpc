package com.netty4.rpc.client.factory;

/**
 * @author zhihui.kzh
 * @create 15/11/201821:01
 */
public interface IServiceFactory {

    public <T> T getService(Class<T> interfaceClass);

    public <T> T getService(Class<T> interfaceClass, long timeout);

}
