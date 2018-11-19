package com.netty4.rpc.demo.server.service;

import com.netty4.rpc.demo.api.OrderService;
import com.netty4.rpc.demo.model.Order;
import com.netty4.rpc.demo.model.param.OrderParam;
import com.netty4.rpc.server.annotation.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @auther zhihui.kzh
 * @create 12/9/1720:25
 */
@Service("orderService")
@RpcService(OrderService.class)
public class OrderServiceImpl implements OrderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Order getOrderById(int id) {
        Order order = new Order();
        order.setId(id);
        order.setPhone("phone:" + id);
        order.setCreateTime(new Date());

        return order;
    }

    @Override
    public List<Order> getOrderList(OrderParam param) {
        if (param.getId() != null) {
            return Arrays.asList(getOrderById(param.getId()));
        }

        int limit = param.getLimit() == null ? 20 : param.getLimit();

        List<Order> list = new ArrayList<Order>();
        for (int i = 0; i < limit; i++) {
            list.add(getOrderById(i));
        }
        return list;
    }
}
