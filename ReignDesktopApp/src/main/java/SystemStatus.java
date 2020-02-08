import com.google.cloud.firestore.annotation.DocumentId;

public class SystemStatus {


    @DocumentId
    public String documentId;

    public String cpuUsage;
    public String  memUsage;


    public SystemStatus(){

    }
}
