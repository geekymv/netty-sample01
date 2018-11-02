package com.gitchat.netty.chat.server.handler;

import com.gitchat.netty.chat.ChatInfo;
import com.gitchat.netty.chat.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AuthHandler extends SimpleChannelInboundHandler<ChatInfo.Chat> {


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SessionUtil.removeChannel(channel);
        System.out.println(SessionUtil.getUserId(channel) + " 下线了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatInfo.Chat msg) throws Exception {
        if(!isLogin(ctx.channel())) {
            System.out.println(ctx.channel() + "没有登录");
            ctx.close();

        }else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 判断channel 是否已经通过登录认证
     * @param channel
     * @return
     */
    private boolean isLogin(Channel channel) {
        return SessionUtil.isLogin(channel);
    }
}
