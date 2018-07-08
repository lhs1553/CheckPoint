package hsim.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hsim on 2017. 8. 13...
 */
@Configuration
@Slf4j
public class JacksonConfig {

    private static final boolean FAIL_ON_UNKNOWN_PROPERTIES = false;
    private static final boolean READ_UNKNOWN_ENUM_VALUE_AS_NULL = true;
    private static final boolean FAIL_ON_EMPTY_BEANS = false;

    @Autowired
    public void settingToObjectMapper(ObjectMapper mapper) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, this.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, this.READ_UNKNOWN_ENUM_VALUE_AS_NULL);

        mapper.setSerializationInclusion(Include.NON_NULL);

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, this.FAIL_ON_EMPTY_BEANS);
    }

    @Autowired
    private void logging() {

        log.info("---------------------ObjectMapper Setting-----------------------");
        log.info("SerializationFeature.INCLUDE: " + Include.NON_NULL);

        log.info("DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES : " + this.FAIL_ON_UNKNOWN_PROPERTIES);
        log.info("DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL : " + this.READ_UNKNOWN_ENUM_VALUE_AS_NULL);

        log.info("DeserializationFeature.FAIL_ON_EMPTY_BEANS : " + this.FAIL_ON_EMPTY_BEANS);
        log.info("---------------------ObjectMapper Setting-----------------------\n");
    }

}


