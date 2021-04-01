package com.my.utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author whm
 * @date 2020/4/7
 */
public class StockServer extends Thread {

    /**
     * 监听的端口号
     */
    public static final int PORT = 8888;

    public static void main(String[] args) {
        ServerSocket server = null;
        Socket client = null;
        PrintStream out = null;
        BufferedReader buf = null;
        BufferedReader input = null;

        try{
            //服务端在20006端口监听客户端请求的TCP连接
            server = new ServerSocket(PORT);
            while(true){
                //等待客户端的连接，如果没有获取连接
                client = server.accept();
                System.out.println("与客户端连接成功！");

                //获取键盘输入
                input = new BufferedReader(new InputStreamReader(System.in));
                //获取Socket的输出流，用来向客户端发送数据
                out = new PrintStream(client.getOutputStream());
                //获取Socket的输入流，用来接收从客户端发送过来的数据
                buf = new BufferedReader(new InputStreamReader(client.getInputStream()));


                String str =  buf.readLine();
                System.out.println("客户端说："+str);

                boolean flag =true;
                while(flag){
//                    //接收从客户端发送过来的数据
//                    String str =  buf.readLine();
//                    if(str == null || "".equals(str)){
//                        flag = false;
//                    }else{
//                        if("bye".equals(str)){
//                            flag = false;
//                        }else{
//                            //显示客户端发送过来的信息
//                            System.out.println("客户端说："+str);

                            //返给客户端信息
                            System.out.print("我说：");
                            String  myWord = input.readLine();
                            out.println(myWord);
                            if("bye".equals(myWord)){
                                flag=false;
                            }
//                        }
//                    }
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out != null){
                out.close();
            }
            if(client != null){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(server != null){
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static void main2(String[] args) {
//        System.out.println("服务器启动...\n");
//
//        try {
//            ServerSocket serverSocket = new ServerSocket(PORT);
//
//            while (true) {
//                // 一旦有堵塞, 则表示服务器与客户端获得了连接
//                Socket socket = serverSocket.accept();
//
//                // 处理这次连接
//                try {
//                    // 读取客户端数据
//                    InputStreamReader is = new InputStreamReader(socket.getInputStream());
//                    //这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
//                    BufferedReader br = new BufferedReader(is);
//                    // 处理客户端数据
//                    String info = null;
//                    while ((info = br.readLine()) != null) {
//                        System.out.println("客户端发过来的内容:" + info);
//                    }
//
//                    // 向客户端回复信息
//                    PrintWriter os = new PrintWriter(socket.getOutputStream());
//                    os.write("server response");
//                    os.flush();
//
//                    //关闭资源
//                    is.close();
//                    os.close();
//                    br.close();
//                } catch (Exception e) {
//                    System.out.println("客户端主动断开连接了");
//                    System.out.println("服务器 run 异常: " + e.getMessage());
//                } finally {
//                    if (socket != null) {
//                        try {
//                            socket.close();
//                        } catch (Exception e) {
//                            socket = null;
//                            System.out.println("关闭连接出现异常");
//                            System.out.println("服务端 finally 异常:" + e.getMessage());
//                        }
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//            System.out.println("服务器异常: " + e.getMessage());
//        }
//    }
}