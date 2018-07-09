package hsim.checkpoint.config;

import hsim.checkpoint.type.MsgCheckType;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

@ToString
@Getter
public class ValidationConfig {

    @Value("${validation.msg.check.type:FUNCTION}")
    private MsgCheckType msgCheckType;

    @Value("${validation.fresh.url.save:true}")
    private boolean freshUrlSave;

    @Value("${validation.msg.check.body.logging:true}")
    private boolean bodyLogging;

    @Value("${validation.setting.password:taeon}")
    private String authToken;

    @Value("${validation.save.max.deeplevel:15}")
    private int maxDeepLevel;

    @Value("${validation.scan.annotation:true}")
    private boolean scanAnnotation;

}
