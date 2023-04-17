package com.tedu.birdboot.core;

import com.tedu.birdboot.http.HttpServletRequest;
import com.tedu.birdboot.http.HttpServletResponse;

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
            HttpServletResponse response = new HttpServletResponse(socket);

            /**
             * 處理請求
             */
            //獲取抽象路徑
            String path = request.getUri();
            System.out.println("抽象路徑為:" + path);

            /**
             * 發送響應
             */
            //綁定類加載對象
            File baseDir = new File(
                    ClientHandler.class.getClassLoader().getResource(".").toURI()
            );

            //綁定static路徑
            File staticFile = new File(baseDir, "static");

            //綁定響應對象
            File file = new File(staticFile, path);

            //404分支
            if (file.isFile()) {//存在且非目錄
                response.setContentFile(file);
            } else {
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                response.setContentFile(new File(staticFile,"404.html"));
            }

            /**
             * 發送響應
             */
            response.response();





        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }




}
