package mg.itu.framework.util;

import java.util.*;
import java.lang.annotation.Annotation;

public class ClassUtil {

    public static List<Class<?>> getClassesWithAnnotation(String packageName , Annotation annotation) {
        List<Class<?>> classes = getClasses(packageName);
        for(Class<?> clazz : classes) {
            if(!clazz.isAnnotationPresent(annotation.annotationType())) {
                classes.remove(clazz);
            }
        }

        return classes;
    }



    public static List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        return classes;
    }

}