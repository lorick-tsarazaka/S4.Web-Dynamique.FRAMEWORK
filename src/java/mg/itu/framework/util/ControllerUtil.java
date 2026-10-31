package mg.itu.framework.util;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.framework.model.MethodClassMapping;
import java.util.HashMap;
import java.util.Map;

public class ControllerUtil {
    private static String mapControllersKey = "mapControllers";

    public static void addControllerContext(HttpServletRequest req, String className , Object instance) {
        Map<String, Object> mapControllers = getMapControllers(req);
        if (mapControllers == null) {
            mapControllers = new HashMap<>();
            req.getServletContext().setAttribute(mapControllersKey, mapControllers);
        }
        mapControllers.put(className, instance);
    }

    public static Object getInstance(MethodClassMapping mapping , HttpServletRequest req) throws Exception {
        initControllerContext(req);
        Map<String, Object> mapControllers = getMapControllers(req);
        if (mapControllers.get(mapping.getClasse().getName()) != null){
            return mapControllers.get(mapping.getClasse().getName());
        } else {
            try{
                Object instance = mapping.getClasse().getDeclaredConstructor().newInstance();
                addControllerContext(req ,mapping.getClasse().getName(), instance);
                return instance;
            } catch (Exception e) {
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapControllers(HttpServletRequest req) {
        return (Map<String, Object>) req.getServletContext().getAttribute(mapControllersKey);
    }

    public static void initControllerContext(HttpServletRequest req) {
        if(req.getServletContext().getAttribute(mapControllersKey) == null) {
            Map<String, Object> mapControllers = new HashMap<>();
            req.getServletContext().setAttribute(mapControllersKey, mapControllers);
        }
    }

}