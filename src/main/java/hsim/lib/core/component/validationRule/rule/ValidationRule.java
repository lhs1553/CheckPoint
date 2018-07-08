package hsim.lib.core.component.validationRule.rule;

import hsim.lib.core.component.validationRule.check.BaseValidationCheck;
import hsim.lib.core.component.validationRule.type.BasicCheckRule;
import hsim.lib.core.component.validationRule.type.StandardValueType;
import hsim.lib.core.domain.ValidationData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ValidationRule {
    private int orderIdx;
    private String ruleName;
    private boolean use;
    private boolean parentDependency;
    private String overlapBanRuleName;
    private Object standardValue;
    private StandardValueType standardValueType;
    private BaseValidationCheck validationCheck;

    private AssistType assistType;

    public ValidationRule(BasicCheckRule rule, StandardValueType standardValueType, BaseValidationCheck checker) {
        this.ruleName = rule.name();
        this.standardValueType = standardValueType;
        this.validationCheck = checker;
    }
    public ValidationRule(String rName, StandardValueType standardValueType, BaseValidationCheck checker) {
        this.ruleName = rName;
        this.standardValueType = standardValueType;
        this.validationCheck = checker;
    }

    public void updateRuleBasicInfo(ValidationRule rule) {
        this.orderIdx = rule.orderIdx;
        this.parentDependency = rule.parentDependency;
        this.standardValueType = rule.standardValueType;
        this.validationCheck = rule.validationCheck;
        this.overlapBanRuleName = rule.overlapBanRuleName;
        this.assistType = rule.assistType;
    }

    public boolean isUse() {
        if (!this.use) {
            return this.use;
        }

        return !(this.standardValueType != null && !this.standardValueType.equals(StandardValueType.NONE) && this.standardValue == null);
    }

    public ValidationRule assistType(AssistType assistType1) {
        this.assistType = assistType1;
        return this;
    }

    public ValidationRule overlapBanRule(BasicCheckRule rule) {
        this.overlapBanRuleName = rule.name();
        return this;
    }
    public ValidationRule overlapBanRule(String ruleName) {
        this.overlapBanRuleName = ruleName;
        return this;
    }


    public ValidationRule overlapBanRule(ValidationRule banRule) {
        if (banRule != null) {
            this.overlapBanRuleName = banRule.getRuleName();
        }
        return this;
    }

    public ValidationRule parentDependency() {
        this.parentDependency = true;
        return this;
    }


    public boolean isUsedMyRule(ValidationData item) {
        List<ValidationRule> usedRules = item.getValidationRules();
        if (usedRules == null || usedRules.isEmpty()) {
            return false;
        }
        return usedRules.stream().filter(ur -> ur.getRuleName().equals(this.ruleName) && ur.isUse()).findAny().orElse(null) != null;
    }

    public List<ValidationData> filter(List<ValidationData> allList) {
        return allList.stream().filter(vd -> this.isUsedMyRule(vd)).collect(Collectors.toList());
    }

}
