package com.kk.test.proxy.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @auther zhihui.kzh
 * @create 11/9/1719:22
 */
public class PrintHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("printLocalDefault".equals(method.getName())) {
            return "printLocalDefault";
        }

        if ("printLocal".equals(method.getName())) {
            return "local:" + args[0];
        }
        if ("printAirDefault".equals(method.getName())) {
            return "printAirDefault";
        }

        if ("printAir".equals(method.getName())) {
            return "aire:" + args[0];
        }
        return null;
    }
}
