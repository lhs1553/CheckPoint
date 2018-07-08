package hsim.checkpoint.setting.service;

import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.type.ParamType;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;


public interface MsgSettingService {
    List<ReqUrl> getAllUrlList();

    List<ValidationData> getValidationData(String method, String url);

    List<ValidationData> getAllValidationData();

    List<ValidationData> getValidationData(ParamType paramType, String method, String url);

    void updateValidationData(List<ValidationData> models);

    void updateValidationData(MultipartHttpServletRequest req);

    void deleteValidationData(List<ValidationData> models);

    void deleteValidationData(ReqUrl url);

}
