package hsim.checkpoint.core.domain;

import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.annotation.ValidationParam;
import hsim.checkpoint.core.component.DetailParam;
import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.type.ParamType;
import hsim.checkpoint.util.ValidationHttpUtil;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;

@ToString
@Getter
@Slf4j
public class BasicCheckInfo {

    private ParamType paramType;
    private ReqUrl reqUrl;
    private DetailParam detailParam;
    private String url;
    private String method;
    private HttpServletRequest req;
    private MethodParameter parameter;
    private String body;

    public BasicCheckInfo(HttpServletRequest httpReq, MethodParameter param) {
        this.req = httpReq;
        this.parameter = param;

        this.paramType = this.parameter.getParameterAnnotation(ValidationBody.class) != null ? ParamType.BODY : ParamType.QUERY_PARAM;

        this.detailParam = new DetailParam(parameter.getParameter(), parameter.getMethod(), null);
        this.url = this.paramType.equals(ParamType.BODY) ?
                parameter.getParameterAnnotation(ValidationBody.class).url() : parameter.getParameterAnnotation(ValidationParam.class).url();

        this.method = this.req.getMethod();

        this.reqUrl = new ReqUrl(this.url.isEmpty() ? this.req.getRequestURI() : this.url, this.method);

        if (this.paramType.equals(ParamType.BODY)) {
            this.body = ValidationHttpUtil.readBody(this.req);
        }
    }

    public boolean isListBody() {
        return (this.parameter.getParameterType().equals(java.util.List.class));
    }

    public void logging(ValidationConfig config) {
        if (!config.isBodyLogging() || this.body == null) {
            return;
        }

        log.info("[" + this.method + "]::[" + this.url + "] ::");
        log.info(this.body);
    }

}