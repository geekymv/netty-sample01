package com.gitchat.netty.oio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 服务端处理器
 * @author geekymv
 */
public class OioServerHandler implements Runnable {

    private Socket socket;

    private BufferedReader reader;

    private PrintWriter writer;

    public OioServerHandler(Socket socket) throws IOException {
        this.socket = socket;

        // 输入流
        InputStream is = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is));

        // 输出流
        OutputStream os = socket.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(os), true);
    }

    @Override
    public void run() {
        try {
            // 发送给客户端的问候
            writer.println("Welcome to " + InetAddress.getLocalHost().getHostName() + "!");

            String body = "";
            while((body = reader.readLine()) != null) {
                System.out.println("客户端发送过来的数据 = " + body);

                // 将消息写给连接的客户端
                writer.println("Did you say '" + body +"'?");
            }

        }catch (Exception e) {
            e.printStackTrace();

        } finally {
            this.close();
        }
    }

    /**
     * 关闭资源
     */
    private void close() {
        if(reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(writer != null) {
            writer.close();
        }
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
