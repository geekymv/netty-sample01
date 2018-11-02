package com.gitchat.netty.chat.client.commamd;

import com.gitchat.netty.chat.ChatInfo;
import com.gitchat.netty.chat.util.SessionUtil;
import com.gitchat.netty.chat.util.StringUtil;
import io.netty.channel.Channel;

public class SingleChatCommand extends AbstractCommand {

    public SingleChatCommand(Channel channel) {
        super(channel);
    }

    @Override
    public ChatInfo.Chat parseInput(String input) {
        if(StringUtil.isBlank(input)) {
            System.out.println("请输入接收人ID和要发送的消息内容");
            return null;
        }
        String[] lineToUse = input.split(",");
        if(lineToUse.length != 3) {
            System.out.println("单聊参数错误");
            return null;
        }
        String toUserId = lineToUse[1];
        String content = lineToUse[2];

        String userId = SessionUtil.getUserId(getChannel());

        ChatInfo.User fromUser = ChatInfo.User.newBuilder().setUserId(userId).build();
        ChatInfo.User toUser = ChatInfo.User.newBuilder().setUserId(toUserId).build();
        ChatInfo.SingleChat singleChat = ChatInfo.SingleChat.newBuilder()
                .setFromUser(fromUser).setToUser(toUser).setMsgContent(content).build();

        ChatInfo.Request request = ChatInfo.Request.newBuilder()
                .setSingleChat(singleChat).build();
        ChatInfo.Chat chat = ChatInfo.Chat.newBuilder()
                .setMsgType(ChatInfo.Chat.MessageType.SINGLE_CHAT).setReq(request).build();

        return chat;
    }
}
