package com.my.utils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @author whm
 * @date 2020/4/27
 */
public class SocketClient {

    static int flag = 1;

    // 搭建客户端
    public static void main(String[] args) throws IOException {
        flag ++;
        try {
            // 1、创建客户端Socket，指定服务器地址和端口
            Socket socket = new Socket("127.0.0.1", 8888);

            //打开输入流输出流
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            //字符输入流输出流
            DataInputStream dis = new DataInputStream(is);
            DataOutputStream dos = new DataOutputStream(os);

            //输出内容
            System.out.println(dis.readUTF());
            dos.writeUTF("111111111111111");
            System.out.println(dis.readUTF());
            dos.writeUTF("222222222222222");

            System.out.println(flag);

        } catch (Exception e) {
            System.out.println("can not listen to:" + e);// 出错，打印出错信息
        }
    }

//    // 搭建客户端
//    public static void main(String[] args) throws IOException {
//        try {
//            // 1、创建客户端Socket，指定服务器地址和端口
//            Socket socket = new Socket("127.0.0.1", 8888);
//
//            // 2、向服务器端发送请求
//            PrintWriter os = new PrintWriter(socket.getOutputStream());
//            os.write("client request");
//            os.flush();
//            socket.shutdownOutput();
//
//            //3、获取输入流
//            InputStreamReader is = new InputStreamReader(socket.getInputStream());
//            BufferedReader br = new BufferedReader(is);
//            String info = null;
//            while ((info = br.readLine()) != null) {
//                System.out.println("server info : "+info);
//            }
//            socket.shutdownInput();
//
//            //4、关闭资源
//            os.close();
//            is.close();
//            br.close();
//            socket.close();
//        } catch (Exception e) {
//            System.out.println("can not listen to:" + e);// 出错，打印出错信息
//        }
//    }

}
