package SystemStats;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;

public class SystemStatus {


    @DocumentId
    public String documentId;

    @ServerTimestamp
    public Timestamp creation;

    public float cpuUsage;
    public double memUsage;
    public double memFree;

    public SystemStatus(float cpuUsage, double  memUsage, double memFree){
        this.cpuUsage = cpuUsage;
        this.memUsage = memUsage;
        this.memFree = memFree;
    }

    public SystemStatus(){
    }
}
