package CommandLine;

public interface CommandFeeder {

    public CommandObject getNextCommand();
    public boolean hasCommand();
}
