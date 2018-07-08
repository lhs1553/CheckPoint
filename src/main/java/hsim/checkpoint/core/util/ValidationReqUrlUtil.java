package hsim.checkpoint.core.util;

import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationReqUrlUtil {

    public static List<ReqUrl> getUrlListFromValidationDatas(List<ValidationData> datas) {
        Map<String, ReqUrl> urlMap = new HashMap<>();
        datas.stream().forEach(data -> {
            ReqUrl url = new ReqUrl(data.getUrl(), data.getMethod());
            urlMap.put(url.getUniqueKey(), url);
        });

        return urlMap.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }
}
