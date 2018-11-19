package com.netty4.rpc.demo.client;

import com.alibaba.fastjson.JSON;
import com.netty4.rpc.demo.api.OrderService;
import com.netty4.rpc.demo.api.UserService;
import com.netty4.rpc.demo.model.User;
import com.netty4.rpc.demo.model.param.UserParam;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @auther zhihui.kzh
 * @create 18/9/1714:10
 */
public class ServiceSpringZKTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected UserService userService;
    protected OrderService orderService;

    // com.netty4.rpc.common.registry.impl.AbstracRegistry.getHostConfigFromSysProp()
    @Before
    public void init() {
        String conf = "consumer/consumer.xml";

        ApplicationContext context = new ClassPathXmlApplicationContext(conf);

        userService = context.getBean(UserService.class);
        orderService = context.getBean(OrderService.class);
    }

    @Test
    public void testGetUser() throws Exception {

        for (int i = 0; i < 100000; i++) {
            try {

                User user = userService.getUserById(i);
                logger.info(JSON.toJSONString(user));


            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            Thread.sleep(100);
        }
    }

}
