package com.chanlee.crpc.v2.client;

import com.chanlee.crpc.v2.entity.Blog;
import com.chanlee.crpc.v2.entity.User;
import com.chanlee.crpc.v2.service.BlogService;
import com.chanlee.crpc.v2.service.UserService;

/**
 * 客户端主代码
 */
public class Client{

    public static void main(String[] args) throws InterruptedException {

        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 8005);
        UserService proxy = clientProxy.getProxy(UserService.class);
        //调用方法 1
        User user = proxy.getUserById(1);
        System.out.println("对应的user为：" + user);

        //调用方法 2
        User codingBoy = User.builder()
                .age(25)
                .id(32)
                .realName("coding boy")
                .build();
        Integer i = proxy.insertUser(codingBoy);
        System.out.println("向服务端插入的 user的Id为：" + i);

        //调用服务 BlogService
        BlogService blogService = clientProxy.getProxy(BlogService.class);
        Blog blogById = blogService.getBlogById(1);
        System.out.println("从服务端得到的blog为：" + blogById);
    }

}
