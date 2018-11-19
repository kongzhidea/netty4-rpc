package com.netty4.rpc.demo.server.bootstrap;

import com.netty4.rpc.demo.api.OrderService;
import com.netty4.rpc.demo.api.UserService;
import com.netty4.rpc.server.RpcServer;
import com.netty4.rpc.server.annotation.RpcService;
import com.netty4.rpc.server.config.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Map;

/**
 * @auther zhihui.kzh
 * @create 12/9/1720:14
 */
@Service
public class ServerStart implements ApplicationContextAware {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${server.port}")
    private int port;

    @Value("${server.threadNum}")
    private int threadNum;

    // false 则不注册到zk上，true则注册到zk上。
    @Value("${server.registryFlag}")
    private boolean registryFlag;

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public void doMain() throws Exception {

        RpcServer server = new RpcServer();

        Args args = new Args();
        args.port = port;
        args.workerThreadNum = threadNum;
        args.registryFlag = registryFlag;

        server.setArgs(args);

        // 通过注解注入
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);

        if (serviceBeanMap != null) {
            for (Object serviceBean : serviceBeanMap.values()) {
                Class<?> interfaceClass = serviceBean.getClass().getAnnotation(RpcService.class).value();
                server.addHandlerMap(interfaceClass, serviceBean);
            }
        }


        // 暴扣对应的接口
//        server.addHandlerMap(UserService.class, ctx.getBean(UserService.class));
//        server.addHandlerMap(OrderService.class, ctx.getBean(OrderService.class));

        // 打印启动日志
        verbose(server);

        // 启动服务
        server.export();
    }

    private void verbose(RpcServer server) {
        logger.info("copyright " + Calendar.getInstance().get(Calendar.YEAR));
        logger.info("-------------------------");
        Map<String, Object> handlerMap = server.getHandlerMap();
        if (handlerMap != null) {
            for (String interfaceName : handlerMap.keySet()) {
                logger.info("register interfase:" + interfaceName);
            }
        }
        logger.info("server started on port " + port + " with " + threadNum + " handlers");
    }


}
