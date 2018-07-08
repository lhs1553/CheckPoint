package hsim.lib.core.component.validationRule.replace;

import hsim.lib.core.component.validationRule.check.BaseValidationCheck;
import hsim.lib.core.domain.ValidationData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReplaceTrim extends BaseValidationCheck {


    @Override
    public Object replace(Object value, Object standardValue, ValidationData param) {
        if (value != null && value instanceof String) {
            return ((String) value).trim();
        }
        return null;
    }

}
