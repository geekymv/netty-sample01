package com.gitchat.netty.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author geekymv
 */
public class NioServer {

    private static final int PORT = 6789;

    public static void main(String[] args) throws IOException {
        // 创建一个选择器来处理Channel
        Selector selector = Selector.open();

        // 打开服务端套接字通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 设置此通道为非阻塞模式
        ssc.configureBlocking(false);
        // 绑定端口
        ssc.bind(new InetSocketAddress(PORT));

        // 向给定的选择器注册此通道的接受连接事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("NioServer 已启动，监听端口：" + PORT);

        while (true) {
            // select()方法会阻塞，直到至少有一个已注册的事件发生。
            int num = selector.select();
            System.out.println("num = " + num);

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectionKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                // 删除处理过的key
                iter.remove();

                if(key.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    // 接受新的连接，并将它注册到选择器
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);

                    System.out.println("接受的连接来自 " + sc);

                    byte[] content = ("Welcome to " + InetAddress.getLocalHost().getHostName() + "!").getBytes();
                    ByteBuffer byteBuffer =  ByteBuffer.allocate(1024);
                    byteBuffer.put(content);

                    // 翻转缓存区（将缓存区写模式变为读模式）
                    byteBuffer.flip();
                    sc.write(byteBuffer);

                }else if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();

                    // 读取客户端发送过来的消息
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    while (true) {
                        buffer.clear();
                        int read = 0;
                        try {
                            read = sc.read(buffer);
                        }catch (IOException ex) {
                            sc.close();
                            System.out.println("读取发生异常 = " + ex);
                            break;
                        }
                        if(read == 0) {
                            // 读到0字节，结束循环读取
                            break;
                        }
                        if(read == -1) {
                            // 将对端链路关闭
                            key.cancel();
                            sc.close();
                            break;
                        }
                        String body = new String(buffer.array(), 0, read);
                        System.out.println("客户端发送过来的数据 = " + body);

                        buffer.clear();
                        buffer.put(("Did you say '" + body +"'?").getBytes());

                        buffer.flip();
                        sc.write(buffer);
                    }

                }else if(key.isWritable()) {
                    System.out.println("writable...");
                }
            }
        }
    }

}
