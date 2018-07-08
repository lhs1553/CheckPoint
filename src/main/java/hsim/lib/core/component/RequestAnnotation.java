package hsim.lib.core.component;

import hsim.lib.core.util.AnnotationUtil;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

public class RequestAnnotation {

    private RequestMapping requestMapping;
    private PostMapping postMapping;
    private PutMapping putMapping;
    private DeleteMapping deleteMapping;
    private GetMapping getMapping;

    public RequestAnnotation(Method method) {
        this.requestMapping = (RequestMapping) AnnotationUtil.getAnnotation(method.getAnnotations(), RequestMapping.class);
        this.postMapping = (PostMapping) AnnotationUtil.getAnnotation(method.getAnnotations(), PostMapping.class);
        this.putMapping = (PutMapping) AnnotationUtil.getAnnotation(method.getAnnotations(), PutMapping.class);
        this.deleteMapping = (DeleteMapping) AnnotationUtil.getAnnotation(method.getAnnotations(), DeleteMapping.class);
        this.getMapping = (GetMapping) AnnotationUtil.getAnnotation(method.getAnnotations(), GetMapping.class);

    }

    public RequestMethod[] getMethod() {
        if (this.requestMapping != null) {
            return this.requestMapping.method();
        }
        if (this.postMapping != null) {
            return new RequestMethod[]{(RequestMethod.POST)};
        }
        if (this.putMapping != null) {
            return new RequestMethod[]{(RequestMethod.PUT)};
        }
        if (this.deleteMapping != null) {
            return new RequestMethod[]{(RequestMethod.DELETE)};
        }
        if (this.getMapping != null) {
            return new RequestMethod[]{(RequestMethod.GET)};
        }
        return null;
    }

    public String[] getValue() {
        if (this.requestMapping != null) {
            return this.requestMapping.value();
        }
        if (this.postMapping != null) {
            return this.postMapping.value();
        }
        if (this.putMapping != null) {
            return this.putMapping.value();
        }
        if (this.deleteMapping != null) {
            return this.deleteMapping.value();
        }
        if (this.getMapping != null) {
            return this.getMapping.value();
        }
        return null;
    }
}
