package com.tedu.birdboot.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * HTTP響應業務
 */
public class HttpServletResponse {
    private Socket socket;
    private int statusCode = 200;
    private String statusReason = "OK";
    private File contentFile;//響應正文對應的實體文件

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
        //發送響應頭
        println("Content-Type: text/html");
        println("Content-Length: " + contentFile.length());//響應消息對應對象的實體大小

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
    }
}
