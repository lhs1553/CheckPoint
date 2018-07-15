package hsim.checkpoint.core.msg;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.domain.BasicCheckInfo;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.store.ValidationRuleStore;
import hsim.checkpoint.core.store.ValidationStore;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MsgChecker {

    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);
    private ValidationRuleStore validationRuleStore = ComponentMap.get(ValidationRuleStore.class);

    private Map<String, ValidationInvalidCallback> callbackMap = new HashMap<>();

    public MsgChecker replaceCallback(BasicCheckRule type, ValidationInvalidCallback cb) {
        this.callbackMap.put(type.name(), cb);
        return this;
    }

    public void checkDataInnerRules(ValidationData data, Object bodyObj) {
        data.getValidationRules().stream().filter(vr -> vr.isUse()).forEach(rule -> {
            this.validationRuleStore.getValidationChecker(rule).checkPoint(data, rule, bodyObj, rule.getStandardValue(), this.callbackMap.get(rule.getRuleName()));
        });
    }

    public void checkRequest(BasicCheckInfo basicCheckInfo, Object bodyObj) {
        String key = basicCheckInfo.getUniqueKey();
        log.info("key : " + key);

        List<ValidationData> checkData = this.validationStore.getValidationDatas(basicCheckInfo.getParamType(), key);

        if (checkData == null || checkData.isEmpty()) {
            return;
        }
        checkData.stream().forEach(data -> {
            log.info("check data : " + data.toString());
            this.checkDataInnerRules(data, bodyObj);
        });
    }
}
