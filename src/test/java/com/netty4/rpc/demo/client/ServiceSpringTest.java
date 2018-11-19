package com.netty4.rpc.demo.client;

import com.alibaba.fastjson.JSON;
import com.netty4.rpc.client.factory.ServiceFactory;
import com.netty4.rpc.core.util.NamedThreadFactory;
import com.netty4.rpc.demo.api.OrderService;
import com.netty4.rpc.demo.api.UserService;
import com.netty4.rpc.demo.model.User;
import com.netty4.rpc.demo.model.param.UserParam;
import com.netty4.rpc.demo.server.bootstrap.ServerStart;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @auther zhihui.kzh
 * @create 18/9/1714:10
 */
public class ServiceSpringTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected UserService userService;
    protected OrderService orderService;

    // com.netty4.rpc.common.registry.impl.AbstracRegistry.getHostConfigFromSysProp()
    @Before
    public void init() {
        System.setProperty("netty4.hosts.com.netty4.rpc.demo.api.UserService", "localhost:9301");
        System.setProperty("netty4.hosts.com.netty4.rpc.demo.api.OrderService", "localhost:9301");

        String conf = "consumer/consumer.xml";

        ApplicationContext context = new ClassPathXmlApplicationContext(conf);

        userService = context.getBean(UserService.class);
        orderService = context.getBean(OrderService.class);
    }

    @Test
    public void testGetUser() throws Exception {

        for (int i = 0; i < 100; i++) {
            try {

                User user = userService.getUserById(i);
                logger.info(JSON.toJSONString(user));


            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            Thread.sleep(1000);
        }
    }

    @Test
    public void testGetUserMulty() throws Exception {

        int threadNum = 50;
        for (int c = 0; c < threadNum; c++) {
            final int ci = c;
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 1000000000; i++) {
                            try {

                                User user = userService.getUserById(i);
                                logger.info("c=" + ci + ",user=" + JSON.toJSONString(user));


                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }

                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }

                }
            };
            new Thread(run).start();
        }
        while (true) {

        }

    }

    @Test
    public void testGetUserList() throws Exception {

        UserParam param = new UserParam();
        param.setLimit(1000);
        List<User> list = userService.getUserList(param);
        logger.info("" + list.size());
    }
}
