package com.gitchat.netty.pack.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.string.LineSeparator;

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for(int i = 0; i < 1000; i++) {
            ctx.writeAndFlush("hello--" + i + LineSeparator.DEFAULT.value());
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 客户端打印服务端发送的数据
        System.out.println("response = " + msg);
    }
}
