package mg.itu.framework.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import mg.itu.framework.annotation.UrlMapping;
import java.lang.reflect.Method;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.annotation.Annotation;

public class ClassUtil {

    public static List<Class<?>> getClassesWithAnnotation(List<String> packageNames, Class<? extends Annotation> annotation) {
        List<Class<?>> classes = new ArrayList<>();
        for (String packageName : packageNames) {
            classes.addAll(getClasses(packageName));
        }
        List<Class<?>> result = new ArrayList<>();
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(annotation)) {
                result.add(clazz);
            }
        }
        return result;
    }

    public static List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();

        try (ScanResult scan = new ClassGraph()
                .enableClassInfo()
                .scan()) {

            for (ClassInfo classInfo : scan.getAllClasses()) {
                try {
                    if (classInfo.getPackageName().equals(packageName)) {
                        classes.add(classInfo.loadClass());
                    }
                } catch (Throwable e) {
                }
            }
        }

        return classes;
    }

    public static Map<String, List<List<String>>> getUrlMappings(List<Class<?>> controllers , Class<? extends Annotation> annotation) {
        Map<String, List<List<String>>> urlMappings = new HashMap<>();

        for (Class<?> controller : controllers) {
            String controllerName = controller.getName();
            List<List<String>> mappings = new ArrayList<>();

            for (Method method : controller.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    UrlMapping urlMapping = (UrlMapping) method.getAnnotation(annotation);
                    String url = getUrlValue(urlMapping);
                    List<String> mappingInfo = new ArrayList<>();
                    mappingInfo.add(method.getName());
                    mappingInfo.add(url);
                    mappings.add(mappingInfo);
                }
            }

            urlMappings.put(controllerName, mappings);
        }

        return urlMappings;
    }

    private static String getUrlValue(UrlMapping urlMapping) {
        try {
            return urlMapping.value();
        } catch (Exception e) {
            return "";
        }
    }

}