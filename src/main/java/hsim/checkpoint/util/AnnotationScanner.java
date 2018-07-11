package hsim.checkpoint.util;

import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.component.DetailParam;
import hsim.checkpoint.init.InitCheckPoint;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class AnnotationScanner {

    private ValidationConfig validationConfig = ComponentMap.get(ValidationConfig.class);

    private List<Class<?>> allClass = null;

    private JarFile getJarFile() {
        String jarPath = InitCheckPoint.getJarPath();
        log.info("jar Path :  " + jarPath);

        if (jarPath != null) {
            try {
                return new JarFile(jarPath);
            } catch (IOException e) {
                log.info("jarFile exception : " + e.getMessage());
            }
        }

        return null;
    }

    private File getClassRootDirectory() {
        return new File(ClassLoader.getSystemClassLoader().getResource("").getFile());
    }

    public AnnotationScanner() {
        File rootDir = this.getClassRootDirectory();
        JarFile jarFile = this.getJarFile();

        if (rootDir != null) {
            this.allClass = getDirClassList(rootDir, null);
        } else if (jarFile != null) {
            this.allClass = this.getJarClassList(jarFile);
        }
    }

    private String fromFileToClassName(final String fileName) {
        return fileName.substring(0, fileName.length() - 6).replaceAll("/|\\\\", "\\.");
    }

    private List<Class<?>> getJarClassList(JarFile jarFile) {
        List<Class<?>> classList = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                String className = fromFileToClassName(entry.getName());
                try {
                    classList.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    log.info("class not found : " + entry.getName());
                }
            }
        }
        return classList;
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
                    try {
                        classList.add(Class.forName(this.fromFileToClassName(filePath)));
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
