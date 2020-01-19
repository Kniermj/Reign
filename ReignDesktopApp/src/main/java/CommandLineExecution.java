import java.io.*;
import java.util.Scanner;

public class CommandLineExecution implements Runnable {

    private Thread running = null;
    private CommandFeeder feeder;

    CommandLineExecution(CommandFeeder feeder){
        this.feeder = feeder;
    }

    @Override
    public void run() {
        ProcessBuilder builder = new ProcessBuilder("cmd.exe");
        builder.redirectErrorStream(true);
        Process proc = null;
        try {
            proc = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        OutputStream stdin = proc.getOutputStream ();
        InputStream stderr = proc.getErrorStream ();
        InputStream stdout = proc.getInputStream ();

        BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        String line = "";
        while (true) {
            if(feeder.hasCommand()){
                CommandObject input = feeder.getNextCommand();
                try {
                    writer.write("((" + input.commandInput + ") && echo --EOF--) || echo --EOF--\n");
                    writer.flush();
                    line = reader.readLine();
                    while (line != null && !line.trim().equals("--EOF--")) {
                        input.commandOutput += line;
                        System.out.println("Stdout: " + line);
                        line = reader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start(){
        if (running == null) {
            running = new Thread (this, "command thread");
            running.start ();
        }
    }
    public void nothing(){
        return;
    }
}
