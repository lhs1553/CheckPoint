package hsim.test.init;

import hsim.lib.core.component.validationRule.rule.AssistType;
import hsim.lib.core.component.validationRule.type.BasicCheckRule;
import hsim.lib.core.component.validationRule.type.StandardValueType;
import hsim.lib.core.helper.CheckPointHelper;
import hsim.test.callback.MandatoryCallback;
import hsim.test.rule.TestRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppInit implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        CheckPointHelper checkPointHelper = new CheckPointHelper();
        checkPointHelper.addValidationRule("ruleTest", StandardValueType.NUMBER, new TestRule(), new AssistType().string()).flush();
        checkPointHelper.replaceExceptionCallback(BasicCheckRule.Mandatory, new MandatoryCallback());
    }
}
