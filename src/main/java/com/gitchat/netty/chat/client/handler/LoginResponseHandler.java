package com.gitchat.netty.chat.client.handler;

import com.gitchat.netty.chat.ChatInfo;
import com.gitchat.netty.chat.common.Constant;
import com.gitchat.netty.chat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 处理登录响应业务
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<ChatInfo.Chat> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatInfo.Chat msg) throws Exception {
        ChatInfo.Chat.MessageType msgType = msg.getMsgType();
        if(msgType == ChatInfo.Chat.MessageType.LOGIN_RESPONSE) {
            int code = msg.getResp().getCode();
            if(Constant.STATUS.SUCCESS == 200) {
                // 登录成功
                ChatInfo.User userInfo = msg.getResp().getLoginResponse().getUserInfo();
                SessionUtil.addChannel(userInfo.getUserId(), ctx.channel());

                System.out.println("恭喜您，登录成功，现在可以发送消息了，格式：消息类型(单聊：1/群聊：2),目标（用户/群）ID,消息内容");
            }else {
                System.out.println("登录失败 error code = " + code);
            }

        }else {
            ctx.fireChannelRead(msg);
        }
    }
}
