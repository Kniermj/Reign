import com.google.cloud.firestore.annotation.DocumentId;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SystemIdentity {
    @DocumentId
    public String documentId;

    public String name;
    public int refreshtime;
    public boolean active;
    public CommandObject processing;
    public String owner;

    public SystemIdentity(){

    }
    public SystemIdentity(String name, int refreshtime, boolean active){
        this.name = name;
        this.refreshtime = refreshtime;
        this.active = active;
        processing = null;
        owner = "";
    }


}
