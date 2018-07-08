package hsim.lib.core.helper;


import com.fasterxml.jackson.databind.ObjectMapper;
import hsim.lib.core.component.ComponentMap;
import hsim.lib.core.msg.MsgChecker;
import hsim.lib.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.lib.core.component.validationRule.check.BaseValidationCheck;
import hsim.lib.core.component.validationRule.rule.AssistType;
import hsim.lib.core.component.validationRule.rule.ValidationRule;
import hsim.lib.core.component.validationRule.type.BasicCheckRule;
import hsim.lib.core.component.validationRule.type.StandardValueType;
import hsim.lib.core.config.ValidationConfig;
import hsim.lib.core.interceptor.ValidationResolver;
import hsim.lib.core.repository.ValidationDataRepository;
import hsim.lib.core.store.ValidationRuleStore;

public class CheckPointHelper {

    private ValidationResolver validationResolver = ComponentMap.get(ValidationResolver.class);
    private ValidationRuleStore validationRuleStore = ComponentMap.get(ValidationRuleStore.class);
    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationConfig validationConfig = ComponentMap.get(ValidationConfig.class);
    private MsgChecker msgChecker = ComponentMap.get(MsgChecker.class);

    public CheckPointHelper replaceObjectMapper(ObjectMapper objectMapper) {
        this.validationResolver.replaceObjectMapper(objectMapper);
        return this;
    }

    public CheckPointHelper replaceExceptionCallback(BasicCheckRule checkRule,  ValidationInvalidCallback cb){
        this.msgChecker.replaceCallback(checkRule, cb);
        return this;
    }

    public CheckPointHelper addValidationRule(String ruleName, StandardValueType standardValueType, BaseValidationCheck validationCheck, AssistType assistType) {
        ValidationRule rule = new ValidationRule(ruleName, standardValueType, validationCheck);
        if (assistType == null) {
            assistType = AssistType.all();
        }
        rule.setAssistType(assistType);
        this.validationRuleStore.addRule(rule);
        return this;
    }

    public CheckPointHelper flush() {
        this.validationDataRepository.flush();
        return this;
    }

    public ValidationConfig getConfig(){
        return this.validationConfig;
    }

}
