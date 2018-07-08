package hsim.checkpoint.core.store;

import hsim.checkpoint.core.component.validationRule.check.*;
import hsim.checkpoint.core.component.validationRule.replace.ReplaceBase64;
import hsim.checkpoint.core.component.validationRule.replace.ReplaceDefaultValue;
import hsim.checkpoint.core.component.validationRule.replace.ReplaceTrim;
import hsim.checkpoint.core.component.validationRule.rule.AssistType;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.component.validationRule.type.StandardValueType;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ValidationRuleStore {

    @Getter
    private List<ValidationRule> rules;

    public ValidationRuleStore() {
        super();

        this.rules = new ArrayList<>();

        this.addRule(new ValidationRule(BasicCheckRule.Mandatory, StandardValueType.NONE, new MandatoryCheck()).parentDependency().overlapBanRule(BasicCheckRule.DefaultValue).assistType(AssistType.all()));
        this.addRule(new ValidationRule(BasicCheckRule.BlackList, StandardValueType.LIST, new BlackListCheck()).overlapBanRule(BasicCheckRule.WhiteList).assistType(new AssistType().string().enumType()));
        this.addRule(new ValidationRule(BasicCheckRule.WhiteList, StandardValueType.LIST, new WhiteListCheck()).overlapBanRule(BasicCheckRule.BlackList).assistType(new AssistType().string().enumType()));

        this.addRule(new ValidationRule(BasicCheckRule.MinSize, StandardValueType.NUMBER, new MinSizeCheck()).assistType(new AssistType().string().list().number()));
        this.addRule(new ValidationRule(BasicCheckRule.MaxSize, StandardValueType.NUMBER, new MaxSizeCheck()).assistType(new AssistType().string().list().number()));

        this.addRule(new ValidationRule(BasicCheckRule.DefaultValue, StandardValueType.STRING, new ReplaceDefaultValue()).overlapBanRule(BasicCheckRule.Mandatory).assistType(new AssistType().number().string().enumType().nullable()));
        this.addRule(new ValidationRule(BasicCheckRule.Base64, StandardValueType.NONE, new ReplaceBase64()).assistType(new AssistType().string()));
        this.addRule(new ValidationRule(BasicCheckRule.Trim, StandardValueType.NONE, new ReplaceTrim()).assistType(new AssistType().string()));
        this.addRule(new ValidationRule(BasicCheckRule.Pattern, StandardValueType.STRING, new PatternCheck()).assistType(new AssistType().string()));
    }

    public BaseValidationCheck getValidationChecker(ValidationRule rule) {
        ValidationRule existRule = this.rules.stream().filter(r -> r.getRuleName().equals(rule.getRuleName())).findFirst().orElse(null);
        if (existRule == null) {
            throw new ValidationLibException("rulename : " + rule.getRuleName() + "checker is notfound  ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return existRule.getValidationCheck();
    }

    public ValidationRuleStore addRule(ValidationRule... rules) {
        for (ValidationRule rule : rules) {
           this.addRule(rule) ;
        }
        return this;
    }

    public synchronized  ValidationRuleStore addRule(ValidationRule rule) {
        rule.setOrderIdx(this.rules.size());
        this.rules.add(rule);
        return this;
    }


}
