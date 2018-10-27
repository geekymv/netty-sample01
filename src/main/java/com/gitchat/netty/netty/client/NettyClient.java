package com.gitchat.netty.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    // 客户端要连接的服务端主机名或ip
    private final String host;

    // 客户端要连接的服务端端口号
    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    /**
     * main方法
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        NettyClient client = new NettyClient("127.0.0.1", 6789);
        client.connect();
    }


    public void connect() throws Exception {
        // 创建NioEventLoopGroup 线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建辅助启动类Bootstrap 实例
            Bootstrap b = new Bootstrap();
            b.group(group)
                    // 指定客户端的IO模型为NIO
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });

            // 连接到服务器，阻塞等待直到连接完成
            ChannelFuture future = b.connect(host, port).sync();

            // 阻塞，直到Channel 关闭
            future.channel().closeFuture().sync();

        }finally {
            // 优雅退出，释放线程组资源
            group.shutdownGracefully().sync();
        }
    }

}
