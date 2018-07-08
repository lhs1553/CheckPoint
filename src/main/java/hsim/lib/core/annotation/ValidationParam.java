package hsim.lib.core.annotation;

import java.lang.annotation.*;


@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidationParam {

    boolean required() default true;

    String charset() default "UTF-8";

    String url() default "";
}