package com.gitchat.netty.chat.server.handler;

import com.gitchat.netty.chat.ChatInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 加入群组请求处理器
 */
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<ChatInfo.Chat> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatInfo.Chat msg) throws Exception {


    }
}
