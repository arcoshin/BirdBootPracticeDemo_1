package com.tedu.birdboot.http;

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

    private String requestURI;//保存uri中"?"左側的請求路徑部分
    private String queryString;//保存uri中"?"右側的參數團部分

    private Map<String, String> headers = new HashMap<>();//消息頭鍵值對
    private Map<String, String> parameters = new HashMap<>();//參數鍵值對

    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
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

    private void parseRequestLine() throws IOException, EmptyRequestException {
        /**
         * V11排除偶發不定時數組下標越界異常:原因在於HTTP協議中允許客戶端發送空請求
         * 此時協議中亦規定，服務器對於空請求應忽略
         */
        String line = readLine();
        if (line.isEmpty()) {
            throw new EmptyRequestException();
        }


        //以空白作為分割請求的依據
        String[] split = line.split("\\s");
        method = split[0];//第一部分為請求方式
        uri = split[1];//第二部分為抽象路徑
        protocol = split[2];//第三部分為協議版本

        System.out.println("method = " + method);//打樁
        System.out.println("uri = " + uri);//打樁
        System.out.println("protocol = " + protocol);//打樁

        //V12新增對uri的進一步解析
        parseRequestURI();

        System.out.println("requestURI = " + requestURI);
        System.out.println("queryString = " + queryString);
        System.out.println("parameters" + parameters);


    }

    private void parseRequestURI() {
        /**
         * V12進一步解析uri可能有三種形式:
         * 1.不含參數的，如: /index.html------>split[/index.html]
         * 2.含參數的，如: /regUser?age=999&------>split[/regUser, age=999&]
         * 3.輸入框未命名時的提交，如: /regUser?------>split[\regUser]
         * 其中只有第二種需要進一步解析!!
         */
        String[] split = uri.split("\\?");//以"?"作為分割點
        requestURI = split[0];//必為請求路徑
        if (split.length > 1) {//有成功分離出參數團者才走進分支二次解析
            queryString = split[1];

            /**
             * 參數團格式範例:username=xsupeipei&password=123456&nickname=peipei&age=999
             */
            split = queryString.split("&");//依照"&"分離為數個鍵值對
            for (String sp : split) {//遍歷每一組
                String[] s = sp.split("=", 2);//將每一組再以"="拆分，且必定拆成兩份
                parameters.put(s[0], s[1]);
            }
        }
    }

    private void parseHeaders() throws IOException {
        while (true) {
            String line = readLine();//不斷獲取(不只一行，直到剩餘消息結束都是消息行)
            if (line.isEmpty()) {//請求文的最後一行會有獨立的CRLF，讀到空行即為末尾(readLine返回值時有調用trim)
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

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }
}
