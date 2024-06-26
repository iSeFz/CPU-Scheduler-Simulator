import java.util.ArrayList;
import java.util.List;

// this class to sort based on burst time
class compareBurst implements java.util.Comparator<Process> {

    @Override
    public int compare(Process o1, Process o2) {
        return o1.getBurstTime() - o2.getBurstTime();
    }
}

// to sort based on arrived time
class compareArrivedTime implements java.util.Comparator<Process> {

    @Override
    public int compare(Process p1, Process p2) {
        return p1.getArrivalTime() - p2.getArrivalTime();
    }
}

public class ShortestJobScheduler extends Scheduler {
    private List<Process> processes;
    private double avgWaitingTime;
    private double avgTurnaroundTime;
    private final int contextTime;

    public ShortestJobScheduler(List<Process> list, int c) {
        processes = list;
        avgTurnaroundTime = 0;
        avgWaitingTime = 0;
        contextTime = c;
    }

    @Override
    public List<Process> executionOrder() {
        return processes;
    }

    @Override
    public List<Integer> waitingTime() {
        List<Integer> res = new ArrayList<Integer>();
        for (Process p : processes)
            res.add(p.getWaitingTime());
        return res;
    }

    @Override
    public List<Integer> turnaroundTime() {
        List<Integer> res = new ArrayList<Integer>();
        for (Process p : processes)
            res.add(p.getTurnAroundTime());
        return res;
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
        int currentTime = 0;
        int counter = 0; // to point to last process was arrived from process list (you can remove it and delete from front of process list)
        List<Process> ArrivedProcesses = new ArrayList<Process>();
        List<Process> resultList = new ArrayList<Process>();
        processes.sort(new compareArrivedTime());
        while (resultList.size() != processes.size()) {
            if (ArrivedProcesses.isEmpty()) { // to handle if there is process will arrive after all process was arrived done
                ArrivedProcesses.add(processes.get(counter++));
                if (currentTime < ArrivedProcesses.get(0).getArrivalTime())
                    currentTime = ArrivedProcesses.get(0).getArrivalTime();
            }
            // to take all processes that will arrive during current time
            for (; counter < processes.size() && processes.get(counter).getArrivalTime() <= currentTime; counter++) {
                ArrivedProcesses.add(processes.get(counter));
            }
            ArrivedProcesses.sort(new compareBurst());
            resultList.add(ArrivedProcesses.get(0));
            resultList.get(resultList.size()-1).setWaitingTime(currentTime - resultList.get(resultList.size()-1).getArrivalTime());
            avgWaitingTime += currentTime - resultList.get(resultList.size()-1).getArrivalTime();
            currentTime += ArrivedProcesses.get(0).getBurstTime();
            resultList.get(resultList.size()-1).setCompletionTime(currentTime);
            resultList.get(resultList.size()-1).setTurnAroundTime(currentTime - resultList.get(resultList.size()-1).getArrivalTime());
            avgTurnaroundTime += currentTime - resultList.get(resultList.size()-1).getArrivalTime();
            ArrivedProcesses.remove(ArrivedProcesses.get(0));
            currentTime += contextTime;
        }
        processes = resultList;
        avgTurnaroundTime /= processes.size();
        avgWaitingTime /= processes.size();
        System.out.println("\nShortest Job First Scheduler:\n");
        super.PrintOUTPUT();
    }
}
