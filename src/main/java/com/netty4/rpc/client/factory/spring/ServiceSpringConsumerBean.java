package com.netty4.rpc.client.factory.spring;

import com.netty4.rpc.client.config.Constants;
import com.netty4.rpc.client.factory.ServiceFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * @author zhihui.kzh
 * @create 16/11/201814:37
 */
public class ServiceSpringConsumerBean implements FactoryBean, InitializingBean {

    protected Class<?> interfaceName;

    protected long timeout = Constants.TIMEOUT;

    protected Object clientObject;


    @Override
    public Class<?> getObjectType() {
        return interfaceName;
    }


    public void setInterfaceName(String interfaceName) {
        try {
            this.interfaceName = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(interfaceName.isInterface(), "not a interface class: " + interfaceName.getName());
        // cacheProvider可以null，不做assert.notNull判断
    }

    @Override
    public Object getObject() {
        if (clientObject == null) {
            clientObject = getService();
            Assert.notNull(clientObject);
        }
        return clientObject;
    }

    protected Object getService() {
        try {
            return ServiceFactory.getService(interfaceName, timeout);
        } catch (RuntimeException e) {
            throw new IllegalStateException(
                    "failed to create bean for " + this.interfaceName.getName(), e);
        }
    }
}
