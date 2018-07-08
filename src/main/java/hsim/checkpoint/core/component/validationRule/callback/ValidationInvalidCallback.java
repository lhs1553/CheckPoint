package hsim.checkpoint.core.component.validationRule.callback;

import hsim.checkpoint.core.domain.ValidationData;

public interface ValidationInvalidCallback {

    void exception(ValidationData param, Object inputValue, Object standardValue);
}
