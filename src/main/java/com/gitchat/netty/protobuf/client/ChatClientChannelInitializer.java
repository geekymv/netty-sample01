package com.gitchat.netty.protobuf.client;

import com.gitchat.netty.protobuf.ChatInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ChatClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 用于半包处理
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        // 设置解码器ProtobufDecoder 解码的目标类是ChatInfo.Chat 实例
        pipeline.addLast(new ProtobufDecoder(ChatInfo.Chat.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast(new ChatClientHandler());
    }
}
