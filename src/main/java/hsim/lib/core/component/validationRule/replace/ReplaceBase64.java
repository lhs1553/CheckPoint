package hsim.lib.core.component.validationRule.replace;

import hsim.lib.core.component.validationRule.check.BaseValidationCheck;
import hsim.lib.core.domain.ValidationData;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Slf4j
@NoArgsConstructor
public class ReplaceBase64 extends BaseValidationCheck {


    @Override
    public Object replace(Object value, Object standardValue, ValidationData param) {
        if (value != null && value instanceof String) {
            try {
                return new String(Base64.getDecoder().decode((String) value), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.info("base64 decode fail ( " + param.getName() + " ) :" + value);
                return null;
            }
        }
        return null;
    }

}
