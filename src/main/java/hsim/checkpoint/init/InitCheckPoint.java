package hsim.checkpoint.init;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.msg.MethodSyncor;
import hsim.checkpoint.core.msg.MsgSaver;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationStore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InitCheckPoint {

    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);
    private MethodSyncor methodSyncor= ComponentMap.get(MethodSyncor.class);

    public InitCheckPoint() {
        this.validationDataRepository.flush();
        this.validationStore.refresh();
        this.methodSyncor.updateMethodKeyAsync();
    }

}
