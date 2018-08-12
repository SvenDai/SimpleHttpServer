package com.svendai.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @program: SimpleHttpServer
 * @description: 模拟Web服务的功能
 * @author: Sven.Dai
 * @create: 2018-08-12 22:39
 **/

public class WebServer {
    private ServerSocket server;

    /**
     * 开启web服务
     */
    public void start(){
        try {
            server = new ServerSocket(8888);
            //监听链接，接收数据
            this.receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收请求数据
     */
    private void receive(){
        try {
            //监听链接
            Socket client = server.accept();
            //接收客户端数据
            InputStream ins = client.getInputStream();

            StringBuilder sb = new StringBuilder();

            BufferedReader  bis = new BufferedReader(new InputStreamReader(ins));

            String msg = null;
            while ((msg = bis.readLine()).length() > 0){
                sb.append(msg);
                sb.append("\r\n");
            }
            String requestInfo = sb.toString().trim();
            System.out.println(requestInfo);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void  main(String args []){
        WebServer webServer = new WebServer();
        webServer.start();
    }
}
