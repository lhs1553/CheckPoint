package hsim.checkpoint.init;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationStore;
import hsim.checkpoint.util.AnnotationScanner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InitCheckPoint {

    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);
    private AnnotationScanner annotationScanner = ComponentMap.get(AnnotationScanner.class);

    public InitCheckPoint() {
        this.validationDataRepository.flush();
        this.validationStore.refresh();
    }

}
