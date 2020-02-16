package SystemStats;

import CommandLine.CommandFeeder;
import ProcessStats.ProcessStat;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SystemResourceFinder implements Runnable {

    private Thread running = null;
    private SystemResourceOut output;

    public SystemResourceFinder(SystemResourceOut output){
        this.output = output;

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            float cpuUsage = Float.parseFloat(getStatFromCommand("Get-Counter -Counter \"\\Processor(_Total)\\% Processor Time\" -SampleInterval 2 -MaxSamples 1"));
            double memUsageBytes = Double.parseDouble(getStatFromCommand("Get-Counter -Counter \"\\Memory\\Committed bytes\" -SampleInterval 2 -MaxSamples 1; echo --eof--"));
            double memFreeBytes = Double.parseDouble(getStatFromCommand("Get-Counter -Counter \"\\Memory\\Available bytes\" -SampleInterval 2 -MaxSamples 1; echo --eof--"));

            double memUsageMB = memUsageBytes / 1048576;
            double memFreeMB = memFreeBytes / 1048576;
            SystemStatus newStat = new SystemStatus(cpuUsage, memUsageMB, memFreeMB);
            output.output(newStat);
        }
    }

    private String getStatFromCommand(String command){
        String out = "0";
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
            writer.write(command + "; echo --EOF--\n");
            writer.flush();
            line = reader.readLine();
            while (line  != null && !line.trim().equals("--EOF--")) {
                if(processHeadingFilter(line)){
                    out = line.split("\\s+")[1];
                }
                line = reader.readLine();
            }
            writer.close();
            stdout.close();
            stdin.close();
            stderr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    private boolean processHeadingFilter(String line){
        if(line.split("\\s+").length == 2){
            return true;
        }
        return false;
    }

    public void start(){
        if (running == null) {
            running = new Thread (this, "stats thread");
            running.start ();
        }
    }

    public void stop(){
        if(running != null){
            running.interrupt();
        }
    }
}
