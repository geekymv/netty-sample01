package com.gitchat.netty.chat.client.command;

import com.gitchat.netty.chat.ChatInfo;
import io.netty.channel.Channel;

public abstract class AbstractCommand implements Command {

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public AbstractCommand(Channel channel) {
        this.channel = channel;
    }

    public abstract ChatInfo.Chat parseInput(String input);

    @Override
    public void execute(String input) {
        ChatInfo.Chat chat = parseInput(input);
        if(chat == null) {
            return;
        }
        channel.writeAndFlush(chat);
    }
}
