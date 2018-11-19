package com.netty4.rpc.client.loader;


import com.netty4.rpc.core.registry.Node;

import java.util.List;

/**
 * 用于封装负载均衡逻辑
 */
public interface LoadBalancer {

    /**
     * 给定接口，返回一个负载均衡后的节点
     *
     * @param className
     * @return
     */
    public Node getNode(String className);

    public List<Node> getNodeList(String className);
}