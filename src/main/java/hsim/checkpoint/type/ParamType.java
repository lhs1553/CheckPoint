package hsim.checkpoint.type;

import hsim.checkpoint.core.domain.ReqUrl;
import lombok.Getter;

public enum ParamType {
    BODY("@ValidationBody"), QUERY_PARAM("@ValidationParam");

    @Getter
    private String annotationName;

    ParamType(String aName) {
        this.annotationName = aName;
    }

    public String getUniqueKey(ReqUrl reqUrl) {
        return this.getUniqueKey(reqUrl.getUniqueKey());
    }

    public String getUniqueKey(String id) {
        return this.name() + ":" + id;
    }
}
