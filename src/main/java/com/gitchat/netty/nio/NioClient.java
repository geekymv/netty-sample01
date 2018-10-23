package com.gitchat.netty.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author geekymv
 */
public class NioClient {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 6789));

        while (true) {
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectionKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                iter.remove();

                if(key.isConnectable()) {
                    SocketChannel sc = (SocketChannel)key.channel();
                    socketChannel.finishConnect();

                    System.out.println("连接服务器成功！");

                    sc.register(selector, SelectionKey.OP_READ);

                    // 控制台输入数据并发送
                    input(sc);

                }else if(key.isReadable()) {
                    SocketChannel sc = (SocketChannel)key.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = sc.read(buffer);
                    if(len > 0) {
                        System.out.println("从服务端发送的消息：" + new String(buffer.array(), 0, len));
                    }
                }
            }
        }
    }

    /**
     * 从控制台输入数据，并发送出去
     * @param channel
     */
    private static void input( SocketChannel channel) {
        new Thread(()-> {
            ByteBuffer buffer =  ByteBuffer.allocate(1024);
            while (true) {
                try {
                    buffer.clear();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String line = reader.readLine();

                    buffer.put(line.getBytes());
                    buffer.flip();
                    channel.write(buffer);

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
