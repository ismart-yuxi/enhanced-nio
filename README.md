# Enhanced NIO Framework

## 介绍

Enhanced NIO Framework 是一个基于 Java NIO
的网络通信框架，旨在提供一个灵活且高效的解决方案，用于构建同时支持客户端和服务端的应用程序。该框架实现了私有协议消息的编码和解码，支持插件机制以扩展功能，并提供性能监控和心跳检测等功能。

## 主要特性

- **双向通信**：框架支持同时作为客户端和服务端，能够处理双向数据传输。
- **私有协议**：实现了自定义的私有协议消息格式，支持消息的编码和解码。
- **插件机制**：通过插件链的方式，可以方便地扩展框架的功能，例如添加心跳检测、性能监控等。
- **动态缓冲区管理**：使用动态缓冲区管理策略，优化内存使用，避免频繁的内存分配。
- **配置化**：通过配置文件管理端口号和缓冲区大小，便于灵活调整。

## 技术栈

- **Java NIO**：使用 Java NIO 提供的非阻塞 I/O 功能，实现高效的网络通信。
- **Java Concurrency**：使用 Java 的并发工具（如 ExecutorService）来处理多线程任务。
- **自定义协议**：实现了自定义的消息协议，支持消息的序列化和反序列化。
- **简单日志记录**：使用 `System.out.println` 进行简单的日志记录，便于调试和监控。

## 使用方法

### 1. 配置文件

在项目根目录下创建一个名为 `config.properties` 的文件，内容如下：

```properties
server.port=8080
buffer.size=1024
```

### 2. 启动服务端

在主类 EnhancedNIOFramework 中，调用 startServer 方法启动服务端：

``` java
    Bootstrap serverBootstrap = new Bootstrap.Builder()
            .addHandler(new ReadHandler<>(new PrivateProtocolDecoder()))
            .addHandler(new WriteHandler<>(new PrivateProtocolEncoder()))
            .addHandler(new ChannelHandler() {
                // 实现通道事件处理
            })
            .enableSessionManagement()
            .addPlugin(new HeartbeatPlugin())
            .addPlugin(new PerformanceMonitorPlugin())
            .build();
    
    serverBootstrap.startServer(port); // 启动服务端
```

### 3. 启动客户端

在同一主类中，调用 startClient 方法启动客户端并连接到服务端：

``` java
    Bootstrap clientBootstrap = new Bootstrap.Builder()
            .addHandler(new ReadHandler<>(new PrivateProtocolDecoder()))
            .addHandler(new WriteHandler<>(new PrivateProtocolEncoder()))
            .addHandler(new ChannelHandler() {
                // 实现通道事件处理
            })
            .build();
    
    clientBootstrap.startClient("localhost", port); // 启动客户端并连接到服务器
```

### 4. 自定义消息处理

在 ChannelHandler 中实现消息的处理逻辑，例如读取消息、发送响应等。

### 5. 运行程序

确保服务端和客户端在同一台机器上运行，或根据需要调整客户端的主机地址。运行主类
EnhancedNIOFramework，服务端将开始监听指定端口，客户端将连接到服务端并发送消息。

## 示例

以下是一个简单的示例，展示如何在服务端和客户端之间发送和接收消息：

``` java
@Override
public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
    PrivateMessage message = (PrivateMessage) msg;
    System.out.println("Received message: " + new String(message.getData()));
    // 处理消息并发送响应
}
```

