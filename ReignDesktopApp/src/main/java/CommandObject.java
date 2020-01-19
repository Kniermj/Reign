public class CommandObject {

    public String commandInput;
    public String commandOutput;

    CommandObject(String commandInput){
        this.commandInput = commandInput;
    }

    public boolean isFinished(){
        return commandOutput != null;
    }
}
