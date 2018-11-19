package com.netty4.rpc.demo.model.param;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther zhihui.kzh
 * @create 12/9/1720:00
 */
public class OrderParam implements Serializable {
    private Integer id;
    private String phone;
    private Date createTime;

    private Integer limit;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
