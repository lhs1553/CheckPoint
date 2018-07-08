package hsim.checkpoint.helper;


import com.fasterxml.jackson.databind.ObjectMapper;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.msg.MsgChecker;
import hsim.checkpoint.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.checkpoint.core.component.validationRule.check.BaseValidationCheck;
import hsim.checkpoint.core.component.validationRule.rule.AssistType;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.component.validationRule.type.StandardValueType;
import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.interceptor.ValidationResolver;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationRuleStore;

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
