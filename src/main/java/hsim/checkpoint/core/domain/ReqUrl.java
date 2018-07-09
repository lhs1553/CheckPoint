package hsim.checkpoint.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReqUrl{
    private String url;
    private String method;

    public ReqUrl(String pUrl, String pMethod) {
        this.url = pUrl;
        this.method = pMethod;
    }

    public String getUniqueKey() {
        return method + ":" + url;
    }

    public String getSheetName(int idx){
        String name=  method + "|" + url.replace("/" , "|");
        if(name.length() > 30){
            return name.substring(0, 29) + idx;
        }
        return name;
    }

}