import java.util.List;

public class ShortestJobScheduler extends Scheduler {
    private List<Process> processes;
    private double avgWaitingTime;
    private double avgTurnaroundTime;

    ShortestJobScheduler(List<Process> list){
        processes = list;
        avgTurnaroundTime = 0;
        avgWaitingTime = 0;
    }
    @Override
    public List<Process> executionOrder() {
        return null;
    }

    @Override
    public List<Integer> waitingTime() {
        return null;
    }

    @Override
    public List<Integer> turnaroundTime() {
        return null;
    }

    @Override
    public double avgWaitingTime() {
        return avgWaitingTime;
    }

    @Override
    public double avgTurnaroundTime() {
        return avgTurnaroundTime;
    }

    @Override
    public void startScheduler() {
        
    }
}
