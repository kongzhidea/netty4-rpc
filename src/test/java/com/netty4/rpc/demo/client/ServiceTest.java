package com.netty4.rpc.demo.client;

import com.alibaba.fastjson.JSON;
import com.netty4.rpc.client.factory.ServiceFactory;
import com.netty4.rpc.demo.api.UserService;
import com.netty4.rpc.demo.model.User;
import com.netty4.rpc.demo.model.param.UserParam;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 *
 * @auther zhihui.kzh
 * @create 18/9/1714:10
 */
public class ServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // com.netty4.rpc.common.registry.impl.AbstracRegistry.getHostConfigFromSysProp()
    @Before
    public void init() {
        System.setProperty("netty4.hosts.com.netty4.rpc.demo.api.UserService", "localhost:9301");
        System.setProperty("netty4.hosts.com.netty4.rpc.demo.api.OrderService", "localhost:9301");
    }

    @Test
    public void testGetUser() throws Exception {

        for (int i = 0; i < 100; i++) {
            try {
                UserService userService = ServiceFactory.getService(UserService.class);

                User user = userService.getUserById(i);
                logger.info(JSON.toJSONString(user));


            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            Thread.sleep(1000);
        }
    }

    @Test
    public void testGetUserList() throws Exception {
        UserService userService = ServiceFactory.getService(UserService.class);

        UserParam param = new UserParam();
        param.setLimit(1000);
        List<User> list = userService.getUserList(param);
        logger.info("" + list.size());
    }
}
