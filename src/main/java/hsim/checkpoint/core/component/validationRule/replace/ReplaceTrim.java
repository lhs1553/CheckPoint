package hsim.checkpoint.core.component.validationRule.replace;

import hsim.checkpoint.core.component.validationRule.check.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReplaceTrim extends BaseValidationCheck {


    @Override
    public Object replace(Object value, Object standardValue, ValidationData param) {
        if (value != null && value instanceof String) {
            String str = (String)value;
            return str.trim();
        }
        return null;
    }

}
