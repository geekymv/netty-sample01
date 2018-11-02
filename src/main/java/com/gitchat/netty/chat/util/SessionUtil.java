package com.gitchat.netty.chat.util;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SessionUtil {

    // <userId, Channel>
    private static final Map<String, Channel> CHANNELS = new ConcurrentHashMap<>();

    private static final AttributeKey<String> USER_SESSION = AttributeKey.newInstance("USER_SESSION");

    public static Channel getChannel(String userId) {
        return CHANNELS.get(userId);
    }

    public static Channel addChannel(String userId, Channel channel) {
        channel.attr(USER_SESSION).set(userId);
        return CHANNELS.putIfAbsent(userId, channel);
    }

    /**
     * 移除Channel
     * @param channel
     * @return
     */
    public static Channel removeChannel(Channel channel) {
        String userId = channel.attr(USER_SESSION).get();
        if(StringUtil.isBlank(userId)) {
            return null;
        }
        return CHANNELS.remove(userId);
    }

    /**
     * 判断Channel 是否已经登录
     * @param channel
     * @return
     */
    public static boolean isLogin(Channel channel) {
        String userId = getUserId(channel);
        if(StringUtil.isBlank(userId)) {
            return false;
        }
        return true;
    }

    public static String getUserId(Channel channel) {
        return channel.attr(USER_SESSION).get();
    }
}
