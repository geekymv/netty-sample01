package com.gitchat.netty.protobuf.client;

import com.gitchat.netty.protobuf.ChatInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatClientHandler extends SimpleChannelInboundHandler<ChatInfo.Chat> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for(int i = 0; i < 200; i++) {
            ChatInfo.Chat chat = ChatInfo.Chat.newBuilder()
                            .setMsg("hello-" + i)
                            .build();
            ctx.writeAndFlush(chat);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatInfo.Chat chat) throws Exception {
        System.out.println("服务端发送过来的消息内容 = " + chat.getMsg());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
