package com.chanlee.crpc.v2.serviceImpl;

import com.chanlee.crpc.v2.entity.User;
import com.chanlee.crpc.v2.service.UserService;

import java.util.Random;
import java.util.UUID;

/**
 * 服务端接口实现类
 */
public class UserServiceImpl implements UserService {
    public User getUserById(int id) {
        System.out.println("客户端调用id 为 " + id + " 的用户");
        Random random = new Random();
        return User.builder()
                .id(id)
                .realName(UUID.randomUUID().toString())
                .age(random.nextInt(50))
                .build();
    }

    public Integer insertUser(User user) {
        System.out.println("插入用户成功：" + user);
        return 1;
    }
}

