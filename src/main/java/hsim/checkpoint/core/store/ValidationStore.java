package hsim.checkpoint.core.store;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.type.MsgCheckType;
import hsim.checkpoint.type.ParamType;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ValidationStore {

    private List<ValidationData> allList;
    private Map<String, ReqUrl> urlMap;
    private Map<String, List<ValidationData>> validationDataRuleListMap;
    private ValidationConfig validationConfig = ComponentMap.get(ValidationConfig.class);

    private ValidationDataRepository repository = ComponentMap.get(ValidationDataRepository.class);

    public ValidationStore(){ }

    private void instanceInit() {
        urlMap = new HashMap<>();
        validationDataRuleListMap = new HashMap<>();
    }

    private List<ValidationData> getMatchedValidaitonRule(ParamType paramType, ReqUrl reqUrl) {
        return allList.stream().filter(d -> d.getParamType().equals(paramType) && d.equalUrl(reqUrl)).collect(Collectors.toList());
    }

    private void validaitonDataEmptyFilter(ValidationData data) {
        data.setValidationRules(data.getValidationRules().stream().filter(vr -> vr.isUse()).collect(Collectors.toList()));
    }

    private void validationDataInit() {

        allList = this.repository.findAll(false);

        allList.stream().forEach(data -> {
            ReqUrl url = new ReqUrl(data.getUrl(), data.getMethod());
            if (this.validationConfig.getMsgCheckType().equals(MsgCheckType.URL)) {
                urlMap.put(url.getUniqueKey(), url);
            } else {
                urlMap.put(data.getMethodKey(), url);
            }
        });

        allList.forEach(this::validaitonDataEmptyFilter);
        allList = allList.stream().filter(vd -> !vd.getValidationRules().isEmpty()).collect(Collectors.toList());

        urlMap.entrySet().stream().forEach(entry -> {
            ReqUrl reqUrl = entry.getValue();

            validationDataRuleListMap.put(ParamType.BODY.getUniqueKey(reqUrl), this.getMatchedValidaitonRule(ParamType.BODY, reqUrl));
            validationDataRuleListMap.put(ParamType.QUERY_PARAM.getUniqueKey(reqUrl), this.getMatchedValidaitonRule(ParamType.QUERY_PARAM, reqUrl));
        });
    }

    public void refresh() {
        this.instanceInit();
        this.validationDataInit();
    }

    public List<ValidationData> getValidationDatas(ParamType paramType, String key) {
        if (this.urlMap == null || this.validationDataRuleListMap == null) {
            refresh();
        }

        if (key == null || paramType == null) {
            throw new ValidationLibException("any parameter is null", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ReqUrl reqUrl = urlMap.get(key);
        if (reqUrl == null) {
            log.info("reqUrl is null :" + key);
            return null;
        }

        return validationDataRuleListMap.get(paramType.getUniqueKey(reqUrl.getUniqueKey()));
    }


}
