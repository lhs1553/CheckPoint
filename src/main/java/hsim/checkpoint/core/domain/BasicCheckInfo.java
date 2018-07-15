package hsim.checkpoint.core.domain;

import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.component.DetailParam;
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
    private HttpServletRequest req;
    private MethodParameter parameter;
    private String body;

    public BasicCheckInfo(HttpServletRequest httpReq, MethodParameter param, boolean log) {
        this.req = httpReq;
        this.parameter = param;

        this.paramType = this.parameter.getParameterAnnotation(ValidationBody.class) != null ? ParamType.BODY : ParamType.QUERY_PARAM;

        this.detailParam = new DetailParam(parameter.getParameter(), parameter.getMethod(), null);
        this.reqUrl = new ReqUrl(this.req);

        if (this.paramType.equals(ParamType.BODY)) {
            this.body = ValidationHttpUtil.readBody(this.req);

            if (log) {
                this.logging();
            }
        }
    }

    public boolean isUrlMapping() {
        if (this.detailParam == null) {
            return false;
        }
        return this.detailParam.isUrlMapping();
    }

    public boolean isListBody() {
        return (this.parameter.getParameterType().equals(java.util.List.class));
    }

    public void logging() {
        if (this.body == null) {
            return;
        }

        log.info("[" + this.reqUrl.getMethod() + "]::[" + this.reqUrl.getUrl() + "] ::");
        log.info(this.body);
    }

    public String getUniqueKey() {
        return this.isUrlMapping() ? this.reqUrl.getUniqueKey() : this.detailParam.getMethodKey();
    }

}
