package hsim.checkpoint.core.msg;

import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.annotation.ValidationParam;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.component.DetailParam;
import hsim.checkpoint.core.config.ValidationConfig;
import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationStore;
import hsim.checkpoint.core.type.ParamType;
import hsim.checkpoint.core.util.AnnotationScanner;
import hsim.checkpoint.core.util.excel.TypeCheckUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

@Slf4j
public class MsgSaver {

    private AnnotationScanner annotationScanner = ComponentMap.get(AnnotationScanner.class);
    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);
    private ValidationConfig validationConfig = ComponentMap.get(ValidationConfig.class);

    public MsgSaver() {
    }

    public void annotationScan(){
        List<DetailParam> annoParams = this.annotationScanner.getParameterWithAnnotation(ValidationParam.class);
        this.initDetailParams(ParamType.QUERY_PARAM, annoParams);
        List<DetailParam> annoBody = this.annotationScanner.getParameterWithAnnotation(ValidationBody.class);
        this.initDetailParams(ParamType.BODY, annoBody);
    }


    private void initDetailParams(ParamType paramType, List<DetailParam> params) {
        params.forEach(param -> {
            List<ReqUrl> urls = param.getReqUrls();
            urls.forEach(url -> {
                this.saveParameter(param, paramType, url, null, param.getParameter().getType(), 0);
            });
        });
    }

    public void urlCheckAndSave(ParamType paramType, ReqUrl reqUrl, Class<?> type) {
        if (!this.validationConfig.isFreshUrlSave()) {
            return;
        }

        List<ValidationData> datas = this.validationDataRepository.findByParamTypeAndUrlAndMethod(paramType, reqUrl.getUrl(), reqUrl.getMethod());
        if (datas.isEmpty()) {
            this.saveParameter(null, paramType, reqUrl, null, type, 0);
        }
        this.validationDataRepository.flush();
        this.validationStore.refresh();
    }

    private ValidationData getDefaultvalidationData(DetailParam detailParam, ParamType paramType, ReqUrl reqUrl, ValidationData parent, Field field, int deepLevel) {

        ValidationData param = new ValidationData();

        param.setMethod(reqUrl.getMethod());
        param.setUrl(reqUrl.getUrl());
        param.setParamType(paramType);
        param.setParentId(parent == null ? null : parent.getId());
        param.setName(field.getName());
        param.setDeepLevel(deepLevel);
        param.updateType(field.getType());
        param.setObj(TypeCheckUtil.isObjClass(field));
        param.setList(TypeCheckUtil.isListClass(field));
        param.setNumber(TypeCheckUtil.isNumberClass(field));
        param.setEnumType(field.getType().isEnum());
        param.updateKey(detailParam);

        return param;
    }


    private void saveParameter(DetailParam detailParam, ParamType paramType, ReqUrl reqUrl, ValidationData parent, Class<?> type, int deepLevel) {
        for (Field field : type.getDeclaredFields()) {
            ValidationData param = this.validationDataRepository.findByParamTypeAndUrlAndMethodAndNameAndParentId(paramType, reqUrl.getUrl(), reqUrl.getMethod(), field.getName(), parent == null ? null : parent.getId());
            if (param == null) {
                param = this.getDefaultvalidationData(detailParam, paramType, reqUrl, parent, field, deepLevel);
            } else {
                param.updateKey(detailParam);
            }

            param = this.validationDataRepository.save(param);

            if (param.isObj()) {
                this.saveParameter(detailParam, paramType, reqUrl, param, param.getTypeClass(), deepLevel + 1);
            }
        }
    }

}
