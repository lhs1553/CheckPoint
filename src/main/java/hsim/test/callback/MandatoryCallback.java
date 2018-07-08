package hsim.test.callback;

import hsim.checkpoint.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import org.springframework.http.HttpStatus;

public class MandatoryCallback implements ValidationInvalidCallback {
    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
        throw  new ValidationLibException(param.getName() +"필드가 누락 되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
