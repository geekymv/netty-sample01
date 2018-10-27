package com.gitchat.netty.chat.server;

import com.gitchat.netty.chat.common.Constant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 存储客户端用户唯一标识与对应Channel的映射<userId, Channel>
    private static final Map<String, Channel> channels = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String content = msg + Constant.DELIMITER;
        System.out.println("客户端发送过来的消息 = " + msg);

        // 循环遍历channel映射
        Set<Map.Entry<String, Channel>> set = channels.entrySet();
        Iterator<Map.Entry<String, Channel>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Channel> entry = iterator.next();
            Channel channel = entry.getValue();
            channel.writeAndFlush(content);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String channelId = ctx.channel().id().asLongText();
        System.out.println("用户"+ channelId +"上线了");
        channels.putIfAbsent(channelId, ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String channelId = ctx.channel().id().asLongText();
        System.out.println("用户"+ channelId +"下线了");
        channels.remove(channelId);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
