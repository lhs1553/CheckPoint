package hsim.checkpoint.core.component.validationRule.check;

import hsim.checkpoint.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.domain.ValidationData;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Base validation check.
 */
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


        }
        //replace value
        Object replaceValue = this.replace(value, standardValue, param);
        if (replaceValue != null) {
            param.replaceValue(bodyObj, replaceValue);
        }
    }

    /**
     * Check point.
     *
     * @param param         the param
     * @param rule          the rule
     * @param bodyObj       the body obj
     * @param standardValue the standard value
     * @param cb            the cb
     */
    public void checkPoint(ValidationData param, ValidationRule rule, Object bodyObj, Object standardValue, ValidationInvalidCallback cb) {
        this.checkAndReplace(param, rule, bodyObj, standardValue, cb);
    }

    /**
     * Check boolean.
     *
     * @param inputValue    the input value
     * @param standardValue the standard value
     * @return the boolean
     */
    public boolean check(Object inputValue, Object standardValue) {
        return true;
    }

    /**
     * Replace object.
     *
     * @param value         the value
     * @param standardValue the standard value
     * @param param         the param
     * @return the object
     */
    public Object replace(Object value, Object standardValue, ValidationData param) {
        return null;
    }

    /**
     * Exception.
     *
     * @param param         the param
     * @param inputValue    the input value
     * @param standardValue the standard value
     */
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
    }
}
