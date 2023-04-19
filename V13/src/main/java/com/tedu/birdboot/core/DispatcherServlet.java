package com.tedu.birdboot.core;

import com.tedu.birdboot.controller.UserController;
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
        String path = request.getRequestURI();
        System.out.println("請求路徑為:" + path);

        /**
         * V13要能判定請求路徑所指的對象是頁面或是業務
         */
        if ("/regUser".equals(path)) {
            new UserController().reg(request, response);
        } else {

            //綁定響應對象
            File file = new File(staticDir, path);

            //404分支
            if (file.isFile()) {//存在且非目錄
                response.setContentFile(file);
                /**
                 * V10響應發送的相關業務全部回歸HttpServletRequest
                 * 於設置響應正文實體對象時可一併動態生成
                 */
            } else {
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                response.setContentFile(new File(staticDir, "404.html"));
            }
        }

    }
}
