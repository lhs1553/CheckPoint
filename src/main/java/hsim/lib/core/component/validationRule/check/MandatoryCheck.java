package hsim.lib.core.component.validationRule.check;

import hsim.lib.core.domain.ValidationData;
import hsim.lib.exception.ValidationLibException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class MandatoryCheck extends BaseValidationCheck {


    @Override
    public boolean check(Object value, Object standardValue) {

        if (value == null) {
            return false;
        }
        String sv = String.valueOf(value);
        return !sv.isEmpty();
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
        throw new ValidationLibException("Mandatory field is null ( " + param.getName() + ") ", HttpStatus.BAD_REQUEST);
    }
}
