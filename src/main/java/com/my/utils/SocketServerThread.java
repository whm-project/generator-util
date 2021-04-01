package com.my.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerThread extends Thread {

    /**
     * 监听的端口号
     */
    public static final int PORT = 8888;
    //服务端socket
    private ServerSocket serverSocket = null;

    /**
     * 无参构造
     */
    public SocketServerThread(){
        try{
            if(null == serverSocket){
                this.serverSocket = new ServerSocket(PORT);
                System.out.println("socket start");
            }
        } catch (IOException e){
            System.out.println("SocketThread创建socket服务出错");
            e.printStackTrace();
        }
    }

    /**
     * 监听的线程
     */
    public void run(){
        while(!this.isInterrupted()){
            Socket socket = null;
            try {
                if(serverSocket==null){
                    this.serverSocket = new ServerSocket(PORT);
                    System.out.println("socket start");
                }else if(serverSocket.isClosed()){
                    break;
                }
                socket = serverSocket.accept();
                if(null != socket && !socket.isClosed()){
                    //处理接受的数据
                    Thread t1 = new Thread(new SocketServerOperate(socket));
                    t1.start();
                }else{
                    break;
                }
            }catch (Exception e) {
                System.out.println("SocketThread创建socket服务出错111111111111111111111111");
                e.printStackTrace();

            }
        }
    }

    public void closeSocketServer(){
        try {
            if(null!=serverSocket && !serverSocket.isClosed())
            {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}