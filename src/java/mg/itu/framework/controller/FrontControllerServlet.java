package mg.itu.framework.controller;

import java.io.*;
import jakarta.servlet.*;
import java.util.*;
import jakarta.servlet.http.*;
import mg.itu.framework.annotation.Controller;
import mg.itu.framework.annotation.UrlMapping;
import mg.itu.framework.util.ClassUtil;

@Controller
public class FrontControllerServlet extends HttpServlet {
    private List<String> listController = new ArrayList<>();
    private Map<String, List<List<String>>> listUrlMapping = new HashMap<>();

    // init
    public void init() throws ServletException {
        List<String> packageNames = new ArrayList<>();
        packageNames.add("mg.itu.framework.controller");
        packageNames.add("controller");

        List<Class<?>> controllers = ClassUtil.getClassesWithAnnotation(packageNames, Controller.class);
        for (Class<?> controller : controllers) {
            listController.add(controller.getName());
        }
        listUrlMapping = ClassUtil.getUrlMappings(controllers, UrlMapping.class);

    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        String url = processRequest(req, res);
        out.println("URL : " + url + "<br><br>");
        // for (String controller : listController) {
        //     out.println("- " + controller + "<br>");
        // }
        getUrlMapping(url, out);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("URL : " + processRequest(req, res) + "<br><br>");
        for (String controller : listController) {
            out.println("- " + controller + "<br>");
        }
    }


    private String processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String url = req.getRequestURL().toString();
        String[] urlParts = url.split("/");
        String path = "";
        for (int i = 4 ; i < urlParts.length; i++) {
            path += "/" + urlParts[i];
        }
        
        return path;
        
    }

    private boolean isUrlAccessible(String url) {
        for (String controller : listUrlMapping.keySet()) {
            List<List<String>> mappings = listUrlMapping.get(controller);
            for (List<String> mappingInfo : mappings) {
                if (mappingInfo.get(1).equals(url)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void getUrlMapping(String url , PrintWriter out) {
        if(isUrlAccessible(url)) {
            for (String controller : listUrlMapping.keySet()) {
                List<List<String>> mappings = listUrlMapping.get(controller);
                for (List<String> mappingInfo : mappings) {
                    if (mappingInfo.get(1).equals(url)) {
                        out.println("- " + controller + " -> ");
                        out.println(mappingInfo.get(0)  + " : " + mappingInfo.get(1) + "<br>");
                    }
                }
            }
        } else {
            out.println("L'URL n'est pas accessible , voici la liste des URL accessibles : <br>");
            for (String controller : listUrlMapping.keySet()) {
                List<List<String>> mappings = listUrlMapping.get(controller);
                for (List<String> mappingInfo : mappings) {
                    out.println("- " + controller + " -> ");
                    out.println(mappingInfo.get(0)  + " : " + mappingInfo.get(1) + "<br>");
                }
            }
        }
    }

}