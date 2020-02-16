package ProcessStats;

import com.google.cloud.firestore.annotation.DocumentId;

public class ProcessStat {

    @DocumentId
    public String documentId;
    public String processName;
    public String processId;


    public ProcessStat(String processId, String processName){
        this.processId = processId;
        this.processName = processName;
    }


    public ProcessStat(){

    }


}
