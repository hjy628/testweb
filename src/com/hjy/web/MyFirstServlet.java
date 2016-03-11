package com.hjy.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hjy on 16-2-19.
 */
@WebServlet(urlPatterns = "/myFirstServlet")
public class MyFirstServlet  extends HttpServlet {

    private static final long serialVersionUID = -19154635321657451L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/*        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            // Write some content
            out.println("<html>");
            out.println("<head>");
            out.println("<title>MyFirstServlet</title>");
            out.println("<script>alert('哈哈哈你中招了！！！')</script>");
            out.println("</head>");
            out.println("<body>");
            out.println("<center><h2>Servlet MyFirstServlet at " + req.getContextPath() + "</h2></center>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }*/

        Map<String,String> data = getData();

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            // Write some content
            out.println("<html>");
            out.println("<head>");
            out.println("<title>CalendarServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Hello " + data.get("username") + ", " + data.get("message") + "</h2>");
            out.println("<h2>The time right now is : " + new Date() + "</h2>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public String getServletInfo() {
        return "MyFirstServlet";
    }

    //This method will access some external system as database to get user name, and his personalized message
    private Map<String, String> getData()
    {
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", "Guest");
        data.put("message",  "Welcome to my world !!");
        return data;
    }


}
