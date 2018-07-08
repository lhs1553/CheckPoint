package hsim.lib.core.store;

import hsim.lib.core.component.validationRule.rule.ValidationRule;
import hsim.lib.core.domain.ValidationData;
import hsim.lib.core.type.ParamType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ValidationDataGroup {
    private ParamType paramType;
    private List<ValidationRule> rules = new ArrayList<>();

    public ValidationDataGroup(ParamType paramType, List<ValidationData> validationDatas) {
        this.initDataGroup(paramType, validationDatas);
    }


    public void addRule(ValidationData vd) {
        this.rules.addAll(vd.getValidationRules().stream().filter(vr -> vr.isUse()).collect(Collectors.toList()));
    }

    public void initDataGroup(ParamType paramType, List<ValidationData> lists) {
        this.paramType = paramType;

        lists.forEach(vd -> {
            this.addRule(vd);
        });

    }
}
