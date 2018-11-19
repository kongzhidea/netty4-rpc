package com.netty4.rpc.demo.server.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @auther zhihui.kzh
 * @create 12/9/1720:04
 */
public class DeamonRunner {
    private static final Logger logger = LoggerFactory.getLogger(DeamonRunner.class);

    public static void main(String[] args) {

        Thread.setDefaultUncaughtExceptionHandler( // NL
                new Thread.UncaughtExceptionHandler() {
                    public void uncaughtException(Thread t, Throwable e) {
                        logger.error(e.getMessage(), e);
                    }
                });

        String conf = "server.xml";

        try {
            ApplicationContext context = new ClassPathXmlApplicationContext(conf);

            ServerStart serverStart = context.getBean(ServerStart.class);

            serverStart.doMain();

        } catch (Throwable e) {
            logger.error("Error from Spring during initialization: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }
}
