package com.chanlee.crpc.v1.client;

import com.chanlee.crpc.v1.common.User;
import com.chanlee.crpc.v1.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 客户端主代码
 */
public class Client{

    public static void main(String[] args){
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
    }

}
