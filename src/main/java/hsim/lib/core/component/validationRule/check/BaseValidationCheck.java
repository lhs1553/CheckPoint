package hsim.lib.core.component.validationRule.check;

import hsim.lib.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.lib.core.component.validationRule.rule.ValidationRule;
import hsim.lib.core.domain.ValidationData;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class BaseValidationCheck {

    private void callExcpetion(ValidationData param, Object value, Object standardValue, ValidationInvalidCallback cb) {
        if (cb != null) {
            cb.exception(param, value, standardValue);
        } else {
            this.exception(param, value, standardValue);
        }
    }

    private void checkAndReplace(ValidationData param, ValidationRule rule, Object bodyObj, Object standardValue, ValidationInvalidCallback cb) {

        //check
        Object value = param.getValue(bodyObj);
        if ((value != null && standardValue != null) || rule.getAssistType().isNullable()) {
            boolean valid = this.check(value, standardValue);

            if (!valid) {
                this.callExcpetion(param, value, standardValue, cb);
            }

            //replace value
            Object replaceValue = this.replace(value, standardValue, param);
            if (replaceValue != null) {
                param.replaceValue(bodyObj, replaceValue);
            }
        }
    }

    public void checkPoint(ValidationData param, ValidationRule rule, Object bodyObj, Object standardValue, ValidationInvalidCallback cb) {
        this.checkAndReplace(param, rule, bodyObj, standardValue, cb);
    }

    public boolean check(Object inputValue, Object standardValue) {
        return true;
    }

    public Object replace(Object value, Object standardValue, ValidationData param) {
        return null;
    }

    public void exception(ValidationData param, Object inputValue, Object standardValue) {
    }
}
