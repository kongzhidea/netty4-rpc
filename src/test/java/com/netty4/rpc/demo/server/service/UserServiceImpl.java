package com.netty4.rpc.demo.server.service;

import com.netty4.rpc.demo.api.UserService;
import com.netty4.rpc.demo.model.User;
import com.netty4.rpc.demo.model.param.UserParam;
import com.netty4.rpc.server.annotation.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @auther zhihui.kzh
 * @create 12/9/1720:25
 */
@Service("userService")
@RpcService(UserService.class)
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User getUserById(int id) {
        User user = new User();
        user.setId(id);
        user.setName("n_" + id);

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return user;
    }

    @Override
    public List<User> getUserList(UserParam param) {
        if (param.getId() != null) {
            return Arrays.asList(getUserById(param.getId()));
        }

        int limit = param.getLimit() == null ? 20 : param.getLimit();

        List<User> list = new ArrayList<User>();
        for (int i = 0; i < limit; i++) {
            list.add(getUserById(i));
        }

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return list;
    }
}
