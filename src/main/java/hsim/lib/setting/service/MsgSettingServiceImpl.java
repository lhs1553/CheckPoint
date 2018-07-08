package hsim.lib.setting.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import hsim.lib.core.component.ComponentMap;
import hsim.lib.core.domain.ReqUrl;
import hsim.lib.core.domain.ValidationData;
import hsim.lib.core.repository.ValidationDataRepository;
import hsim.lib.core.store.ValidationStore;
import hsim.lib.core.type.ParamType;
import hsim.lib.core.util.ValidationObjUtil;
import hsim.lib.core.util.ValidationReqUrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MsgSettingServiceImpl implements MsgSettingService {

    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);

    public List<ReqUrl> getAllUrlList() {
        return this.validationDataRepository.findAllUrl();
    }

    @Override
    public List<ValidationData> getValidationData(String method, String url) {
        return this.validationDataRepository.findByUrlAndMethod(url, method);
    }

    @Override
    public List<ValidationData> getAllValidationData() {
        return this.validationDataRepository.findAll();
    }

    @Override
    public List<ValidationData> getValidationData(ParamType paramType, String method, String url) {
        return this.validationDataRepository.findByParamTypeAndUrlAndMethod(paramType, url, method);
    }

    @Override
    public void updateValidationData(List<ValidationData> models) {

        List<ValidationData> origins = this.validationDataRepository.findByIds(models.stream().map(model -> model.getId()).collect(Collectors.toList()));

        this.validationDataRepository.saveAll(
                origins.stream().map(origin -> origin.updateUserData(
                        models.stream().filter(model -> model.getId().equals(origin.getId())).findFirst().orElse(null))
                ).collect(Collectors.toList()));

        this.validationDataRepository.saveAll(models.stream().filter(model -> model.getId() == null).collect(Collectors.toList()));

        this.validationDataRepository.flushAndRuleSync();
        this.validationStore.refresh();
    }

    public void updateFromFile(MultipartFile file) {
        ObjectMapper objectMapper = ValidationObjUtil.getDefaultObjectMapper();

        try {
            String jsonStr = new String(file.getBytes(), "UTF-8");
            List<ValidationData> list = objectMapper.readValue(jsonStr, objectMapper.getTypeFactory().constructCollectionType(List.class, ValidationData.class));
            List<ReqUrl> reqUrls = ValidationReqUrlUtil.getUrlListFromValidationDatas(list);
            reqUrls.forEach(reqUrl -> {
                this.deleteValidationData(reqUrl);
            });
            Map<Long, Long> idsMap = new HashMap<>();
            List<ValidationData> saveList = new ArrayList<>();

            list.forEach(data -> {
                long oldId = data.getId();
                data.setId(null);
                data = this.validationDataRepository.save(data);
                idsMap.put(oldId, data.getId());
                saveList.add(data);
            });

            saveList.forEach(data -> {
                if (data.getParentId() != null) {
                    data.setParentId(idsMap.get(data.getParentId()));
                    this.validationDataRepository.save(data);
                }
            });

        } catch (IOException e) {
            log.info("file io exception : " + e.getMessage());
        }
    }

    public void updateFromFiles(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            this.updateFromFile(file);
        }
    }

    @Override
    public void updateValidationData(MultipartHttpServletRequest req) {
        req.getMultiFileMap().entrySet().forEach(entry -> {
            List<MultipartFile> files = entry.getValue();
            this.updateFromFiles(files);
        });

        this.validationDataRepository.flushAndRuleSync();
        this.validationStore.refresh();
    }

    @Override
    public void deleteValidationData(List<ValidationData> models) {
        if (models == null || models.isEmpty()) {
            return;
        }

        this.validationDataRepository.deleteAll(models);

        this.validationDataRepository.flushAndRuleSync();
        this.validationStore.refresh();
    }

    @Override
    public void deleteValidationData(ReqUrl url) {
        this.validationDataRepository.deleteAll(this.validationDataRepository.findByUrlAndMethod(url.getUrl(), url.getMethod()));

        this.validationDataRepository.flushAndRuleSync();
        this.validationStore.refresh();

    }


}
