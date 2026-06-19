package mg.itu.framework.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import mg.itu.framework.annotation.Controller;

import java.util.*;
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

}