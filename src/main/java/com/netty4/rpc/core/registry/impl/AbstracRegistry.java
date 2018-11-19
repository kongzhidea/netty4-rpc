package com.netty4.rpc.core.registry.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.netty4.rpc.core.registry.IRegistry;
import com.netty4.rpc.core.registry.Node;
import com.netty4.rpc.core.registry.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * XoaRegistry的一个基础实现类
 */
public abstract class AbstracRegistry implements IRegistry {

    protected Log logger = LogFactory.getLog(this.getClass());

    private Map<String, Service> serviceMap = new ConcurrentHashMap<String, Service>();

    @Override
    public List<Node> queryService(String serviceId) {
        List<Node> propNodes = getHostConfigFromSysProp(serviceId);
        if (!propNodes.isEmpty()) {
            return propNodes;
        }
        Service service = getService(serviceId);

        //not found
        if (service == null) {
            return null;
        }
        return service.getNodes();
    }

    private Service getService(String serviceId) {
        Service service = serviceMap.get(serviceId);
        if (service == null) {
            synchronized (serviceId.intern()) {
                service = serviceMap.get(serviceId);
                if (service == null) {
                    //load
                    service = loadService(serviceId);
                    //put into map
                    if (service != null) {
                        serviceMap.put(serviceId, service);
                    }
                }
            }
        }

        return service;
    }

    protected void clearService(String serviceId) {
        synchronized (serviceMap) {
            serviceMap.remove(serviceId);
        }
    }


    protected abstract Service loadService(String serviceId);

    class LocalConfig {
        public LocalConfig(List<Node> nodes, long time) {
            this.nodes = nodes;
            this.time = time;
        }

        public List<Node> nodes;
        public long time;
    }

    private Map<String, LocalConfig> hostConfigCache = new HashMap<String, LocalConfig>();

    private List<Node> getLocalConfigCache(String serviceId) {
        LocalConfig cfg = hostConfigCache.get(serviceId);
        long curTime = (new java.util.Date()).getTime();
        if (cfg != null && curTime - cfg.time < 10000) {
            return cfg.nodes;
        }
        return null;
    }

    private List<Node> getHostConfigFromSysProp(String serviceId) {
        List<Node> nodes = getLocalConfigCache(serviceId);

        if (nodes != null) {
            return nodes;
        }

        StringBuilder propName = new StringBuilder();
        propName.append("netty4.hosts.");
        propName.append(serviceId);

        String hosts = System.getProperty(propName.toString());

        nodes = new ArrayList<Node>();
        if (hosts != null) {
            String hostVec[] = hosts.split(",");
            for (String host : hostVec) {
                String[] ss = host.split(":");
                if (ss.length == 2) {
                    Node node = Node.getNodeFromIdentity(host);
                    nodes.add(node);
                }
            }
            if (nodes.size() > 0) {
                logger.debug("Using system property to locate service nodes and cached:" + hosts);
            }
            hostConfigCache.put(serviceId, new LocalConfig(nodes, (new java.util.Date()).getTime()));
        }
        return nodes;
    }

}