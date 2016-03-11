package com.hjy.web.other;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hjy on 16-2-19.
 */
public class Server {
    public static void main(String[] args) {
        try {
//        创建一个ServerSocket监听8000端口
            ServerSocket server = new ServerSocket(8000);
//            等待请求
            Socket socket = server.accept();
            //接收到请求后使用socket进行通信，创建bufferReader用于读取数据
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = is.readLine();
            System.out.println("received from client:"+line);
            //创建PrintWriter,用于发送数据
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println("received data:"+line);
            pw.flush();
            //关闭资源
            pw.close();
            is.close();
            socket.close();
            server.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }
}
