package ProcessStats;

import ProcessStats.ProcessStat;

import java.util.List;

public interface ProcessOutput {
    public void output(List<ProcessStat> status);
}
