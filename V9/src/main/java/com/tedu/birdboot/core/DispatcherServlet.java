package com.tedu.birdboot.core;

import com.tedu.birdboot.http.HttpServletRequest;
import com.tedu.birdboot.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

/**
 * 程序調度員
 */
public class DispatcherServlet {
    private static File baseDir;
    private static File staticDir;

    static {
        try {
            baseDir = new File(
                    DispatcherServlet.class.getClassLoader().getResource(".").toURI()
            );

            staticDir = new File(baseDir, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        //獲取抽象路徑
        String path = request.getUri();
        System.out.println("抽象路徑為:" + path);

        //綁定響應對象
        File file = new File(staticDir, path);

        //404分支
        if (file.isFile()) {//存在且非目錄
            response.setContentFile(file);
            /**
             * V9新增，由於HttpServletResponse中已經動態改寫
             * 現在會自動以 響頭鍵值對Headers的key&value值 自動拼接發送消息頭
             * 因此目前僅須在此調用addHeader，完整填寫Headers
             */
            response.addHeader("Content-Type", "text/html");
            response.addHeader("Content-Length",file.length()+"");
        } else {
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            response.setContentFile(new File(staticDir, "404.html"));
            /**
             * V9同上原理，404定位完成後，依然要半自動填寫Headers
             */
            response.addHeader("Content-Type", "text/html");
            response.addHeader("Content-Length",file.length()+"");
        }


    }
}
