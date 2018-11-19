package com.kk.test.proxy;

import com.kk.test.proxy.api.AirPrintApi;
import com.kk.test.proxy.api.LocalPrintApi;
import com.kk.test.proxy.invocation.LocalPrintHandler;
import com.kk.test.proxy.invocation.PrintHandler;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * @auther zhihui.kzh
 * @create 11/9/1719:18
 */
public class JdkProxyTest {

    @Test
    public void testLocalPrint() {
        LocalPrintApi proxy = (LocalPrintApi) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[]{LocalPrintApi.class}, new LocalPrintHandler());

        System.out.println(proxy.printLocalDefault());
        System.out.println(proxy.printLocal("kk"));

        System.out.println(proxy.toString()); // 如果在handler中没有 处理 toString方法，则返回null。
    }

    // jdk动态代理，可以支持多个接口
    @Test
    public void testPrint() {
        {
            LocalPrintApi proxy = (LocalPrintApi) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class<?>[]{LocalPrintApi.class, AirPrintApi.class}, new PrintHandler());

            System.out.println(proxy.printLocalDefault());
            System.out.println(proxy.printLocal("kk"));
        }
        {
            AirPrintApi proxy = (AirPrintApi) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class<?>[]{LocalPrintApi.class, AirPrintApi.class}, new PrintHandler());

            System.out.println(proxy.printAirDefault());
            System.out.println(proxy.printAir("kk"));
        }
    }
}
