package hsim.checkpoint.core.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;

@Slf4j
public class ComponentMap {

    private static Object[] objs = {};

    public synchronized static <T> T get(Class<T> cType) {
        for (Object obj : objs) {
            if (obj.getClass().equals(cType)) {
                return cType.cast(obj);
            }
        }
        try {
            Object obj = cType.newInstance();
            log.info("[CREATE INSTANCE] : " + cType.getSimpleName());
            objs = ArrayUtils.add(objs, obj);
            return cType.cast(obj);
        } catch (InstantiationException | IllegalAccessException e) {
            log.info(e.getMessage());
        }
        return null;
    }

}
