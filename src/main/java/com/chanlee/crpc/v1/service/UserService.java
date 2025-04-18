package com.chanlee.crpc.v1.service;

import com.chanlee.crpc.v1.common.User;

/**
 * 服务端接口
 */
public interface UserService {

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    User getUserById(int id);

    /**
     * 插入用户信息
     * @param user
     * @return
     */
    Integer insertUser(User user);
}
