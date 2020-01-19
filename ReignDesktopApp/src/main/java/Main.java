
public class Main {
    public static void main(String[] args){
        CommandFeeder feeder = new CommandFeederTest(3);
        CommandLineExecution command  = new CommandLineExecution(feeder);
        command.start();
    }
}
