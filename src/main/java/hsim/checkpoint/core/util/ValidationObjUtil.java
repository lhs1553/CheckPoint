package hsim.checkpoint.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.http.HttpStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class ValidationObjUtil {

    private static final Class[] EMPTY_CLASS = new Class[0];
    private static final String GET_METHOD_PREFIX = "get";
    private static final String SET_METHOD_PREFIX = "set";
    private static final String IS_METHOD_PREFIX = "is";

    public static ObjectMapper getDefaultObjectMapper() {

        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper;
    }


    private static boolean isBlockFieldMethod(Method m, String... blockFields) {

        if (blockFields == null) {
            return false;
        }

        String methodField = getFieldNameFromMethod(m);

        for (String field : blockFields) {
            if (field.equals(methodField)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumberObj(Object type) {
        try {
            Double.valueOf(type + "");
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNumberType(Class<?> type) {
        return isIntType(type) || isDoubleType(type);
    }

    public static boolean isDoubleType(Class<?> type) {
        return (type.isPrimitive() && type == float.class) || (type.isPrimitive() && type == double.class)
                || (!type.isPrimitive() && type == Float.class) || (!type.isPrimitive() && type == Double.class);
    }


    public static boolean isIntType(Class<?> type) {
        return (type.isPrimitive() && type == int.class) || (type.isPrimitive() && type == long.class)
                || (!type.isPrimitive() && type == Integer.class) || (!type.isPrimitive() && type == Long.class);
    }

    private static Class<?> getSubType(Class<?> mainType) {
        if (mainType.isPrimitive() && mainType == int.class) {
            return Integer.class;
        } else if (mainType.isPrimitive() && mainType == long.class) {
            return Long.class;
        } else if (mainType.isPrimitive() && mainType == float.class) {
            return Float.class;
        } else if (mainType.isPrimitive() && mainType == double.class) {
            return Float.class;
        } else if (!mainType.isPrimitive() && mainType == Integer.class) {
            return int.class;
        } else if (!mainType.isPrimitive() && mainType == Long.class) {
            return long.class;
        } else if (!mainType.isPrimitive() && mainType == Float.class) {
            return float.class;
        } else if (!mainType.isPrimitive() && mainType == Double.class) {
            return double.class;
        }

        return null;
    }

    public static Method getSetterMethodNotCheckParamType(Class<?> cType, String fieldName) {
        String methodName = getMethodName(fieldName, SET_METHOD_PREFIX);
        Method[] methods = cType.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName) && m.getParameterCount() == 1) {
                return m;
            }
        }
        return null;
    }

    public static Method getSetterMethod(Class<?> cType, String fieldName, Class<?> paramType) {

        Class<?> subType = getSubType(paramType);
        String methodName = getMethodName(fieldName, SET_METHOD_PREFIX);

        try {
            return cType.getMethod(methodName, paramType);
        } catch (NoSuchMethodException e) {
            try {
                return cType.getMethod(methodName, subType);
            } catch (NoSuchMethodException e1) {
                //log.info("setter method not found : " + fieldName);
                return null;
            }
        }
    }

    public static String getMethodName(String name, String prefix) {
        return prefix + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String getFieldNameFromMethod(Method m) {
        String methodName = m.getName();

        if (methodName.startsWith(GET_METHOD_PREFIX)) {
            methodName = methodName.replaceFirst(GET_METHOD_PREFIX, "");
        } else if (methodName.startsWith(IS_METHOD_PREFIX)) {
            methodName = methodName.replaceFirst(IS_METHOD_PREFIX, "");
        } else if (methodName.startsWith(SET_METHOD_PREFIX)) {
            methodName = methodName.replaceFirst(SET_METHOD_PREFIX, "");
        }

        return methodName.substring(0, 1).toLowerCase() + methodName.substring(1);

    }

    public static Method getGetterMethod(Class<?> c, String field) {

        try {
            return c.getMethod(getMethodName(field, GET_METHOD_PREFIX), EMPTY_CLASS);
        } catch (NoSuchMethodException e) {
            try {
                return c.getMethod(getMethodName(field, IS_METHOD_PREFIX), EMPTY_CLASS);
            } catch (NoSuchMethodException e1) {
                //log.info("getter method not found : " + field);
                return null;
            }
        }
    }

    public static Object getValue(Object obj, String field) {
        Method getter = getGetterMethod(obj.getClass(), field);

        try {
            return getter.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    public static void copyFromTo(Object from, Object to, String... fieldNames) {
        for (String field : fieldNames) {
            copyFromTo(from, to, field);
        }
    }

    public static void copyFromTo(Object from, Object to, String fieldName) {

        if (from == null || to == null) {
            log.info("object deep copy : from or to is null ");
            return;
        }

        try {
            Method getter = getGetterMethod(from.getClass(), fieldName);

            if (getter == null) {
                //log.info("getter method not found : " + fieldName);
                return;
            }

            Method setter = getSetterMethod(to.getClass(), fieldName, getter.getReturnType());
            if (setter == null) {
                //log.info("setter method not found : " + fieldName);
                return;
            }

            setter.invoke(to, getter.invoke(from, EMPTY_CLASS));

        } catch (IllegalAccessException | InvocationTargetException e) {
            log.info("set method invoke error : " + fieldName);
        }
    }


    private static Object getNewInstance(Class<?> c) {
        try {
            return c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.info("new instance create fail : " + e.getMessage());
            throw new ValidationLibException("new instnace create fail : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    public static String[] getDeclaredFields(Class<?> baseClass) {

        Method[] methods = baseClass.getMethods();

        String[] fieldNames = ArrayUtils.EMPTY_STRING_ARRAY;

        for (Method fm : methods) {
            if ((!fm.getName().startsWith(GET_METHOD_PREFIX) && !fm.getName().startsWith(IS_METHOD_PREFIX)) || fm.getParameterCount() != 0) {
                continue;
            }

            String field = getFieldNameFromMethod(fm);
            fieldNames = (String[]) ArrayUtils.add(fieldNames, field);
        }
        return fieldNames;
    }


    public static Object objDeepCopy(Object from, Object to, String... copyFields) {
        if (from == null || to == null) {
            log.error("object deep copy from or to is null ");
            return to;
        }

        for (String field : copyFields) {
            copyFromTo(from, to, field);
        }

        return to;
    }

    public static <T> T objectDeepCopyWithWhiteList(Object from, Object to, String... copyFields) {
        return (T) to.getClass().cast(objDeepCopy(from, to, copyFields));
    }

    public static boolean isGetterMethod(Method m) {

        String methodName = m.getName();

        if (methodName.equals("getClass")) {
            return false;
        }

        if (!methodName.startsWith(GET_METHOD_PREFIX) && !methodName.startsWith(IS_METHOD_PREFIX)) {
            return false;
        }
        if (m.getParameterCount() > 0) {
            return false;
        }

        return true;
    }

    public static <T> T objectDeepCopyWithBlackList(Object from, Object to, String... blockFields) {

        if (to == null) {
            to = getNewInstance(from.getClass());
        }

        String[] whiteList = ArrayUtils.EMPTY_STRING_ARRAY;

        Method[] methods = from.getClass().getMethods();

        for (Method m : methods) {

            if (!isGetterMethod(m)) {
                continue;
            }

            if (isBlockFieldMethod(m, blockFields)) {
                continue;
            }

            whiteList = (String[]) ArrayUtils.add(whiteList, getFieldNameFromMethod(m));

        }

        return objectDeepCopyWithWhiteList(from, to, whiteList);

    }

    public static <T> T objectDeepCopyWithBlackList(Object from, Object to, Class<?> baseClass, String... blockFields) {

        String[] bFields = getDeclaredFields(baseClass);
        bFields = (String[]) ArrayUtils.addAll(bFields, blockFields);

        return objectDeepCopyWithBlackList(from, to, bFields);
    }

    public static <T> T objectDeepCopyWithBlackList(Object from, Class<T> toClass, String... blockFields) {
        Object to = getNewInstance(toClass);
        return objectDeepCopyWithBlackList(from, to, blockFields);
    }

    public static <T> T objectDeepCopyWithBlackList(Object from, Class<T> toClass, Class<?> baseClass, String... blockFields) {
        Object to = getNewInstance(toClass);
        return objectDeepCopyWithBlackList(from, to, baseClass, blockFields);
    }

    public static Double getObjectSize(Object value) {

        double v = 1;

        if (value instanceof String) {
            v = ((String) value).length();
            if (v < 1) {
                return null;
            }
        } else if (ValidationObjUtil.isNumberObj(value)) {
            v = Double.valueOf(value + "");
        } else if (value instanceof List) {
            v = ((List) value).size();
        }

        return v;
    }

}

