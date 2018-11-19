package com.kk.test.proxy.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @auther zhihui.kzh
 * @create 11/9/1719:22
 */
public class LocalPrintHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("printLocalDefault".equals(method.getName())) {
            return "default";
        }

        if ("printLocal".equals(method.getName())) {
            return args[0];
        }
        return null;
    }
}
