package mg.itu.framework.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mg.itu.framework.model.MethodClassMapping;
import mg.itu.framework.model.UrlMethod;

@WebListener
public class AppStartUpListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext context = sce.getServletContext();
            ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(context);

            if (springContext == null) {
                throw new RuntimeException("Le contexte Spring n'a pas pu être récupéré. ");
            }

            String packageName = context.getInitParameter("packageNames");
            String prefix = context.getInitParameter("prefix");
            String suffix = context.getInitParameter("suffix");
            String sessionAuth = context.getInitParameter("sessionAuth");
            String roleAuth = context.getInitParameter("roleAuth");
            String pathFile = context.getInitParameter("pathFile") != null ? context.getInitParameter("pathFile") : "/";

            if (packageName == null || packageName.trim().isEmpty()) {
                throw new RuntimeException("Le context-param 'packageNames' est introuvable dans web.xml.");
            }

            List<String> listController = new ArrayList<>();
            Map<UrlMethod, MethodClassMapping> listUrlMapping = new HashMap<>();

            context.setAttribute("listController", listController);
            context.setAttribute("listUrlMapping", listUrlMapping);
            context.setAttribute("prefix", prefix);
            context.setAttribute("suffix", suffix);
            context.setAttribute("pathFile", pathFile);
            context.setAttribute("springContext", springContext);
            context.setAttribute("sessionAuth", sessionAuth);
            context.setAttribute("roleAuth", roleAuth);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}