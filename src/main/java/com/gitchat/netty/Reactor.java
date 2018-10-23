package com.gitchat.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    public Reactor(int port) {
        try {
            // ServerSocketChannel打开，用于监听客户端的连接
            serverSocketChannel = ServerSocketChannel.open();

            // 绑定端口，设置连接为非阻塞模式
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            // 创建Reactor线程，创建多路复用器并启动线程
            selector = Selector.open();

            // 将ServerSocketChannel 注册到Reactor 线程的多路复用器上，监听ACCEPT 事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        try {
            // 多路复用器在线程run方法的无限循环体内轮询准备就绪的key
            while(true) {
                try {
                    int num = selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    for(Iterator<SelectionKey> iter = selectionKeys.iterator(); iter.hasNext(); ) {

                        SelectionKey key = iter.next();

                        iter.remove();

                        handleInput(key);
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if(!key.isValid()) {
            return;
        }

        if(key.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);

            socketChannel.register(selector, SelectionKey.OP_READ);
        }

        if(key.isReadable()) {
            SocketChannel socketChannel = (SocketChannel)key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int readBytes = socketChannel.read(byteBuffer);
            if(readBytes > 0) {

                byteBuffer.flip();

                socketChannel.write(byteBuffer);

            }else if(readBytes < 0) {
                // 将对端链路关闭
                key.cancel();
                socketChannel.close();

            }else {
                // 读到0字节，忽略
            }
        }

    }



}
