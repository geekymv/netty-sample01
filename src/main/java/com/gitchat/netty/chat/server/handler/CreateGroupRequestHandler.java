package com.gitchat.netty.chat.server.handler;

import com.gitchat.netty.chat.ChatInfo;
import com.gitchat.netty.chat.common.Constant;
import com.gitchat.netty.chat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<ChatInfo.Chat> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatInfo.Chat msg) throws Exception {
        ChatInfo.Chat.MessageType msgType = msg.getMsgType();
        if(ChatInfo.Chat.MessageType.CREATE_GROUP_REQUEST == msgType) {
            ChatInfo.Group group = msg.getCreateGroupRequest().getGroup();

            group = createGroup(group, ctx);
            ChatInfo.Chat chat = buidlCreateGroupResponse(group);

            ctx.writeAndFlush(chat);

        }else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 创建群组成功
     * @return
     */
    private ChatInfo.Chat buidlCreateGroupResponse(ChatInfo.Group group) {
        ChatInfo.CreateGroupResponse createGroupResponse = ChatInfo.CreateGroupResponse.newBuilder()
                .setGroup(group)
                .setCode(Constant.STATUS.SUCCESS).setMsg("创建成功")
                .build();

        ChatInfo.Chat chat = ChatInfo.Chat.newBuilder()
                .setMsgType(ChatInfo.Chat.MessageType.CREATE_GROUP_RESPONSE)
                .setCreateGroupResponse(createGroupResponse).build();

        return chat;
    }


    /**
     * 创建群组
     * @param g
     * @param ctx
     * @return
     */
    private ChatInfo.Group createGroup(ChatInfo.Group g, ChannelHandlerContext ctx) {
        String groudId = SessionUtil.uuid();
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        // 创建群组
        SessionUtil.createGroup(groudId, channelGroup);

        ChatInfo.Group group = ChatInfo.Group.newBuilder()
                .setGroupId(groudId).setGroupName(g.getGroupName()).build();

        return group;
    }
}
