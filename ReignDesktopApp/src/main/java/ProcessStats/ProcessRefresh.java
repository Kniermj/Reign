package ProcessStats;

import Utils.SystemCheckIn;
import Utils.SystemIdentity;
import com.google.cloud.firestore.Firestore;

public class ProcessRefresh implements ProcessFinderTrigger{

    private SystemCheckIn checkIn;

    public ProcessRefresh(SystemCheckIn checkIn, Firestore db){
        this.checkIn = checkIn;
    }

    public boolean runProcessFinder(){
        return checkIn.identity.refreshProcesses;
    }
}
