package com.netty4.rpc.demo.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther zhihui.kzh
 * @create 5/9/1718:22
 */
public class Order implements Serializable {

    private static final long serialVersionUID = -6233632705525964348L;

    private int id;
    private String phone;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
