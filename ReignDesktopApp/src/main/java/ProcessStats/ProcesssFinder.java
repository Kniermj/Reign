package ProcessStats;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcesssFinder implements Runnable {

    private Thread running = null;
    private ProcessOutput output;
    private ProcessFinderTrigger trigger;

    public ProcesssFinder(ProcessOutput output, ProcessFinderTrigger trigger){
        this.output = output;
        this.trigger = trigger;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(trigger.runProcessFinder()){
                List<ProcessStat> currentProc = getCurrentProccess();
                output.output(currentProc);
            }
        }
    }

    private List<ProcessStat> getCurrentProccess(){
        List out = new ArrayList();
        ProcessBuilder builder = new ProcessBuilder("powershell.exe");
        builder.redirectErrorStream(true);
        Process proc = null;
        try {
            proc = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
            return out;
        }

        OutputStream stdin = proc.getOutputStream ();
        InputStream stderr = proc.getErrorStream ();
        InputStream stdout = proc.getInputStream ();

        BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        String line = "";

            try {
                writer.write("(get-process| ft -HideTableHeaders); echo --EOF--\n");
                writer.flush();
                line = reader.readLine();
                while (line  != null && !line.trim().equals("--EOF--")) {
                    if(processHeadingFilter(line)){
                        ProcessStat ps = makeFromCMDLine(line);
                        out.add(ps);
                    }
                    line = reader.readLine();
                }
            writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return out;
    }

    private boolean processHeadingFilter(String line){
        if(line.split("\\s+").length >= 8){
            return true;
        }
        return false;
    }

    private ProcessStat makeFromCMDLine(String line){
        String[] info = line.split("\\s+");
        return new ProcessStat(info[info.length - 3], info[info.length - 1]);
    }

    public void start(){
        if (running == null) {
            running = new Thread (this, "process thread");
            running.start ();
        }
    }

    public void stop(){
        if(running != null){
            running.interrupt();
        }
    }
}
