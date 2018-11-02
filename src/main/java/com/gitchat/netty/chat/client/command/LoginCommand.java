package com.gitchat.netty.chat.client.command;


import com.gitchat.netty.chat.ChatInfo;
import com.gitchat.netty.chat.util.StringUtil;
import io.netty.channel.Channel;

public class LoginCommand extends AbstractCommand {

    public LoginCommand(Channel channel) {
        super(channel);
    }

    @Override
    public ChatInfo.Chat parseInput(String input) {
        if(StringUtil.isBlank(input)) {
            System.out.println("请输入用户名和密码");
            return null;
        }
        String[] lineToUse = input.split(",");
        if(lineToUse.length != 2) {
            System.out.println("登录参数错误");
            return null;
        }

        String username = lineToUse[0];
        String password = lineToUse[1];

        ChatInfo.User user = ChatInfo.User.newBuilder()
                .setUsername(username).setPassword(password).build();
        ChatInfo.LoginRequest loginRequest = ChatInfo.LoginRequest.newBuilder()
                .setLoginUser(user).build();

        ChatInfo.Chat chat = ChatInfo.Chat.newBuilder()
                .setMsgType(ChatInfo.Chat.MessageType.LOGIN_REQUEST).setLoginRequest(loginRequest).build();

        return chat;
    }
}
