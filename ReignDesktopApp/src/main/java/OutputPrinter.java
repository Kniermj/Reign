public class OutputPrinter implements CommandOutput {

    @Override
    public boolean output(CommandObject obj) {
        System.out.println(obj.commandOutput);
        return true;
    }
}
