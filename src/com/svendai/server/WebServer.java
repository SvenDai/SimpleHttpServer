package com.svendai.server;

import javax.xml.ws.http.HTTPBinding;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @program: SimpleHttpServer
 * @description: 模拟Web服务的功能
 * @author: Sven.Dai
 * @create: 2018-08-12 22:39
 **/

public class WebServer {
    //Socket Server
    private ServerSocket server;
    //换行
    private final static String CLRF = "\r\n";
    //空格
    private static final String BLANK = " ";

    /**
     * 开启web服务
     */
    public void start(){
        try {
            server = new ServerSocket(8888);
            //监听链接，接收数据
            //this.receive();
            this.receivePost();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收GET请求数据
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

    /**
     * 接收POST请求
     * 由于http协议中 请求头和请求体之间有一个空行
     * 所以直接用readline()的方法取不到post的参数
     */
    private void receivePost(){
        try {
            //监听客户端连接
            Socket client   = server.accept();
            byte[] data     = new byte[10240];
            int length      = client.getInputStream().read(data);
            String reqData  = new String(data,0,length);

            System.out.println(reqData);
            //向客户端返回响应
            this.response(client);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向客户端返回响应
     * @param client
     * @throws IOException
     */
    private void response(Socket client) throws IOException{
        //返回内容体
        StringBuilder respContext = new StringBuilder();
        respContext.append("<html><head><title>HTTP响应示例</title>" +
                "</head><body>Hello world 你好世界!</body></html>");

        //返回http头
        StringBuilder response = new StringBuilder();
        //1)  HTTP协议版本、状态代码、描述
        response.append("HTTP/1.1").append(BLANK).append("200").append(BLANK).append("OK").append(CLRF);
        //2)  响应头(Response Head)
        response.append("Server:Tomcate Server/0.0.1").append(CLRF);
        response.append("Date:").append(new Date()).append(CLRF);
        response.append("Content-type:text/html;charset=UTF-8").append(CLRF);
        //重要:返回正文的长度：字节单位
        response.append("Content-Length:").append(respContext.toString().getBytes().length).append(CLRF);
        //http头与正文之间的换行
        response.append(CLRF);
        //正文
        response.append(respContext).append(CLRF);

        System.out.println(response);

        //输出流返回响应
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        bw.write(response.toString());
        bw.flush();
        bw.close();
    }


    public static void  main(String args []){
        WebServer webServer = new WebServer();
        webServer.start();
    }
}
