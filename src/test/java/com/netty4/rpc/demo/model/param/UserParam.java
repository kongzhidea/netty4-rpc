package com.netty4.rpc.demo.model.param;

import java.io.Serializable;

/**
 * @auther zhihui.kzh
 * @create 12/9/1720:00
 */
public class UserParam implements Serializable {
    private Integer id;
    private String name;
    private Integer limit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
