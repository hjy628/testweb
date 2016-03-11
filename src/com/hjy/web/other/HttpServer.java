package com.hjy.web.other;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * Created by hjy on 16-2-19.
 */
public class HttpServer {
    public static void main(String[] args) throws Exception{
        //创建ServerSocketChannel，监听8000端口
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(8000));
        //设置为非阻塞模式
        ssc.configureBlocking(false);
        //为ssc注册选择器
        Selector selector=Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            //等待请求，每次等待阻塞3s,超过3s后线程继续向下运行,如果传入0或者不传参数将一直阻塞
            if (selector.select(3000)==0){
                System.out.println("等待请求超时....");
                continue;
            }
            System.out.println("处理请求....");
            //获取待处理的SelectionKey
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                    //启动新线程处理SelectionKey
                new Thread(new HttpHandler(key)).run();
                //处理完后，从待处理的SelectionKey迭代器中移除当前所使用的key
                keyIterator.remove();
            }
        }
    }



    public static class HttpHandler implements Runnable{
        private int bufferSize = 1024;
        private String localCharset = "utf-8";
        private SelectionKey key;

        public HttpHandler(SelectionKey key) {
            this.key=key;
        }

        public void handleAccept()throws IOException {
            SocketChannel sc = ((ServerSocketChannel)key.channel()).accept();
            sc.configureBlocking(false);
            sc.register(key.selector(),SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
        }

        public void handleRead()throws IOException{
            //获取channel
            SocketChannel sc = (SocketChannel)key.channel();
            //获取buffer并重置
            ByteBuffer buffer = (ByteBuffer)key.attachment();
            buffer.clear();
            //没有读到内容则关闭
            if (sc.read(buffer)==-1){
                sc.close();
            }else {
               //接收请求数据
                buffer.flip();
                String receivedString = Charset.forName(localCharset).newDecoder().decode(buffer).toString();
                //控制台打印请求报文头
                String[] requestMessage = receivedString.split("\r\n");
                for (String s:requestMessage){
                    System.out.println(s);
                    //遇到空行说明报文头已经打印完
                    if (s.isEmpty())
                        break;
                }

                //控制台打印首行信息
                String[] firstLine = requestMessage[0].split(" ");
                System.out.println();
                System.out.println("Method:\t"+firstLine[0]);
                System.out.println("Url:\t"+firstLine[1]);
                System.out.println("Http Version:\t"+firstLine[2]);
                System.out.println();

                //返回客户端
                StringBuilder sendString = new StringBuilder();
                sendString.append("Http/1.1 200 OK\r\n"); //响应报文首行，200表示处理成功
                sendString.append("Content-Type:text/html;charset="+localCharset+"\r\n");
                sendString.append("\r\n"); //报文结束后加一个空行

                sendString.append("<html><head><title>显示报文</title></head><body>");
                sendString.append("接收到的请求报文是<br/>");
                for (String s:requestMessage){
                    sendString.append(s+"<br/>");
                }
                sendString.append("</body></html>");
                buffer=ByteBuffer.wrap(sendString.toString().getBytes(localCharset));
                sc.write(buffer);
                sc.close();
            }
        }

        @Override
        public void run() {
            try {
                //接收到请求连接时
                if (key.isAcceptable()){
                    handleAccept();
                }
                //读数据
                if (key.isReadable()){
                    handleRead();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
