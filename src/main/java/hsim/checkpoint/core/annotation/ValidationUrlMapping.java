package hsim.checkpoint.core.annotation;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidationUrlMapping {

    boolean required() default true;
}