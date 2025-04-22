# fcRPC
记录我从零学习手写 RPC 的过程
# 参考文档
https://github.com/he2121/MyRPCFromZero

# Version-0
## 一、写项目第一步，先添加远程仓库
先在 github 上新建仓库，然后将本地新建的项目推送到远程仓库中

由于网上很多教程，所以本节不再赘述（可以让 chatGPT给出一个完美的解决方案）

## 二、前置知识
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
- `writeObject(Object obj)`：序列化并发送对象
- `readObject()`：从流中反序列化对象；
- `close()` : 关闭流

## 三、一个最基础版本的 RPC设计思路
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
## 四、代码实现
### 服务端

#### server/Server

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

#### server/UserServiceImpl

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


### 客户端

#### client/Client

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


### service 层（这一层存放了接口类）

#### service/UserService

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
## 五、反思&总结

后面的打算就是按照参考文档中的内容来自己动手做，在做的过程中再去补充学习那些不会的内容

在做version-0的时候发现自己对 java 中 IO 流不是很熟悉，花了一点时间才理清楚该如何传输java对象。

关于 java 的socket 编程的相关 API 我也没有学习过，不过之前学习过 C 语言的 socket 编程，所以理解起来并不困难。

本章中的前置知识部分完全基于 ChatGPT O3-mini 生成



# Version-1

## 一、 前置知识

### 1. 反射

**获取字节码的三种方式**

- `Class.forName("全类名")` （全类名，即包名+类名）
- `类名.class`
- `对象.getClass()` (任意对象都可调用，因为该方法来自`Object`类）

**获取成员方法**

`Method getMethod(String name,Class<?>...parameterTypes)`

参数为：方法名，参数类型<可变>

**执行成员方法**

`Object invoke(Object obj, Object ... args)`

参数 1：哪个对象来调用该方法

参数 2 ：传入的实参

### 2. 动态代理

> 具体代码实现参考下方的"代码实现"部分

动态代理实现流程：

1. 创建一个类来实现`InvocationHandler`接口（重写`invoke`方法）
2. 调用`Proxy.newProxyInstance()`来创建代理类（需要将第一步创建的类作为参数传进来）
3. 通过代理类来调用方法

## 二、上一个版本中的问题&解决思路

**问题 1：**服务端当前仅支持一个服务，当提供多个服务时，客户端如何指定要调用哪个服务？（当前客户端发送请求数据时，而只会傻乎乎地发送数据，而无法指定具体调用哪个接口）

> 当服务端提供多个服务时，客户端需要指定调用的服务名称。

创建一个请求对象类`Request`，其中包含的成员属性有：接口名称、要调用的方法名称、传递的参数数据，服务端利用这些信息，来使用反射调用相应的服务



**问题 2：**当前服务端中的方法返回类型是固定的，但是当有不同的方法时，返回数据类型可能不同，如果客户端要处理这些不同类型的数据，就需要提前知道服务端返回的数据类型。但这显然会违背解耦的原则，降低灵活性，而且不便于后续维护和扩展

> 引入统一的响应格式——将返回数据封装到一个公共类型中

创建一个响应对象类`Response`，其中包含的成员属性有：状态码、状态描述、响应数据（这种思想在 javaweb 开发也经常使用）



**问题 3：**在上个版本中，客户端和目标主机建立连接时，采用了硬编码，不够优雅

**问题 4：**如果仍然采用上个版本的客户端代码，那么代码耦合性较高（建立连接、发送请求的代码、接收响应、处理响应结果都写在了一起）

针对问题 3、4，将这些问题抽象了出来，建立一个 IOClient 类，专门建立连接、发送请求、接收响应。

## 三、设计思路

1. 将请求、响应的数据各自封装到一个公共类中，这样在请求和响应时就能进行统一，便于后续代码的书写和维护
2. 服务端采用循环+BIO的形式，当接收到请求对象时，利用反射调用对应方法，并将执行结果发送给客户端
3. 将建立连接、发送数据、接收数据的过程封装到专用组件中（会将请求参数封装到一个请求对象中（包含方法、参数类型、参数列表等））
4. 利用动态代理，拦截所有对接口方法的调用，将其转换为 RPC 请求



## 四、代码实现

### 服务端

#### server/Server

```java
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
```

#### server/UserServiceImpl

```java
package com.chanlee.crpc.v1.server;

import com.chanlee.crpc.v1.common.User;
import com.chanlee.crpc.v1.service.UserService;

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

    public Integer insertUser(User user) {
        System.out.println("插入用户成功：" + user);
        return 1;
    }
}
```

###  客户端

#### client/Client

```java
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
```

#### client/Proxy

```java
package com.chanlee.crpc.v1.client;

import com.chanlee.crpc.v1.common.RpcRequestDTO;
import com.chanlee.crpc.v1.common.RpcResponseDTO;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static com.chanlee.crpc.v1.client.IOClient.sendRequest;

/**
 * 客户端代理类
 */
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    /**
     * 服务端 IP
     */
    private String host;

    /**
     * 服务端端口号
     */
    private int port;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request请求
        RpcRequestDTO request = RpcRequestDTO.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .method(method.getName())
                .paramsTypes(method.getParameterTypes())
                .params(args)
                .build();
        //发送请求并获取响应
        RpcResponseDTO<Object> response = sendRequest(host, port, request);
        //返回结果数据
        return response.getData();
    }
    public <T> T getProxy(Class<T> tClass){
        Object o = Proxy.newProxyInstance(
                tClass.getClassLoader(),
                new Class[]{tClass},
                this
        );
        return (T)o;
    }
}
```

#### client/IOClient

```java
package com.chanlee.crpc.v1.client;

import com.chanlee.crpc.v1.common.RpcRequestDTO;
import com.chanlee.crpc.v1.common.RpcResponseDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * 客户端 IO 组件
 */
@Slf4j
public class IOClient implements Serializable {
    public static <T> RpcResponseDTO<T> sendRequest(String host, int port, RpcRequestDTO request){
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
            RpcResponseDTO<T> response = (RpcResponseDTO<T>) objectInput.readObject();
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
```



### 服务层

#### service/UserService

```java
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
```



### 公共类

#### convention/BaseErrorCode

```java
package com.chanlee.crpc.v1.common.convention;

/**
 * 基础错误码
 */
public enum BaseErrorCode implements ErrorCode {
    SERVER_ERROR("A000001", "服务端内部错误");

    private final String code;

    private final String message;

    BaseErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
```

#### convention/ErrorCode

```java
package com.chanlee.crpc.v1.common.convention;

/**
 * 错误码接口
 */
public interface ErrorCode {
    /**
     * 错误码
     */
    String code();

    /**
     * 错误信息
     */
    String message();
}
```

#### commom/RpcRequestDTO

```java
package com.chanlee.crpc.v1.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 请求对象体
 */
@Builder
@Data
public class RpcRequestDTO implements Serializable {
    /**
     * 接口名
     */
    private String interfaceName;

    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    private Object[] params;

    /**
     * 参数类型
     */
    private Class<?>[] paramsTypes;
}
```

#### Common/RpcRespDTO

```java
package com.chanlee.crpc.v1.common;

import com.chanlee.crpc.v1.common.convention.BaseErrorCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应对象体
 */
@Data
@Accessors(chain = true)
public class RpcResponseDTO<T> implements Serializable {
    /**
     * 正确返回码
     */
    public static final String SUCCESS_CODE = "200";

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public static RpcResponseDTO<Void> success(){
        return new RpcResponseDTO<Void>()
                .setCode(SUCCESS_CODE);
    }

    public static <T> RpcResponseDTO<T> success(T data){
        return new RpcResponseDTO<T>()
                .setCode(SUCCESS_CODE)
                .setData(data);
    }

    public static RpcResponseDTO<Void> failure(){
        return new RpcResponseDTO<Void>()
                .setMessage(BaseErrorCode.SERVER_ERROR.code())
                .setCode(BaseErrorCode.SERVER_ERROR.message());
    }
}
```

#### common/User

```java
package com.chanlee.crpc.v1.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * 用户类
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    /**
     * 用户id
     */
    Integer id;

    /**
     * 用户真实姓名
     */
    String realName;

    /**
     * 用户年龄
     */
    Integer age;
}
```

## 五、本版本完成的任务

- [x]  将请求和响应进行封装
- [x]  服务端提供多个服务
- [x]  对客户端的一些任务进行了封装（建立连接&发出请求&接收响应）
- [x] 利用动态代理加强了 IO 过程（封装了客户端发送的请求数据）



# 目前仍然存在的问题

在处理异常时很粗糙——后续要为每种异常场景来抛出不同的错误码
