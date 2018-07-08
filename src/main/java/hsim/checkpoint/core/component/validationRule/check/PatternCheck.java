package hsim.checkpoint.core.component.validationRule.check;

import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class PatternCheck extends BaseValidationCheck {


    @Override
    public boolean check(Object value, Object standradValue) {

        if (value instanceof String) {
            Pattern pattern = Pattern.compile(String.valueOf(standradValue));
            Matcher matcher = pattern.matcher(String.valueOf(value));
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
        throw new ValidationLibException("invalid pattern value : " + param.getName() + " - " + inputValue, HttpStatus.BAD_REQUEST);
    }

}
