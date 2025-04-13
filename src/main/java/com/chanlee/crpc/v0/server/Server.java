package com.chanlee.crpc.v0.server;

import com.chanlee.crpc.v0.common.User;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 服务端代码
 */
public class Server {
    private static final int SERVER_PORT = 8003;

        public static void main(String[] args) {
            try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
                System.out.println("服务器已启动，等待客户端连接...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("客户端已连接...");
                // 获取输入、输出流
                ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream());
                // 接收客户端发送的 id（注意，这里直接接收 Integer 对象）
                Integer id = (Integer) objectInput.readObject();
                // 调用方法
                UserServiceImpl userService = new UserServiceImpl();
                User user = userService.getUserById(id);
                // 向客户端发送响应
                objectOutput.writeObject(user);
                objectInput.close();
                objectOutput.close();
                clientSocket.close();
            }catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
}
