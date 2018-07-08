package hsim.lib.core.component.validationRule.check;

import hsim.lib.core.domain.ValidationData;
import hsim.lib.exception.ValidationLibException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@NoArgsConstructor
public class BlackListCheck extends BaseValidationCheck {

    @Override
    public boolean check(Object value, Object standardValue) {
        if (value == null) {
            return true;
        }
        List<String> blackList = (List<String>) standardValue;
        return !blackList.contains(String.valueOf(value).trim());
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
        throw new ValidationLibException(inputValue + " is bad", HttpStatus.NOT_ACCEPTABLE);
    }
}
