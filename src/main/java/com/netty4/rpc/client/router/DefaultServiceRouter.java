package com.netty4.rpc.client.router;

import com.netty4.rpc.client.loader.LoadBalancer;
import com.netty4.rpc.client.loader.RoundRobinLoadBalancer;
import com.netty4.rpc.client.transport.ConnectionProvider;
import com.netty4.rpc.client.transport.RpcClient;
import com.netty4.rpc.core.registry.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author zhihui.kzh
 * @create 15/11/201821:12
 */
public class DefaultServiceRouter implements ServiceRouter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ConnectionProvider connectionProvider = new ConnectionProvider();

    private LoadBalancer loadBalancer = new RoundRobinLoadBalancer();

    @Override
    public RpcClient routeService(String className) {
        Node node = loadBalancer.getNode(className);

        RpcClient client = connectionProvider.getClient(node);

        if (client != null && client.isActive()) {
            return client;
        }

        logger.warn("server not active:" + node.getIdentity());

        List<Node> nodeList = loadBalancer.getNodeList(className);
        for (Node n : nodeList) {
            client = connectionProvider.getClient(n);
            if (client != null && client.isActive()) {
                return client;
            }
        }

        throw new RuntimeException("no active server:" + className);
    }
}
