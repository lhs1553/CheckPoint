package hsim.checkpoint.core.util.excel;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TypeCheckUtil {

    private static Class<?>[] PRIMITIVE_CLASS_TYPE = {
            java.lang.Double.class, java.lang.Float.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Byte.class, java.lang.String.class, java.lang.Short.class, java.lang.Character.class, java.util.List.class
    };

    private static Class<?>[] NUMBER_CLASS_TYPE = {
            java.lang.Double.class, java.lang.Float.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Byte.class, java.lang.Short.class,
            double.class, float.class, long.class, int.class, byte.class, short.class
    };

    private static List<Class<?>> PRIMITIVE_CLASS_LIST = Arrays.stream(PRIMITIVE_CLASS_TYPE).collect(Collectors.toList());

    private static List<Class<?>> NUMBER_CLASS_LIST = Arrays.stream(NUMBER_CLASS_TYPE).collect(Collectors.toList());

    public static boolean isObjClass(Field field) {
        return isObjClass(field.getType());
    }

    public static boolean isListClass(Field field) {
        return isListClass(field.getType());
    }

    public static boolean isNumberClass(Field field) {
        return isNumberClass(field.getType());
    }

    public static boolean isObjClass(Class<?> type) {
        if (type.isPrimitive() || type.isEnum()) {
            return false;
        }
        return !PRIMITIVE_CLASS_LIST.contains(type);
    }

    public static boolean isListClass(Class<?> type) {
        if (type.isPrimitive() || type.isEnum()) {
            return false;
        }
        return type.equals(java.util.List.class);
    }

    public static boolean isNumberClass(Class<?> type) {
        return NUMBER_CLASS_LIST.contains(type);
    }


    public static String[] getDefaultWhiteList(Field field) {
        Method values = null;

        if (!field.getType().isEnum()) {
            try {
                values = field.getType().getMethod("values");
            } catch (NoSuchMethodException e) {
                log.info("field : " + field.getName() + " values method not found ");
            }

            try {
                Object[] objs = (Object[]) values.invoke(field.getType());
                return (String[]) Arrays.stream(objs).map(obj -> String.valueOf(obj)).toArray();
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.info("values invoke error : " + e.getMessage());
            }
        }
        return null;
    }
}
