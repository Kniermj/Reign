package Utils;

import CommandLine.CommandObject;
import com.google.cloud.firestore.annotation.DocumentId;

public class SystemIdentity {
    @DocumentId
    public String documentId;

    public String name;
    public int refreshtime;
    public boolean active;
    public CommandObject processing;
    public String owner;
    public boolean refreshProcesses;

    public SystemIdentity(){

    }

    public SystemIdentity(String name, int refreshtime, boolean active){
        this.name = name;
        this.refreshtime = refreshtime;
        this.active = active;
        processing = null;
        refreshProcesses = true;
        owner = "";
    }


}
