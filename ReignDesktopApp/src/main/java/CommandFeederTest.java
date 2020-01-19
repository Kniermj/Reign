public class CommandFeederTest implements CommandFeeder {

    private int numberOfRuns;

    public CommandFeederTest(int numberOfRuns){
        this.numberOfRuns = numberOfRuns;
    }

    @Override
    public CommandObject getNextCommand() {
        if(numberOfRuns > 0){
            numberOfRuns--;
            return new CommandObject("dir");
        }else{
            return null;
        }
    }

    @Override
    public boolean hasCommand() {
        return numberOfRuns > 0;
    }
}
