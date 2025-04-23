package com.chanlee.crpc.v2.service;

import com.chanlee.crpc.v2.entity.User;

/**
 * 用户服务
 */
public interface UserService {

    /**
     * 根据id获取用户信息
     */
    User getUserById(int id);

    /**
     * 插入用户信息
     */
    Integer insertUser(User user);
}
