package mg.itu.framework.controller;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class FrontControllerServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println(processRequest(req, res));
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println(processRequest(req, res));
    }


    private String processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String url = req.getRequestURL().toString();
        String route = "front/";
        String path = url.substring(url.indexOf(route) + route.length());
        return path;
    }
}