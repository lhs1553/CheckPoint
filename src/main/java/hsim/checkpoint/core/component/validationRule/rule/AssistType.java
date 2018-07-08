package hsim.checkpoint.core.component.validationRule.rule;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AssistType {
    private boolean nullable;
    private boolean string;
    private boolean number;
    private boolean enumType;
    private boolean list;
    private boolean obj;

    public static AssistType all() {
        AssistType assistType = new AssistType();
        assistType.nullable = true;
        assistType.string = true;
        assistType.number = true;
        assistType.enumType = true;
        assistType.list = true;
        assistType.obj = true;
        return assistType;
    }

    public AssistType nullable() {
        this.nullable = true;
        return this;
    }

    public AssistType string() {
        this.string = true;
        return this;
    }

    public AssistType number() {
        this.number = true;
        return this;
    }

    public AssistType enumType() {
        this.enumType = true;
        return this;
    }

    public AssistType list() {
        this.list = true;
        return this;
    }

    public AssistType obj() {
        this.obj = true;
        return this;
    }
}
