package com.my.utils;

import java.io.*;
import java.net.Socket;

public class SocketServerOperate implements Runnable {

    private Socket socket;
    //该线程所处理的Socket所对应的输入流
    BufferedReader br = null;
    String str = null;
    String content = null;
    InputStreamReader reader = null;

    /**
     * 带参构造
     *
     * @param socket
     * @throws IOException
     */
    public SocketServerOperate(Socket socket) throws IOException {
        this.socket = socket;
        reader = new InputStreamReader(this.socket.getInputStream(), "utf-8");
        br = new BufferedReader(reader);
    }

    @Override
    public void run() {
        try {
            // 处理客户端数据
            String info = null;
            while ((info = br.readLine()) != null) {
                System.out.println("客户端发过来的内容:" + info);
            }

            // 向客户端回复信息
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            os.write(("server response"));
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    socket = null;
                    System.out.println("关闭连接出现异常");
                    System.out.println("服务端 finally 异常:" + e.getMessage());
                }
            }
        }
    }
}