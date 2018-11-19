package com.netty4.rpc.core.registry;


import com.netty4.rpc.core.registry.impl.zookeeper.ZookeeperBasedRegistry;

/**
 *
 * XoaRegistry工厂
 */
public class RegistryFactory {

    private static RegistryFactory instance = new RegistryFactory();

    public static RegistryFactory getInstance() {
        return instance;
    }

    private IRegistry registry;

    private RegistryFactory() {
        try {
            registry = new ZookeeperBasedRegistry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 默认的XoaRegistry
     */
    public IRegistry getDefaultRegistry() {
        return registry;
    }
}