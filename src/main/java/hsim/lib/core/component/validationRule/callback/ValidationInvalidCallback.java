package hsim.lib.core.component.validationRule.callback;

import hsim.lib.core.domain.ValidationData;

public interface ValidationInvalidCallback {

    void exception(ValidationData param, Object inputValue, Object standardValue);
}
