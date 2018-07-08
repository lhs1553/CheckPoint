package hsim.checkpoint.core.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.store.ValidationRuleStore;
import hsim.checkpoint.core.type.ParamType;
import hsim.checkpoint.core.util.ValidationObjUtil;
import hsim.checkpoint.exception.ValidationLibException;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationDataRepository {

    private ObjectMapper objectMapper = ValidationObjUtil.getDefaultObjectMapper();
    private ValidationRuleStore validationRuleStore = ComponentMap.get(ValidationRuleStore.class);

    private List<ValidationData> datas;
    private Map<String, ReqUrl> urlMap;

    private String filePath = "checkpoint/validation.json";

    private long currentMaxId = 0;

    public ValidationDataRepository() {
        this.dataInit();
    }

    private void urlMapInit() {
        this.urlMap = new HashMap<>();
        this.datas.stream().forEach(d -> {
            ReqUrl reqUrl = new ReqUrl(d.getUrl(), d.getMethod());
            this.urlMap.put(reqUrl.getUniqueKey(), reqUrl);
        });
    }

    private void currentIdInit() {
        this.currentMaxId = 0;
        this.datas.stream().forEach(d -> {
            if (d.getId() > this.currentMaxId) {
                this.currentMaxId = d.getId();
            }
        });
    }

    private void parentObjInit(List<ValidationData> dataList) {
        dataList.stream().forEach(d -> {
            if (d.getParentId() != null) {
                d.setParent(dataList.stream().filter(vd -> d.getParentId().equals(vd.getId())).findFirst().orElse(null));
            }
        });
    }

    public void dataInit() {
        this.findAll();
        this.currentIdInit();
        this.urlMapInit();
    }

    public List<ValidationData> findByIds(List<Long> ids) {
        return this.datas.stream().filter(d -> ids.contains(d.getId())).collect(Collectors.toList());
    }

    public ValidationData findById(Long id) {
        return this.datas.stream().filter(d -> d.getId() != null && d.getId().equals(id)).findFirst().orElse(null);
    }

    public List<ValidationData> findAll() {
        return this.findAll(true);
    }

    public List<ValidationData> findAll(boolean referenceCache) {

        if (referenceCache && this.datas != null) {
            return this.datas;
        }

        List<ValidationData> list = null;
        try {
            String jsonStr = FileUtils.readFileToString(new File(this.filePath), "UTF-8");
            list = objectMapper.readValue(jsonStr, objectMapper.getTypeFactory().constructCollectionType(List.class, ValidationData.class));
        } catch (IOException e) {
            list = new ArrayList<>();
        }

        this.parentObjInit(list);

        if (referenceCache) {
            this.datas = list;
        }

        return list;
    }

    public List<ValidationData> findByUrlAndMethod(String url, String method) {
        return this.datas.stream().filter(d -> d.getUrl().equalsIgnoreCase(url) && d.getMethod().equalsIgnoreCase(method)).collect(Collectors.toList());
    }

    public List<ValidationData> findByParamTypeAndUrlAndMethod(ParamType paramType, String url, String method) {
        return this.findByUrlAndMethod(url, method).stream().filter(d -> d.getParamType().equals(paramType)).collect(Collectors.toList());
    }

    public List<ValidationData> findByParamTypeAndUrlAndMethodAndName(ParamType paramType, String url, String method, String name) {
        return this.findByUrlAndMethodAndName(url, method, name).stream().filter(vd -> vd.getParamType().equals(paramType)).collect(Collectors.toList());
    }

    public ValidationData findByParamTypeAndUrlAndMethodAndNameAndParentId(ParamType paramType, String url, String method, String name, Long parentId) {
        if (parentId == null) {
            return this.findByParamTypeAndUrlAndMethodAndName(paramType, url, method, name).stream().filter(d -> d.getParentId() == null).findAny().orElse(null);
        }
        return this.findByParamTypeAndUrlAndMethodAndName(paramType, url, method, name).stream()
                .filter(d -> d.getParentId() != null && d.getParentId().equals(parentId)).findAny().orElse(null);

    }

    public List<ValidationData> findByUrlAndMethodAndName(String url, String method, String name) {
        return this.findByUrlAndMethod(url, method).stream().filter(d -> d.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
    }


    public List<ValidationData> findByParentId(Long id) {
        return this.datas.stream().filter(d -> d.getParentId() != null && d.getParentId().equals(id)).collect(Collectors.toList());
    }


    public ValidationData findByUrlAndMethodAndNameAndParentId(String url, String method, String name, Long parentId) {
        if (parentId == null) {
            return this.findByUrlAndMethodAndName(url, method, name).stream().filter(d -> d.getParentId() == null).findAny().orElse(null);
        }
        return this.findByUrlAndMethodAndName(url, method, name).stream()
                .filter(d -> d.getParentId() != null && d.getParentId().equals(parentId)).findAny().orElse(null);

    }


    public List<ValidationData> saveAll(List<ValidationData> pDatas) {
        pDatas.forEach(data -> this.save(data));
        return pDatas;
    }

    public void deleteAll(List<ValidationData> pDatas) {
        pDatas.stream().forEach(data -> delete(data));
    }

    public void delete(ValidationData pData) {
        this.datas = this.datas.stream().filter(d -> !d.getId().equals(pData.getId())).collect(Collectors.toList());
    }

    public ValidationData save(ValidationData data) {
        if (data.getParamType() == null || data.getUrl() == null || data.getMethod() == null || data.getType() == null || data.getTypeClass() == null) {
            throw new ValidationLibException("mandatory field is null ", HttpStatus.BAD_REQUEST);
        }
        ValidationData existData = this.datas.stream().filter(d -> d.getId().equals(data.getId())).findAny().orElse(null);
        if (existData == null) {
            data.setId(++this.currentMaxId);
            this.datas.add(data);
        } else {
            ValidationObjUtil.objectDeepCopyWithBlackList(data, existData, "id");
            existData.setValidationRules(data.getValidationRules());
        }

        return data;
    }
    public synchronized  void flushAndRuleSync(){
        this.ruleSync();
        this.flush();
    }

    public synchronized  void ruleSync(){
        this.datas.stream().forEach(data -> {
            data.initValidationRuleList(this.validationRuleStore.getRules(),true);
        });
    }
    public synchronized  void ruleCheck(){
        this.datas.stream().forEach(data -> {
            data.initValidationRuleList(this.validationRuleStore.getRules(), false);
        });
    }

    public synchronized void flush() {

        this.ruleCheck();

        try {
            String jsonStr = this.objectMapper.writeValueAsString(this.datas);
            try {
                FileUtils.writeStringToFile(new File(this.filePath), jsonStr, "UTF-8");
            } catch (IOException e) {
                throw new ValidationLibException("file write error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (JsonProcessingException e) {
            throw new ValidationLibException("json str parsing error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        this.dataInit();
    }

    public List<ReqUrl> findAllUrl() {
        return this.urlMap.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }

}
