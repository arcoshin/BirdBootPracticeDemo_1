package com.tedu.birdboot.http;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * HTTP響應業務
 */
public class HttpServletResponse {
    private static MimetypesFileTypeMap mftm = new MimetypesFileTypeMap();//自動查詢消息頭類型

    private Socket socket;
    private int statusCode = 200;
    private String statusReason = "OK";
    private File contentFile;//響應正文對應的實體文件
    private Map<String, String> headers = new HashMap<>();//存放響應頭的鍵值對

    public HttpServletResponse(Socket socket) {
        this.socket = socket;
    }

    public void response() throws IOException {
        //發送狀態行
        sendStatusLine();

        //發送響應頭
        sendHeaders();

        //發送響應正文
        sendContent();
    }

    private void sendStatusLine() throws IOException {
        //發送狀態行
        println("HTTP/1.1" + " " + statusCode + " " + statusReason);
    }

    private void sendHeaders() throws IOException {
        //發送響應頭(動態生成不再寫死)
        Set<Map.Entry<String, String>> entrySet = headers.entrySet();
        for (Map.Entry<String, String> e : entrySet) {
            println(e.getKey() + ": " + e.getValue());
        }

        //響應頭結束時，要單獨發送一行迴車換行
        println("");
    }

    private void sendContent() throws IOException {
        //發送響應正文
        //建立輸入流
        BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(contentFile)
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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
        addHeader("Content-Type", mftm.getContentType(contentFile));
        addHeader("Content-Length", contentFile.length() + "");
    }
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
}
