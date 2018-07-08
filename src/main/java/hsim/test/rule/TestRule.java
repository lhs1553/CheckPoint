package hsim.test.rule;

import hsim.lib.core.component.validationRule.check.BaseValidationCheck;
import hsim.lib.core.domain.ValidationData;
import hsim.lib.exception.ValidationLibException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Slf4j
public class TestRule extends BaseValidationCheck {


    @Override
    public boolean check(Object value, Object standardValue) {

        log.info("value : " + value);
        log.info("standardValue : " + standardValue);

        return true;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
        throw new ValidationLibException("logging", HttpStatus.BAD_REQUEST);
    }

}
