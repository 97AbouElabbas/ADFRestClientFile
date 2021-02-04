package com.api.servlets;

import com.api.utils.FileClientUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class ImageServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = (request.getParameter("id"));
        if (path == null) {
            System.err.println("PATH Is NULL");
        }
        OutputStream os = response.getOutputStream();
        try {
            FileClientUtil.fileDownload("http://localhost:8080/downloadFile/" + path, os);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
}
