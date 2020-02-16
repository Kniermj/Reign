package CommandLine;

import java.io.*;

public class CommandLineExecution implements Runnable {

    private Thread running = null;
    private CommandFeeder feeder;
    private CommandOutput out;

    public CommandLineExecution(CommandFeeder feeder, CommandOutput out){
        this.feeder = feeder;
        this.out = out;
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(feeder.hasCommand()){
                CommandObject input = feeder.getNextCommand();
                try {
                    writer.write("((" + input.commandInput + ") && echo --EOF--) || echo --EOF--\n");
                    writer.flush();
                    line = reader.readLine();
                    while (line != null && !line.trim().equals("--EOF--")) {
                        input.commandOutput += line + "\n";
                        line = reader.readLine();
                    }
                    out.output(input);
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

    public void stop(){
        if(running != null){
            running.interrupt();
        }
    }
}
