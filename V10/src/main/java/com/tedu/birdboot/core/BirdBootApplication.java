package com.tedu.birdboot.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BirdBootApplication {
    private ServerSocket serverSocket;

    /**
     * 構造器:服務器啟動的業務方法
     */
    public BirdBootApplication() {
        try {
            System.out.println("正在啟動服務器......");
            serverSocket = new ServerSocket(9188);
            System.out.println("服務器已完成!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 服務器的業務方法
     */
    public void start() {
        try {
            while (true) {
                //主線程負責接收用戶端連接
                System.out.println("正在等待客戶端連接......");
                Socket socket = serverSocket.accept();
                System.out.println("一個用戶已連接!!!");

                //建立另一線程任務類不斷收發數據
                new Thread(new ClientHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 程序入口
     */
    public static void main(String[] args) {
        new BirdBootApplication().start();
    }
}
