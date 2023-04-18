package com.tedu.birdboot.core;

import com.tedu.birdboot.http.HttpServletRequest;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 定義服務器中"與用戶進行HTTP交互"的線程任務類
 * 1.解析請求
 * 2.處理請求
 * 3.發送響應
 */
public class ClientHandler implements Runnable {
    /**
     * 定義一個Socket實例於構造器以形參傳入，待生成對象後成為成員變量，全局皆可訪問
     */
    Socket socket;

    /**
     * 解析請求:
     * 1.解析請求行
     * 2.解析消息行
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {
        try {
            /**
             * 解析請求
             */

            //V4:將解析請求的細節移動到HttpServletRequest中的構造器處理
            HttpServletRequest request = new HttpServletRequest(socket);

            /**
             * 處理請求
             */
            //獲取抽象路徑
            String path = request.getUri();
            System.out.println("抽象路徑為:" + path);

            /**
             * 發送響應
             */
//                一則響應的大致格式
//                HTTP/1.1 200 OK(CRLF) <---狀態行
//                Content-Type: text/html(CRLF) <---響應頭
//                Content-Length: 2546(CRLF)(CRLF) <---響應頭
//                1011101010101010101......(index.html页面内容) <---響應正文
            //定位當前目錄的類加載路徑
            File baseDir = new File(
                    ClientHandler.class.getClassLoader().getResource(".").toURI()
            );

            //綁定static路徑
            File staticFile = new File(baseDir, "static");

            //綁定響應對象
            File file = new File(staticFile, "index.html");


            //發送狀態行
            println("HTTP/1.1 200 OK");

            //發送響應頭
            println("Content-Type: text/html");
            println("Content-Length: " + file.length());//響應消息對應對象的實體大小

            //響應頭結束時，要單獨發送一行迴車換行
            println("");

            //發送響應正文

            //建立輸入流
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file)
            );

            //建立輸出流
            BufferedOutputStream bos = new BufferedOutputStream(
                    socket.getOutputStream()
            );

            int d;
            while ((d = bis.read()) != -1) {
                bos.write(d);
            }
            bos.flush();


        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private void println(String line) throws IOException {
        //建立輸出流
        BufferedOutputStream bos = new BufferedOutputStream(
                socket.getOutputStream()
        );

        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        bos.write(data);
        bos.write(13);//迴車
        bos.write(10);//換行
        bos.flush();
    }


}
