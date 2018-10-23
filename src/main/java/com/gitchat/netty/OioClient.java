package com.gitchat.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class OioClient {

    private Socket socket;

    private volatile boolean connected;

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        connected = true;
    }

    public Socket getSocket() {
        return socket;
    }

    public static void main(String[] args) throws IOException {

        OioClient client = new OioClient();
        try {
            client.connect("127.0.0.1", 6789);
            client.receive();

            client.send();
        }finally {
            if(client.getSocket() != null) {
                client.getSocket().close();
            }
        }

    }


    private void receive() throws IOException {
        InputStream is = socket.getInputStream();

        // 新开启一个线程，用于接收服务端发送的数据
        new Thread(()-> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            try {
                while((line = reader.readLine()) != null) {
                    System.out.println("服务端返回的数据：" + line);
                }

            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                connected = false;
            }

        }).start();
    }

    private void send() throws IOException {
        OutputStream os = socket.getOutputStream();

        // 从控制台输入消息，回车发送给服务端
        Scanner scanner = new Scanner(System.in);
        PrintWriter writer =new PrintWriter(os, true);
        System.out.print("请输入：");
        while(connected) {
            String text = scanner.nextLine();
            writer.println(text);
        }
    }


}
