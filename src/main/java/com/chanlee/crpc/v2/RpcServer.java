package com.chanlee.crpc.v2;

import com.chanlee.crpc.v2.common.config.ServerConfig;
import com.chanlee.crpc.v2.dto.RpcRequestDTO;
import com.chanlee.crpc.v2.dto.RpcResponseDTO;
import com.chanlee.crpc.v2.handler.RequestHandler;
import com.chanlee.crpc.v2.registry.ServiceRegistry;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RPC服务器
 */
@Data
public class RpcServer {
    private final ServiceRegistry serviceRegistry;
    private final RequestHandler requestHandler;
    private final ServerConfig serverConfig;
    private final ThreadPoolExecutor threadPool;

    public RpcServer(ServiceRegistry serviceRegistry, RequestHandler requestHandler, ServerConfig serverConfig) {
        this.serviceRegistry = serviceRegistry;
        this.requestHandler = requestHandler;
        this.serverConfig = serverConfig;
        this.threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100));
    }

    /**
     * 启动RPC服务器
     */
    public void start(){
        System.out.println("服务器已启动...");
        try (ServerSocket serverSocket = new ServerSocket(serverConfig.getPort())){
            while(true){
                Socket socket = serverSocket.accept();
                threadPool.execute(() -> handleRequest(socket));
            }
        } catch (IOException e) {
            System.out.println("服务器启动失败：" + e.getMessage());
        }
    }
    /**
     * 处理客户端请求
     */
    private void handleRequest(Socket socket){
        try(
            // 获取输入、输出流
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            //打印线程信息
            System.out.println("线程：" + Thread.currentThread().getId() + " 接收到请求...");
            //读取接收到的请求
            RpcRequestDTO request = (RpcRequestDTO) in.readObject();

            //处理请求
            RpcResponseDTO response = requestHandler.handle(request);

            System.out.println("线程：" + Thread.currentThread().getId() + "待发送响应：" + response);
            //发送响应
            out.writeObject(response);
            out.flush();
            System.out.println("线程：" + Thread.currentThread().getId() + " 响应已发送...");
        } catch (Exception e) {
            System.err.println("处理请求失败: " + e.getMessage());
        }
    }
}
