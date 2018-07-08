package hsim.lib.core.init;

import hsim.lib.core.component.ComponentMap;
import hsim.lib.core.msg.MsgSaver;
import hsim.lib.core.repository.ValidationDataRepository;
import hsim.lib.core.store.ValidationStore;

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
