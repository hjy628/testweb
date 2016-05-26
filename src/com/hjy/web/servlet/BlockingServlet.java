package com.hjy.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hjy on 16-5-26.
 */
@WebServlet(urlPatterns = "/BlockingServlet")
public class BlockingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        try {
            long start = System.currentTimeMillis();
            Thread.sleep(2000);
            String name = Thread.currentThread().getName();
            long duration = System.currentTimeMillis() - start;
            System.out.println("调用一次");
            response.getWriter().printf("Thread %s completed the task in %d ms.", name, duration);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}