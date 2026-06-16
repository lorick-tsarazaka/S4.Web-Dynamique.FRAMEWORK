package mg.itu.framework.controller;

import java.io.*;
import jakarta.servlet.*;
import java.util.*;
import jakarta.servlet.http.*;
import mg.itu.framework.annotation.Controller;
import mg.itu.framework.util.ClassUtil;

public class FrontControllerServlet extends HttpServlet {
    private List<String> listController = new ArrayList<>();

    // init
    public void init() {

    }
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
        String route = "/";
        String path = url.substring(url.indexOf(route) + route.length());
        return path;
    }

    private boolean checkAnnotation(Class<?> clazz) {
        return clazz.isAnnotationPresent(Controller.class);
    }
}