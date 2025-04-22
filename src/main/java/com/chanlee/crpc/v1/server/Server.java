package com.chanlee.crpc.v1.server;

import com.chanlee.crpc.v1.common.RpcRequestDTO;
import com.chanlee.crpc.v1.common.RpcResponseDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 服务端代码
 */
public class Server {
    private static final int SERVER_PORT = 8005;

        public static void main(String[] args) {

            UserServiceImpl userService = new UserServiceImpl();

            try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
                System.out.println("服务器已启动...");
                while(true){
                    Socket socket = serverSocket.accept();
                    //接收到连接请求后，启动一个新线程去处理任务
                    new Thread(() -> {
                        try {
                            // 获取输入、输出流
                            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
                            //读取接收到的请求
                            RpcRequestDTO request = (RpcRequestDTO) objectInput.readObject();
                            //利用反射调用对应方法
                            Method method = userService.getClass().getMethod(request.getMethod(), request.getParamsTypes());
                            Object invoke = method.invoke(userService, request.getParams());
                            //将调用结果进行封装
                            objectOutput.writeObject(RpcResponseDTO.success(invoke));
                            //及时传递消息
                            objectOutput.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (IOException e) {
                System.out.println("服务器启动失败...");
            }
        }
}
