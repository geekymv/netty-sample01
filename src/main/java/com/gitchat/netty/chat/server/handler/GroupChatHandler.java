package com.gitchat.netty.chat.server.handler;

import com.gitchat.netty.chat.ChatInfo;
import com.gitchat.netty.chat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

public class GroupChatHandler extends SimpleChannelInboundHandler<ChatInfo.Chat> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatInfo.Chat msg) throws Exception {

        ChatInfo.Chat.MessageType msgType = msg.getMsgType();
        if(ChatInfo.Chat.MessageType.GROUP_CHAT == msgType) {

            ChatInfo.GroupChat groupChat = msg.getGroupChat();
            ChatInfo.User fromUser = groupChat.getFromUser();
            String groupId = groupChat.getGroup().getGroupId();

            System.out.println(fromUser.getUserId() + "在群组 " + groupId + "发送了消息：" + groupChat.getMsgContent());

            ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
            channelGroup.writeAndFlush(msg);

        }else {
            ctx.fireChannelRead(msg);
        }
    }
}
