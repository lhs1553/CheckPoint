package hsim.checkpoint.core.msg;

import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.annotation.ValidationParam;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.component.DetailParam;
import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationStore;
import hsim.checkpoint.type.MsgCheckType;
import hsim.checkpoint.type.ParamType;
import hsim.checkpoint.util.AnnotationScanner;
import hsim.checkpoint.util.excel.TypeCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class MsgSaver {

    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);
    private ValidationConfig validationConfig = ComponentMap.get(ValidationConfig.class);
    private AnnotationScanner annotationScanner = ComponentMap.get(AnnotationScanner.class);

    public MsgSaver() {
    }


    public void annotationScan(final int maxDeepLeel) {

        Arrays.stream(ParamType.values()).forEach(paramType -> this.initDetailParams(paramType, maxDeepLeel));

        this.validationDataRepository.flush();
        this.validationStore.refresh();
        log.info("[ANNOTATION_SCAN] Complete");
    }

    private void initDetailParams(ParamType paramType, final int maxDeepLevel) {

        List<DetailParam> params = this.annotationScanner.getParameterWithAnnotation(paramType.equals(ParamType.BODY) ? ValidationBody.class : ValidationParam.class);

        params.forEach(param -> {
            List<ReqUrl> urls = param.getReqUrls();
            urls.forEach(url -> {
                this.saveParameter(param, paramType, url, null, param.getParameter().getType(), 0, maxDeepLevel);
            });
        });
    }

    public void urlCheckAndSave(MethodParameter methodParameter, ParamType paramType, ReqUrl reqUrl, Class<?> type) {
        if (!this.validationConfig.getMsgCheckType().equals(MsgCheckType.URL) || !this.validationConfig.isFreshUrlSave()) {
            return;
        }
        DetailParam detailParam = new DetailParam(methodParameter.getParameter(), methodParameter.getMethod(), null);

        List<ValidationData> datas = this.validationDataRepository.findByParamTypeAndUrlAndMethod(paramType, reqUrl.getUrl(), reqUrl.getMethod());
        if (datas.isEmpty()) {
            this.saveParameter(detailParam, paramType, reqUrl, null, type, 0, this.validationConfig.getMaxDeepLevel());
            this.validationDataRepository.flush();
            this.validationStore.refresh();
        }
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


    private void saveParameter(DetailParam detailParam, ParamType paramType, ReqUrl reqUrl, ValidationData parent, Class<?> type, int deepLevel, final int maxDeepLevel) {
        if (deepLevel > maxDeepLevel) {
            if (detailParam != null) {
                log.info(detailParam.getParameter().getType().getName() + "deep level " + deepLevel + " param : " + type.getName());
            }
            return;
        }
        for (Field field : type.getDeclaredFields()) {
            ValidationData param = this.validationDataRepository.findByParamTypeAndUrlAndMethodAndNameAndParentId(paramType, reqUrl.getUrl(), reqUrl.getMethod(), field.getName(), parent == null ? null : parent.getId());
            if (param == null) {
                param = this.getDefaultvalidationData(detailParam, paramType, reqUrl, parent, field, deepLevel);
            } else {
                param.updateKey(detailParam);
            }

            param = this.validationDataRepository.save(param);

            if (param.isObj()) {
                this.saveParameter(detailParam, paramType, reqUrl, param, param.getTypeClass(), deepLevel + 1, maxDeepLevel);
            }
        }
    }


}
