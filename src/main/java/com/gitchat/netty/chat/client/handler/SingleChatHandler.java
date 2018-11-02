package com.gitchat.netty.chat.client.handler;

import com.gitchat.netty.chat.ChatInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SingleChatHandler extends SimpleChannelInboundHandler<ChatInfo.Chat> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatInfo.Chat msg) throws Exception {
        ChatInfo.Chat.MessageType msgType = msg.getMsgType();
        if(msgType == ChatInfo.Chat.MessageType.SINGLE_CHAT) {
            ChatInfo.SingleChat singleChat = msg.getReq().getSingleChat();

            ChatInfo.User fromUser = singleChat.getFromUser();
            ChatInfo.User toUser = singleChat.getToUser();

            System.out.println("客户端：" + fromUser.getUserId() + " 发送给 " + toUser.getUserId() + " 的消息：" + singleChat.getMsgContent());

        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
