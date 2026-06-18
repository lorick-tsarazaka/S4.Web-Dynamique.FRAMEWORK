package mg.itu.framework.controller;

import java.io.*;
import jakarta.servlet.*;
import java.util.*;
import jakarta.servlet.http.*;
import mg.itu.framework.annotation.Controller;
import mg.itu.framework.util.ClassUtil;

@Controller
public class FrontControllerServlet extends HttpServlet {
    private List<String> listController = new ArrayList<>();

    // init
    public void init() throws ServletException {
        List<String> packageNames = new ArrayList<>();
        packageNames.add("mg.itu.framework.controller");
        packageNames.add("controller");

        List<Class<?>> controllers = ClassUtil.getClassesWithAnnotation(packageNames, Controller.class);
        for (Class<?> controller : controllers) {
            listController.add(controller.getName());
        }
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println(processRequest(req, res) + "<br>");
        for (String controller : listController) {
            out.println("- " + controller + "<br>");
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println(processRequest(req, res) + "<br>");
        for (String controller : listController) {
            out.println("- " + controller + "<br>");
        }
    }


    private String processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String url = req.getRequestURL().toString();
        String route = "/";
        String path = url.substring(url.indexOf(route) + route.length());
        return path;
    }

}