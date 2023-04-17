package com.tedu.birdboot.core;

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
             * 解析請求行
             */

            //建立由socket的輸入流
            InputStream in = socket.getInputStream();

            //獲取讀取的每一行
            int data;
            char thisChar = 0;//當前讀取字符
            char lastChar = 0;//上次讀取字符
            StringBuilder builder = new StringBuilder();//拼接字符串的容器
            while ((data = in.read()) != -1) {
                thisChar = (char) data;
                if (thisChar == 10 && lastChar == 13) {//如果符合回車換行
                    break;//就停止循環
                }//否則
                builder.append(thisChar);//拼接當前所讀
                lastChar = thisChar;//當前對象輸出完交接往後傳遞，以便進入下次循環判定
            }
            String line = builder.toString();
            System.out.println("請求行 " + line);


//            GET /index.html HTTP/1.1   <---請求 包含請求方式 抽象路徑 協議版本
//            Host: localhost:9188       <---消息 包含消息頭 消息內文
//            Connection: keep-alive
//            Cache-Control: max-age=0
//            .
//            .
//            .

            //解析請求行 : 請求方式 + 抽象路徑 + 協議版本
            String method;//請求方式
            String uri;//抽象路徑
            String protocol;//協議版本

            //以空白作為分割請求的依據
            String[] split = line.split("\\s");
            method = split[0];//第一部分為請求方式
            uri = split[1];//第二部分為抽象路徑
            protocol = split[2];//第三部分為協議版本

            /**
             * 解析後得到的請求資訊
             */
            System.out.println("method = " + method);
            System.out.println("uri = " + uri);
            System.out.println("protocol = " + protocol);


//


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
