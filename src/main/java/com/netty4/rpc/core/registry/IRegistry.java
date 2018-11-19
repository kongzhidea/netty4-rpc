package com.netty4.rpc.core.registry;

import java.util.List;


/**
 * RegistryFactory  单例
 */
public interface IRegistry {

    public void registryServer(String className, Node node) throws Exception;

    public void unRegistryServer(String className, Node node) throws Exception;

    public List<Node> queryService(String className);

}