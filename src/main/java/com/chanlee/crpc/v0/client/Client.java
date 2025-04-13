package com.chanlee.crpc.v0.client;
import com.chanlee.crpc.v0.common.User;

import java.io.*;
import java.net.Socket;

/**
 * 客户端代码
 */
public class Client{
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8003;

    public static void main(String[] args){
        try(Socket socket = new Socket(SERVER_IP, SERVER_PORT)){
            System.out.println("客户端已连接服务器...");
            // 获取输入流和输出流
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
            // 向服务端发送用户ID
            objectOutput.writeObject(1);
            // 接收来自服务端的响应
            User response = (User) objectInput.readObject();
            System.out.println("服务器响应：" + response);
            objectOutput.close();
            objectInput.close();
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
