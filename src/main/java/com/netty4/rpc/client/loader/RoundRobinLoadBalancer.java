package com.netty4.rpc.client.loader;

import com.netty4.rpc.core.registry.IRegistry;
import com.netty4.rpc.core.registry.Node;
import com.netty4.rpc.core.registry.RegistryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhihui.kzh
 * @create 15/11/201812:01
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, AtomicInteger> counterMap = new ConcurrentHashMap<String, AtomicInteger>();

    private IRegistry registry = RegistryFactory.getInstance().getDefaultRegistry();

    public Node getNode(String className) {
        List<Node> nodes = registry.queryService(className);
        return selectNode(className, nodes);
    }

    @Override
    public List<Node> getNodeList(String className) {
        List<Node> nodes = registry.queryService(className);
        return nodes;
    }

    private Node selectNode(String className, List<Node> nodes) {
        AtomicInteger counter = counterMap.get(className);
        if (counter == null) {
            synchronized (className.intern()) {
                counter = counterMap.get(className);
                if (counter == null) {
                    counter = new AtomicInteger();
                    counterMap.put(className, counter);
                }
            }
        }

        int count = counter.incrementAndGet();
        if (count > Integer.MAX_VALUE - 100000) {   //防上溢，打出点提前量
            //归零
            counter.set(0);
        }
        Node node = nodes.get(count % nodes.size());

        return node;
    }


}
