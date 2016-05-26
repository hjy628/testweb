package com.hjy.web.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebListener;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by hjy on 16-5-26.
 */
@WebListener
public class Work implements ServletContextListener {

    private static final BlockingQueue<AsyncContext> queue = new LinkedBlockingQueue<AsyncContext>();

    private volatile Thread thread;

    public static void add(AsyncContext c) {
        queue.add(c);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("有作用！！！");
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                        AsyncContext context;
                        while ((context = queue.poll()) != null) {
                            try {
                                ServletResponse response = context.getResponse();
                                response.setContentType("text/plain");
                                PrintWriter out = response.getWriter();
                                out.printf("Thread %s completed the task", Thread.currentThread().getName());
                                out.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException(e.getMessage(), e);
                            } finally {
                                context.complete();
                            }
                        }
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        thread.interrupt();
    }
}
