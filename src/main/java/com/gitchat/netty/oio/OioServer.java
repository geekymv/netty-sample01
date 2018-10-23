package com.gitchat.netty.oio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author geekymv
 */
public class OioServer {

    public static void main(String[] args) throws IOException {
        OioServer server = new OioServer();
        server.serve(6789);
    }

    public void serve (final int port) throws IOException {
        ServerSocket serverSocket = null;

        try {
            // 将服务器绑定到指定端口
            serverSocket = new ServerSocket(port);
            System.out.println("OioServer 已启动，监听端口：" + port);

            while(true) {
                // 接受客户端的连接，如果没有客户端接入，则主线程阻塞在accept()方法上。
                Socket socket = serverSocket.accept();
                System.out.println("接收的连接来自 " + socket);

                // 创建并启动一个新的线程来处理这个连接
                new Thread(new OioServerHandler(socket)).start();
            }

        } finally {
            if(serverSocket != null) {
                serverSocket.close();
                System.out.println("OioServer已关闭");
            }
        }

    }
}
