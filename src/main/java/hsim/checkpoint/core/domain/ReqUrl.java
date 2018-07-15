package hsim.checkpoint.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Setter
@Getter
@NoArgsConstructor
public class ReqUrl {
    private String url;
    private String method;
    private boolean urlMapping;

    public ReqUrl(String method, String url) {
        this.method = method;
        this.url = url;
    }

    public ReqUrl(ValidationData validationData) {
        this.method = validationData.getMethod();
        this.url = validationData.getUrl();
        this.urlMapping = validationData.isUrlMapping();
    }

    public ReqUrl(HttpServletRequest req) {
        this.method = req.getMethod();
        this.url = req.getRequestURI();
    }

    public String getUniqueKey() {
        return method + ":" + url;
    }

    public String getSheetName(int idx) {
        String name = method + "|" + url.replace("/", "|");
        if (name.length() > 30) {
            return name.substring(0, 29) + idx;
        }
        return name;
    }

}
