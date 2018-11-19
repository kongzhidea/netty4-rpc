package com.netty4.rpc.client.transport;

import com.netty4.rpc.core.registry.Node;
import com.netty4.rpc.core.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionProvider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, RpcClient> clientMap = new ConcurrentHashMap<String, RpcClient>();

    // 断线重连使用
    private ScheduledExecutorService heartBeatTimerService = Executors
            .newScheduledThreadPool(1, new NamedThreadFactory("netty-heartTimerService"));

    public ConnectionProvider() {
        initHeartBeatTimer();
    }

    private void initHeartBeatTimer() {
        heartBeatTimerService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Set<String> addressList = clientMap.keySet();
                            for (String address : addressList) {
                                RpcClient client = clientMap.get(address);
                                if (client != null && client.isActive()) {
                                    continue;
                                }
                                logger.warn("heart beat check server is not active, reconnecting:" + address);
                                client.reConnect();
                            }

                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }

                    }
                },
                0,
                10,
                TimeUnit.SECONDS);
    }

    private void connectServerNode(Node node) {
        RpcClient client = new RpcClient();

        try {
            client.connect(node);
            clientMap.put(node.getIdentity(), client);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public RpcClient getClient(Node node) {
        String identify = node.getIdentity();
        if (!clientMap.containsKey(identify)) {
            synchronized (this) {
                if (!clientMap.containsKey(identify)) {
                    connectServerNode(node);
                }
            }
        }
        RpcClient client = clientMap.get(identify);
        return client;
    }
}
