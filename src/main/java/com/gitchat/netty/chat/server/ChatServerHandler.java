//package com.gitchat.netty.chat.server;
//
//import com.gitchat.netty.chat.ChatInfo;
//import com.gitchat.netty.chat.common.Constant;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class ChatServerHandler extends SimpleChannelInboundHandler<ChatInfo.Chat> {
//
//    // 存储客户端用户唯一标识与对应Channel的映射<userId, Channel>
//    private static final Map<String, Channel> channels = new ConcurrentHashMap<>();
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, ChatInfo.Chat chat) throws Exception {
//
//        ChatInfo.Chat.MessageType msgType = chat.getMsgType();
//        if(ChatInfo.Chat.MessageType.LOGIN_REQUEST == msgType) {
//            ChatInfo.User loginUser = chat.getReq().getLogin().getLoginUser();
//            String username = loginUser.getUsername();
//            String password = loginUser.getPassword();
//
//            String uuid = UUID.randomUUID().toString().replace("-", "");
//
//            ChatInfo.LoginResponse loginResponse = ChatInfo.LoginResponse.newBuilder()
//                                                            .setToken(uuid)
//                                                            .build();
//            ChatInfo.Response response = ChatInfo.Response.newBuilder()
//                                                .setLoginResponse(loginResponse)
//                                                .setCode(200)
//                                                .setMsg("登录成功")
//                                                .build();
//
//            ctx.writeAndFlush(response);
//        }
//
//
//        // 循环遍历channel映射
//        Set<Map.Entry<String, Channel>> set = channels.entrySet();
//        Iterator<Map.Entry<String, Channel>> iterator = set.iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, Channel> entry = iterator.next();
//            Channel channel = entry.getValue();
//            channel.writeAndFlush(content);
//        }
//    }
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        String channelId = ctx.channel().id().asLongText();
//        System.out.println("用户"+ channelId +"上线了");
//        channels.putIfAbsent(channelId, ctx.channel());
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        String channelId = ctx.channel().id().asLongText();
//        System.out.println("用户"+ channelId +"下线了");
//        channels.remove(channelId);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}
