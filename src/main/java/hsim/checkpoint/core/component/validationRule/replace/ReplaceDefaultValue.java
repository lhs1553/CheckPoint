package hsim.checkpoint.core.component.validationRule.replace;

import hsim.checkpoint.core.component.validationRule.check.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import lombok.NoArgsConstructor;

/**
 * The type Replace default value.
 */
@NoArgsConstructor
public class ReplaceDefaultValue extends BaseValidationCheck {


    private boolean isEmptyString(Object value) {
        return (value instanceof String && ((String) value).isEmpty());
    }

    @Override
    public Object replace(Object value, Object standardValue, ValidationData param) {
        if (value != null && !this.isEmptyString(value)) {
            return null;
        }

        return standardValue;
    }

}
