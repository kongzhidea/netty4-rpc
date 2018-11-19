package com.netty4.rpc.core.registry;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Node {
    private String host;
    private int port;

    public Node() {
    }

    public Node(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        Node otherNode = (Node) o;
        if (StringUtils.equals(host, otherNode.getHost())
                && port == otherNode.getPort()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (host + ":" + port).hashCode();
    }

    // 不要使用此方法，如果需要转到字符串，则使用：getIdentity方法
    public String toString() {
        return host + ":" + port;
    }

    public String getIdentity() {
        return host + ":" + port;
    }

    public static Node getNodeFromIdentity(String identity) {
        try {
            String[] conts = StringUtils.split(identity, ":");
            String host = conts[0];
            String port = conts[1];
            Node node = new Node(host, Integer.valueOf(port));
            return node;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Node> getNodeListFromIdentity(String identities) {
        String[] conts = StringUtils.split(identities, ";");
        return getNodeListFromIdentity(Arrays.asList(conts));
    }

    public static List<Node> getNodeListFromIdentity(List<String> identities) {
        List<Node> nodes = new ArrayList<Node>();
        for (String identify : identities) {
            Node node = getNodeFromIdentity(identify);
            if (node != null) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    public static boolean inNodes(List<String> nodeList, String identity) {
        if (identity == null || "".equals(identity)) {
            return false;
        }
        for (String n : nodeList) {
            if (n.equals(identity)) {
                return true;
            }
        }
        return false;
    }
}