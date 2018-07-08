package hsim.checkpoint.core.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class AnnotationUtil {

    public static Annotation getAnnotation(Annotation[] annotations, Class<?> annotationClass) {
        return Arrays.stream(annotations).filter(annotation -> annotation.annotationType().equals(annotationClass)).findFirst().orElse(null);
    }
}
