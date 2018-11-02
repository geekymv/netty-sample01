package com.gitchat.netty.chat.server;

import com.gitchat.netty.chat.ChatInfo;
import com.gitchat.netty.chat.server.handler.AuthHandler;
import com.gitchat.netty.chat.server.handler.LoginRequestHandler;
import com.gitchat.netty.chat.server.handler.SingleChatHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ChatServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(ChatInfo.Chat.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());

        // 登录请求处理器
        pipeline.addLast(new LoginRequestHandler());
        // 验证是否登录处理器
        pipeline.addLast(new AuthHandler());
        // 单聊处理器
        pipeline.addLast(new SingleChatHandler());
    }
}
