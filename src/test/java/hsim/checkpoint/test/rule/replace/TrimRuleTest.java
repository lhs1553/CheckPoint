package hsim.checkpoint.test.rule.replace;

import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.test.rule.RuleTestUtil;
import hsim.model.CommonReqModel;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * The type Trim rule test.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TrimRuleTest {

    private RuleTestUtil ruleTestUtil = new RuleTestUtil();
    private CommonReqModel obj = new CommonReqModel();
    private ValidationData data = ruleTestUtil.getDefaultValidationData();
    private BasicCheckRule checkType = BasicCheckRule.Trim;

    /**
     * Instantiates a new Trim rule test.
     */
    public TrimRuleTest() {
        this.data.setName("name");
        this.data.setTypeClass(String.class);

        ValidationRule rule = data.getValidationRules().stream().filter(r -> r.getRuleName().equals(checkType.name())).findAny().get();
        rule.setUse(true);
    }

    /**
     * Test fail 1.
     */
    @Test
    public void test_fail_1() {
        obj.setName("taeon");
        ruleTestUtil.getMsgChecker().checkDataInnerRules(this.data, this.obj);
        Assert.assertEquals(obj.getName(), "taeon");
    }

    /**
     * Test fail 2.
     */
    @Test
    public void test_fail_2() {
        obj.setName(null);
        ruleTestUtil.getMsgChecker().checkDataInnerRules(this.data, this.obj);
        Assert.assertEquals(obj.getName(), null);
    }

    /**
     * Test success 1.
     */
    @Test
    public void test_success_1() {
        obj.setName("hsim  ");
        ruleTestUtil.getMsgChecker().checkDataInnerRules(this.data, this.obj);
        Assert.assertEquals(obj.getName(), "hsim");
    }

    /**
     * Test success 2.
     */
    @Test
    public void test_success_2() {
        obj.setName("    taeon   ");
        ruleTestUtil.getMsgChecker().checkDataInnerRules(this.data, this.obj);
        Assert.assertEquals(obj.getName(), "taeon");
    }

}
