package CommandLine;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;

public class CommandObject {

    @DocumentId
    public String documentId;
    public String commandInput;
    public String commandOutput;

    @ServerTimestamp
    public Timestamp creation;

    public CommandObject(){}
    public CommandObject(String commandInput){
        this.commandInput = commandInput;
    }

    public boolean isFinished(){
        return commandOutput != null;
    }
}
