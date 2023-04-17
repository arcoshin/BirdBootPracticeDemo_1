package com.tedu.birdboot.http;

import com.tedu.birdboot.core.ClientHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 專司處理請求的相關業務方法的類
 */
public class HttpServletRequest {
    private Socket socket;

    private String method;//請求方式
    private String uri;//抽象路徑
    private String protocol;//協議版本
    private Map<String, String> headers = new HashMap<>();//用以存放消息

    public HttpServletRequest(Socket socket) throws IOException {
        this.socket = socket;

        /**
         * 1.解析請求行
         */
        parseRequestLine();


        /**
         * 2.解析消息頭
         */
        parseHeaders();

        /**
         * 3.解析消息正文
         */
        parseContent();

    }

    private void parseRequestLine() throws IOException {
        String line = readLine();

        //以空白作為分割請求的依據
        String[] split = line.split("\\s");
        method = split[0];//第一部分為請求方式
        uri = split[1];//第二部分為抽象路徑
        protocol = split[2];//第三部分為協議版本

        System.out.println("method = " + method);//打樁
        System.out.println("uri = " + uri);//打樁
        System.out.println("protocol = " + protocol);//打樁
    }

    private void parseHeaders() throws IOException {
        while (true) {
            String line = readLine();//不斷獲取(不只一行，直到剩餘消息結束都是消息行)
            if (line.isEmpty()) {//請求文的最後一行會有獨立的CRLF，應該讀到空行即為末尾
                break;
            }//如果非空
            System.out.println("消息頭: " + line);

            //解析消息 = 消息頭: +消息正文
            String[] split = line.split(":\\s");

            //解析後依照分段存放入headers
            headers.put(split[0], split[1]);

        }//退出循環後
        System.out.println("headers : " + headers);
    }

    private void parseContent() {

    }

    /**
     * 讀取請求每一行的方法
     */
    public String readLine() throws IOException {
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
        /**
         * trim此處為必要，不可省略
         * 否則輸出一個空格字符串，後期調用方法易有空指針
         */
        return line.trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}
