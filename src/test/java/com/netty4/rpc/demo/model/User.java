package com.netty4.rpc.demo.model;

import java.io.Serializable;

/**
 * @auther zhihui.kzh
 * @create 5/9/1718:22
 */
public class User implements Serializable {

    private static final long serialVersionUID = -6233632705525964348L;

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
