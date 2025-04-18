package com.chanlee.crpc.v1.server;

import com.chanlee.crpc.v1.common.User;
import com.chanlee.crpc.v1.service.UserService;

import java.util.Random;
import java.util.UUID;

/**
 * 服务端接口实现类
 */
public class UserServiceImpl implements UserService {

    public User getUserById(int id) {
        System.out.println("客户端调用id 为 " + id + " 的用户");
        Random random = new Random();
        User user = User.builder()
                .id(id)
                .realName(UUID.randomUUID().toString())
                .age(random.nextInt(50))
                .build();
        return user;
    }

    public Integer insertUser(User user) {
        System.out.println("插入用户成功：" + user);
        return 1;
    }
}

