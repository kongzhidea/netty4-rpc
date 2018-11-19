package com.netty4.rpc.demo.api;

import com.netty4.rpc.demo.model.User;
import com.netty4.rpc.demo.model.param.UserParam;

import java.util.List;

public interface UserService {

    User getUserById(int id);

    List<User> getUserList(UserParam param);
}
