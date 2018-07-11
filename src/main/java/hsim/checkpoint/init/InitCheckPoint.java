package hsim.checkpoint.init;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.msg.MsgSaver;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationStore;

import java.util.Arrays;

public class InitCheckPoint {

    private static String[] jarPath ;

    public static String getJarPath(){
        if(jarPath == null) { return null; }
        return Arrays.stream(jarPath).filter(path -> path.endsWith(".jar")).findFirst().orElse(null);
    }

    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);

    public static void init(String[] args){
        InitCheckPoint.jarPath = args;
    }

    public InitCheckPoint(){
       this.validationDataRepository.flush();
       this.validationStore.refresh();
    }
}
