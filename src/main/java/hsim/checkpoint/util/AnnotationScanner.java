package hsim.checkpoint.util;

import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.component.DetailParam;
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

    private ValidationConfig validationConfig = ComponentMap.get(ValidationConfig.class);

    private List<Class<?>> allClass = null;

    private File getClassRootDirectory(){
        File hsim = new File(ClassLoader.getSystemClassLoader().getResource("hsim").getFile());
        return hsim.getParentFile();
    }

    public AnnotationScanner() {
        if(!this.validationConfig.isScanAnnotation()){ return; }
        this.allClass = getDirClassList(this.getClassRootDirectory(), null);
    }

    private List<Class<?>> getDirClassList(File dir, String parent) {
        List<Class<?>> classList = new ArrayList<>();
        if (dir.exists()) {
            Arrays.stream(dir.list()).forEach(f -> {
                File file = new File(dir.getPath() + "/" + f);
                if (file.isDirectory()) {
                    classList.addAll(getDirClassList(file, parent == null ? file.getName() : parent + "/" + file.getName()));
                } else {
                    String filePath = parent + "/" + file.getName();
                    log.info(filePath);
                    log.info(filePath);
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
