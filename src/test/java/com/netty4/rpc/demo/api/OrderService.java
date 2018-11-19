package com.netty4.rpc.demo.api;

import com.netty4.rpc.demo.model.Order;
import com.netty4.rpc.demo.model.param.OrderParam;

import java.util.List;

public interface OrderService {

    Order getOrderById(int id);

    List<Order> getOrderList(OrderParam param);
}
