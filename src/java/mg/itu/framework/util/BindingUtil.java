package mg.itu.framework.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Field;
import mg.itu.framework.model.MapFile;

public class BindingUtil {
    public static String sep = ".";

    public static Map<String, Class<?>> getObjectParameters(String parameterName, Class<?> classe, String separation , HttpServletRequest req) {
        return getAttributTypes(parameterName, classe, separation, req);
    }

    public static Map<String, Class<?>> getObjectParameters(String parameterName, Class<?> classe , HttpServletRequest req) {
        return getObjectParameters(parameterName, classe, sep, req);
    }

    public static Map<String, Class<?>> getAttributTypes(String prefix, Class<?> classe, String separation , HttpServletRequest req) {
        Map<String, Class<?>> parameters = new HashMap<>();
        if (classe == null || classe == Object.class) {
            return parameters;
        } 

        for (Field field : classe.getDeclaredFields()) {
            String name = prefix + separation + field.getName();
            Class<?> type = field.getType();

            if (!checkParameter(name + separation, req)) {
                parameters.put(name, type);
            } else {
                parameters.putAll(
                    getAttributTypes(name, type, separation, req)
                );
            }
        }

        // Récupérer les attributs des classes parentes
        parameters.putAll(
            getAttributTypes(prefix, classe.getSuperclass(), separation, req)
        );

        return parameters;
    }

    public static Object buildObject(String parameterName, Class<?> classe, HttpServletRequest req) throws Exception{
        return buildObject(parameterName, classe, req, sep, null);
    }

    public static Object buildObject(String parameterName, Class<?> classe, HttpServletRequest req, String separation) throws Exception {
        return buildObject(parameterName, classe, req, separation, null);
    }

    public static Object buildObject(String parameterName, Class<?> classe,
            HttpServletRequest req, String separation, String pathFile) throws Exception {
        Object object = classe.getDeclaredConstructor().newInstance();
        Map<String, Class<?>> parameters = getObjectParameters(parameterName, classe, separation, req);

        for (Map.Entry<String, Class<?>> entry : parameters.entrySet()) {
            String name = entry.getKey();
            Class<?> type = entry.getValue();
            String value = req.getParameter(name);

            if (type != MapFile.class && type != MapFile[].class
                    && (value == null || value.isEmpty())) {
                continue;
            }

            String prefix = parameterName + separation;
            if (!name.startsWith(prefix)) {
                continue;
            }

            String path = name.substring(prefix.length());
            if (path.isEmpty()) {
                continue;
            }


            String[] attributes = path.split(java.util.regex.Pattern.quote(separation));
            if (attributes.length == 0) {
                continue;
            }

            Object currentObject = object;
            Class<?> currentClass = classe;

            // Parcourir les attributs Objects
            for (int i = 0; i < attributes.length - 1; i++) {
                Field field = findField(currentClass, attributes[i]);
                field.setAccessible(true);

                Object attributObject = field.get(currentObject);

                if (attributObject == null) {
                    attributObject = field.getType().getDeclaredConstructor().newInstance();
                    field.set(currentObject, attributObject);
                }

                currentObject = attributObject;
                currentClass = field.getType();
            }

            // Affecter la valeur au dernier attribut
            String lastAttribute = attributes[attributes.length - 1];

            Field field = findField(currentClass, lastAttribute);
            field.setAccessible(true);

            Object convertedValue;
            if (type == MapFile.class) {
                convertedValue = UploadUtil.buildFile(req, name, pathFile);

                if (convertedValue == null) {
                    continue;
                }
            } else if (type == MapFile[].class) {
                convertedValue = UploadUtil.buildFile(req, name, pathFile, true);
            } else {
                convertedValue = convertParameter(value, type);
            }

            field.set(currentObject, convertedValue);
        }

        return object;
    }

    public static boolean checkParameter(String parameterName, HttpServletRequest req) {
        Enumeration<String> names = req.getParameterNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith(parameterName)) {
                return true;
            }
        }

        return false;
    }

    private static Field findField(Class<?> classe, String name) throws NoSuchFieldException {
        Class<?> current = classe;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }

        throw new NoSuchFieldException(name);
    }

    public  static  Object convertParameter(String value, Class<?> targetType) {
        if (targetType == String.class) {
            return value;
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        } else if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(value);
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value);
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (targetType == float.class || targetType == Float.class) {
            return Float.parseFloat(value);
        } else if (targetType == short.class || targetType == Short.class) {
            return Short.parseShort(value);
        } else if (targetType == byte.class || targetType == Byte.class) {
            return Byte.parseByte(value);
        } else if (targetType == char.class || targetType == Character.class) {
            return value.charAt(0);
        }
        else if (targetType == java.util.Date.class) {
            try {
                return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(value);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    
}
