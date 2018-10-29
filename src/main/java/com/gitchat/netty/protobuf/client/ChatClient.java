package com.gitchat.netty.protobuf.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatClient {

    private final String host;

    private final int port;

    private Channel client;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * main方法
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient("127.0.0.1", 6789);
        client.connect();
    }

    public void connect() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientChannelInitializer());

            ChannelFuture future = b.connect(host, port).sync();
            this.client = future.channel();

            future.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
