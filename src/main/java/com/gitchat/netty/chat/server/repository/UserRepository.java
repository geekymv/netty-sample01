package com.gitchat.netty.chat.server.repository;


import com.gitchat.netty.chat.ChatInfo;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author geekymv
 */
public class UserRepository {

    private static Map<String, ChatInfo.User> users = new ConcurrentHashMap<>();

    // 这里模拟用户信息（用户id、用户名都是唯一的）
    static {
        ChatInfo.User u1 = ChatInfo.User.newBuilder().setUserId("1001")
                                .setUsername("hello").setPassword("1001").build();
        ChatInfo.User u2 = ChatInfo.User.newBuilder().setUserId("1002")
                .setUsername("world").setPassword("1002").build();
        ChatInfo.User u3 = ChatInfo.User.newBuilder().setUserId("1003")
                .setUsername("kitty").setPassword("103").build();

        users.put(u1.getUserId(), u1);
        users.put(u2.getUserId(), u2);
        users.put(u3.getUserId(), u3);
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    public ChatInfo.User queryUser(String username, String password) {
        Set<Map.Entry<String, ChatInfo.User>> set = users.entrySet();
        Iterator<Map.Entry<String, ChatInfo.User>> iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry<String, ChatInfo.User> entry = iter.next();
            ChatInfo.User user = entry.getValue();
            if(username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }

}
