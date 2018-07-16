package hsim.checkpoint.config;

import hsim.checkpoint.interceptor.ValidationResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * The type Validation intercepter config.
 */
public class ValidationIntercepterConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ValidationResolver());
    }
}
