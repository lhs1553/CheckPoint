package hsim.checkpoint.core.component.validationRule.check;

import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * The type Mandatory check.
 */
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
