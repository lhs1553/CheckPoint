package hsim.lib.core.util;

import hsim.lib.core.component.DetailParam;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class AnnotationScanner {

    private List<Class<?>> allClass = null;

    public AnnotationScanner() {
        this.allClass = getDirClassList(new File(ClassLoader.getSystemClassLoader().getResource("").getFile()), null);
    }

    private List<Class<?>> getDirClassList(File dir, String parent) {
        List<Class<?>> classList = new ArrayList<>();
        if (dir.exists()) {
            Arrays.stream(dir.list()).forEach(f -> {
                File file = new File(dir.getPath() + "/" + f);
                if (file.isDirectory()) {
                    classList.addAll(getDirClassList(file, parent == null ? file.getName() : parent + "/" + file.getName()));
                } else {
                    log.info(parent + "/" + file.getName());
                    String filePath = parent + "/" + file.getName();
                    try {
                        classList.add(Class.forName(filePath.replaceAll("/", ".").replaceAll(".class", "")));
                    } catch (ClassNotFoundException e) {
                        log.info("class not found : " + filePath);
                    }
                }
            });
        }
        return classList;
    }


    public List<DetailParam> getParameterFromMethodWithAnnotation(Class<?> parentClass, Method method, Class<?> annotationClass) {
        List<DetailParam> params = new ArrayList<>();
        if (method.getParameterCount() < 1) {
            return params;
        }

        for (Parameter param : method.getParameters()) {

            Annotation[] annotations = param.getAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(annotationClass)) {
                    params.add(new DetailParam(param, method, parentClass));
                    break;
                }
            }
        }
        return params;
    }

    public List<DetailParam> getParameterFromClassWithAnnotation(Class<?> baseClass, Class<?> annotationClass) {
        List<DetailParam> params = new ArrayList<>();
        Arrays.stream(baseClass.getDeclaredMethods()).forEach(method -> params.addAll(this.getParameterFromMethodWithAnnotation(baseClass, method, annotationClass)));
        return params;
    }

    public List<DetailParam> getParameterWithAnnotation(Class<?> annotation) {
        List<DetailParam> params = new ArrayList<>();
        this.allClass.stream().forEach(cla -> params.addAll(this.getParameterFromClassWithAnnotation(cla, annotation)));
        return params;

    }
}
