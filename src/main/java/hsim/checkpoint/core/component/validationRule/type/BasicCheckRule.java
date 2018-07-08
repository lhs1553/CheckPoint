package hsim.checkpoint.core.component.validationRule.type;

import lombok.Getter;

@Getter
public enum BasicCheckRule {
    Mandatory, BlackList, WhiteList, MinSize, MaxSize, DefaultValue, Base64, Trim, Pattern
}
