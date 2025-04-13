# cRPC
记录我从零学习手写 RPC 的过程
# 参考文档
https://github.com/he2121/MyRPCFromZero

# Version-0
## 0、写项目第一步，先添加远程仓库
先在 github 上新建仓库，然后将本地新建的项目推送到远程仓库中

由于网上很多教程，所以本节不再赘述（可以让 chatGPT给出一个完美的解决方案）

## 1、前置知识
### 1. Java 的Socket编程 （用于建立网络连接）
   > 服务端需要启动监听过后，客户端才能够向服务端发起有效的连接
   
`ServerSocket serverSocket = new ServerSocket(SERVER_PORT)` 服务端监听指定端口
   
`Socket socket = new Socket(SERVER_IP, SERVER_PORT)` 客户端向服务端发起连接（需要指定服务端 IP、端口号）
   
`Socket clientSocket = serverSocket.accept();`服务端等待并接收来自客户端的连接请求

`socket.close()`关闭连接
### 2.字节流类

`InputStream 和 OutputStream`

   字节流以字节为单位进行读写，适用于处理二进制数据，比如图片、音频、视频甚至网络传输中的原始数据。
   常用方法：
- `read()`：从输入流读取单个字节或字节数组；
- `write()`：向输出流写入字节或字节数组；
- `flush()`：刷新输出缓冲区。

### 3. 字符流类

`（Reader/Writer、InputStreamReader/OutputStreamWriter）`

   以字符为单位读写，适合处理文本数据，比如请求参数、文本消息等，会自动处理字符编码问题。
   常用方法：
- `read()`、`readLine()`：读取字符或字符串；
- `write()`：写字符数据到流中。
### 4. 缓冲流类
   在字节流和字符流之上使用缓冲流，可以提高输入输出效率
- `BufferedReader`：常用于包装 `InputStreamReader`，可以使用`readLine()`方法一行行读取文本数据。
- `BufferedWriter/PrintWriter`：常用于包装`OutputStreamWriter`，可以使用`println()`输出文本，并自动刷新缓冲区。
### 5. 对象流类
   允许直接发送和接收 Java 对象。但前提是对象需要实现 Serializable 接口。

   `ObjectOutputStream` 与 `ObjectInputStream`：
- writeObject(Object obj)：序列化并发送对象
- readObject()：从流中反序列化对象；
- close(): 关闭流

## 2、一个最基础版本的 RPC设计思路
  我们知道，RPC 最核心且最基础的功能是：客户端调用服务端的方法，服务端执行该方法并将返回结果发送给客户端。
  
**假设现在有这样一个背景：**
1. 有一个 User 对象（客户端和服务端共有的）
2. UserService接口类中有一个方法：getUserById(Integer id)
3. 服务端中的UserServiceImpl类实现了该接口
   
**假设有这样一个RPC过程：**
1. 客户端向服务端发起连接请求
2. 服务端接收来自客户端的连接请求
3. 客户端将 用户id 传输给服务端，服务端调用该接口实现类，将结果返回给客户端
4. 客户端接收到服务端的响应后，打印结果
## 3、代码实现
> 服务端
  
server/Server
```java
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
```

server/UserServiceImpl
```java
package com.chanlee.crpc.v0.server;

import com.chanlee.crpc.v0.common.User;
import com.chanlee.crpc.v0.service.UserService;

import java.util.Random;
import java.util.UUID;

/**
 * 服务端接口实现类
 */
public class UserServiceImpl implements UserService {

    public User getUserById(int id) {
        System.out.println("客户端调用id 为 " + id + " 的用户");
        Random random = new Random();
        User user = User.builder()
                .id(id)
                .realName(UUID.randomUUID().toString())
                .age(random.nextInt(50))
                .build();
        return user;
    }
}
```
> 客户端

client/Client
```java
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

```
> service 层（这一层存放了接口类）

service/UserService
```java
package com.chanlee.crpc.v0.service;

import com.chanlee.crpc.v0.common.User;

/**
 * 服务端接口
 */
public interface UserService {
    public User getUserById(int id);
}

```
## 4、反思&总结
      
后面的打算就是按照参考文档中的内容来自己动手做，在做的过程中再去补充学习那些不会的内容

在做version-0的时候发现自己对 java 中 IO 流不是很熟悉，花了一点时间才理清楚该如何传输java对象。

关于 java 的socket 编程的相关 API 我也没有学习过，不过之前学习过 C 语言的 socket 编程，所以理解起来并不困难。

本章中的前置知识部分完全基于 ChatGPT O3-mini 生成
