import com.google.cloud.firestore.annotation.DocumentId;

public class CommandObject {

    @DocumentId
    public String documentId;
    public String commandInput;
    public String commandOutput;

    public CommandObject(){}
    public CommandObject(String commandInput){
        this.commandInput = commandInput;
    }

    public boolean isFinished(){
        return commandOutput != null;
    }
}
