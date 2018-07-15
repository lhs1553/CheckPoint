package hsim.checkpoint.core.domain;

import hsim.checkpoint.core.component.DetailParam;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.sort.RuleSorter;
import hsim.checkpoint.exception.ValidationLibException;
import hsim.checkpoint.type.ParamType;
import hsim.checkpoint.util.ValidationObjUtil;
import hsim.checkpoint.util.excel.TypeCheckUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Slf4j
public class ValidationData {

    private Long id;
    private String url;
    private String method;

    private ParamType paramType;

    private Long parentId;
    private String name;

    private int deepLevel;

    private String type;
    private Class<?> typeClass;

    private boolean number;
    private boolean obj;
    private boolean enumType;
    private boolean list;

    private String methodKey;
    private String parameterKey;

    @Getter(AccessLevel.PRIVATE)
    private ValidationData parent;

    private List<ValidationRule> validationRules = new ArrayList<>();


    public ValidationData(DetailParam detailParam, ParamType paramType, ReqUrl reqUrl, ValidationData parent, Field field, int deepLevel) {

        this.method = reqUrl.getMethod();
        this.url = reqUrl.getUrl();
        this.paramType = paramType;
        this.parentId = parent == null ? null : parent.getId();
        this.deepLevel = deepLevel;

        this.updateField(field);
        this.updateKey(detailParam);
    }

    public void initValidationRuleList(List<ValidationRule> list, boolean refresh) {
        if (this.validationRules == null) {
            this.validationRules = list;
        }

        List<ValidationRule> refreshlist = new ArrayList<>();

        list.forEach(rule -> {
            ValidationRule existRule = this.validationRules.stream().filter(vr -> vr.getRuleName().equals(rule.getRuleName())).findAny().orElse(null);
            if (existRule != null) {
                existRule.updateRuleBasicInfo(rule);
                refreshlist.add(existRule);
            } else {
                this.validationRules.add(rule);
                refreshlist.add(rule);
            }
        });

        if (refresh) {
            this.validationRules = refreshlist;
        }

        this.validationRules.sort(new RuleSorter());
    }


    public boolean equalUrl(ReqUrl url) {
        return url.getMethod().equalsIgnoreCase(this.method) && url.getUrl().equalsIgnoreCase(this.url);
    }

    private Object castSetValue(ValidationData param, Object value) {
        try {
            Method valueof = param.getTypeClass().getMethod("valueOf", String.class);
            try {
                return valueof.invoke(null, value.toString());
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.info("value of invoke error : " + param.getName());
                return value;
            }
        } catch (NoSuchMethodException e) {
            return value;
        }
    }

    private void setter(ValidationData param, Object bodyObj, Object setValue) {

        Method setter = ValidationObjUtil.getSetterMethodNotCheckParamType(bodyObj.getClass(), param.getName());
        if (setter == null) {
            log.error("setter method is null :" + param.getName());
            return;
        }
        try {
            setter.invoke(bodyObj, this.castSetValue(param, setValue));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ValidationLibException("setter fail: " + param.getName(), HttpStatus.BAD_REQUEST);
        }
    }

    public void replaceValue(Object bodyObj, Object replaceValue) {
        Object parentObj = bodyObj;
        if (this.parentId != null) {
            parentObj = this.parent.getValue(bodyObj);
        }

        this.setter(this, parentObj, replaceValue);
    }

    private Object getter(ValidationData param, Object bodyObj) {

        Method getter = ValidationObjUtil.getGetterMethod(bodyObj.getClass(), param.getName());

        if (getter == null) {
            log.error("getter method is null :" + param.getName());
            return null;
        }

        try {
            return getter.invoke(bodyObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ValidationLibException("mandatory Param is invalid : " + param.getName(), HttpStatus.BAD_REQUEST);
        }
    }

    private Object getParentObj(ValidationData child, Object bodyObj) {
        if (child.getParent() == null) {
            return this.getter(child, bodyObj);
        }
        return this.getter(child, this.getParentObj(child.getParent(), bodyObj));
    }

    public Object getValue(Object bodyObj) {

        return this.getParentObj(this, bodyObj);
    }


    public ValidationData updateUserData(ValidationData data) {
        if (data == null) {
            return this;
        }
        return ValidationObjUtil.objectDeepCopyWithBlackList(data, this, "name", "parent", "type", "deepLevel");
    }

    public ValidationData updateKey(DetailParam detailParam) {
        if (detailParam == null) {
            return this;
        }
        this.methodKey = detailParam.getMethodKey();
        this.parameterKey = detailParam.getParameterKey();

        return this;
    }

    public void updateField(Field field) {
        this.name = field.getName();
        this.obj = TypeCheckUtil.isObjClass(field);
        this.list = TypeCheckUtil.isListClass(field);
        this.number = TypeCheckUtil.isNumberClass(field);
        this.enumType = field.getType().isEnum();
        this.typeClass = field.getType();
        this.type = field.getType().getSimpleName();
    }

    public boolean isQueryParam() {
        if (this.paramType == null) {
            return false;
        }
        return this.paramType.equals(ParamType.QUERY_PARAM);
    }

    public boolean isBody() {
        if (this.paramType == null) {
            return false;
        }
        return this.paramType.equals(ParamType.BODY);
    }

}
