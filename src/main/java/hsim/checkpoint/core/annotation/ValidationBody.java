package hsim.checkpoint.core.annotation;

import java.lang.annotation.*;


@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidationBody {

    boolean required() default true;

    String url() default "";
}