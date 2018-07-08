package hsim.checkpoint.init;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.msg.MsgSaver;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationStore;

public class InitCheckPoint {

    private MsgSaver msgSaver = ComponentMap.get(MsgSaver.class);
    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);

    public InitCheckPoint(){
       this.msgSaver.annotationScan();
       this.validationDataRepository.flush();
       this.validationStore.refresh();
    }
}
