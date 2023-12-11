import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SRTFScheduler extends Scheduler {

    private List<Process> processes;


    // Constructor to initialize the SRTF scheduler with a list of processes
    public SRTFScheduler(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        this.avgTurnaroundTime=0;
        this.avgWaitingTime=0;
    }

    // Implementation of the SRTF scheduling algorithm
    @Override
    public List<Process> executionOrder() {
        // To be implemented
        return null;
    }

    // Implementation of other abstract methods
    @Override
    public List<Integer> waitingTime() {
        List<Integer> waitingTimes = new ArrayList<>();
        for (Process process : processes) {
            int waitingTime = process.getTurnAroundTime() - process.getBurstTime();
            waitingTimes.add(waitingTime);
        }
        return waitingTimes;
    }

    @Override
    public List<Integer> turnaroundTime() {
        List<Integer> turnaroundTimes = new ArrayList<>();
        for (Process process : processes) {
            turnaroundTimes.add(process.getTurnAroundTime());
        }
        return turnaroundTimes;
    }

    @Override
    public double avgWaitingTime() {
        List<Integer> waitingTimes = waitingTime();
        int sum = 0;

        for (int waitingTime : waitingTimes) {
            sum += waitingTime;
        }

        return (double) sum / waitingTimes.size();
    }

    public double avgTurnaroundTime() {
        List<Integer> turnaroundTimes = turnaroundTime();
        int sum = 0;

        for (int turnaroundTime : turnaroundTimes) {
            sum += turnaroundTime;
        }

        return (double) sum / turnaroundTimes.size();
    }
    @Override
    public void startScheduler() {
        System.out.println("SRTF Scheduler is running...");
        PrintOUTPUT();
    }
}