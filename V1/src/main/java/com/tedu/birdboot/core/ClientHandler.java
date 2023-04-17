package com.tedu.birdboot.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 定義服務器中"與用戶進行HTTP交互"的線程任務類
 */
public class ClientHandler implements Runnable {
    /**
     * 定義一個Socket實例於構造器以形參傳入，待生成對象後成為成員變量，全局皆可訪問
     */
    Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {
        try {
            /**
             * 建立由socket的輸入流
             */
            InputStream in = socket.getInputStream();

            /**
             * 讀出數據 : 會發現讀出一大段HTTP請求
             */
            int data;
            while ((data = in.read()) != -1) {
                System.out.print((char) data);
            }

//            GET /index.html HTTP/1.1
//            Host: localhost:9188
//            Connection: keep-alive
//            Cache-Control: max-age=0
//            sec-ch-ua: "Chromium";v="112", "Google Chrome";v="112", "Not:A-Brand";v="99"
//            sec-ch-ua-mobile: ?0
//            sec-ch-ua-platform: "Windows"
//            DNT: 1
//            Upgrade-Insecure-Requests: 1
//            User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36
//            Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
//            Sec-Fetch-Site: none
//            Sec-Fetch-Mode: navigate
//            Sec-Fetch-User: ?1
//            Sec-Fetch-Dest: document
//            Accept-Encoding: gzip, deflate, br
//            Accept-Language: zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6
//            Cookie: Idea-52a54c1f=3870a42d-25c8-4458-be63-11faecfba2c6


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}