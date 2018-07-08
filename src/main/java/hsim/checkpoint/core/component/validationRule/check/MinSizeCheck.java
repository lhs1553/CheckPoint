package hsim.checkpoint.core.component.validationRule.check;

import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.util.ValidationObjUtil;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class MinSizeCheck extends BaseValidationCheck {


    @Override
    public boolean check(Object value, Object standardValue) {

        Double v = ValidationObjUtil.getObjectSize(value);
        if (v == null) {
            return true;
        }
        Double doubleSValue = Double.valueOf(String.valueOf(standardValue));

        if (v < doubleSValue) {
            return false;
        }

        return true;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
        throw new ValidationLibException("Parameter : " + param.getName() + " value is too small(minimum :" + standardValue + ")", HttpStatus.BAD_REQUEST);
    }

}
