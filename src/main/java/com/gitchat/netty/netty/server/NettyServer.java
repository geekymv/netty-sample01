package com.gitchat.netty.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    // 服务端监听的端口
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    /**
     * main方法
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        NettyServer server = new NettyServer(6789);
        server.start();
    }


    private void start() throws Exception {
        // 创建两个线程组，其中bossGroup 用于监听端口，接受新的连接。
        //  workerGroup 用于处理传入的客户端连接的数据读写。
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建引导类ServerBootstrap，用于引导服务器的启动
            ServerBootstrap b = new ServerBootstrap();
            // 配置线程组，指定线程模型
            b.group(bossGroup, workerGroup)
                    // 指定服务端的IO模型为NIO
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 我们自定义用于处理业务的Handler添加到pipline上
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            // 将服务器绑定到指定的端口以监听新的连接请求。同步等待绑定完成。
            ChannelFuture future = b.bind(port).sync();
            System.out.println("NettyServer 已启动，监听端口：" + port);

            //  阻塞，直到Channel 关闭
            future.channel().closeFuture().sync();

        }finally {
            // 优雅退出，释放线程组资源
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }


}
