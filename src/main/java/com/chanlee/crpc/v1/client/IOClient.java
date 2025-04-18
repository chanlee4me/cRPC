package com.chanlee.crpc.v1.client;

import com.chanlee.crpc.v1.common.RpcRequest;
import com.chanlee.crpc.v1.common.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
@Slf4j
public class IOClient implements Serializable {
    public static <T> RpcResponse<T> sendRequest(String host, int port, RpcRequest request){
        //和服务器建立连接
        try {
            Socket socket = new Socket(host, port);
            // 获取输入流和输出流
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
            //发送请求
            objectOutput.writeObject(request);
            objectOutput.flush();
            //接收结果
            RpcResponse<T> response = (RpcResponse<T>) objectInput.readObject();
            //关闭连接
            socket.close();
            //返回结果
            return response;
        } catch (IOException e) {
            log.error("和服务器建立连接失败: {}", e);
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            log.error("接收结果失败: {}", e);
            throw new RuntimeException(e);
        }
    }
}
