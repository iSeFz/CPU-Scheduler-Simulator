import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class AGScheduler extends Scheduler {
    Scanner Input;
    int NumOfProcesses;
    ArrayList<Process> mProcesses;
    ArrayList<Process> mTemp;
    ArrayList<Process> mOutput;
    ArrayList<Process> mDeadList;
    ArrayList<Integer> mTimeLine;
    Queue<Process> readyQueue;
    int MaxTime;
    int TotalQuantum;
    int TotalTurnAround;
    int TotalWaitingTime;

    public AGScheduler() {
        this.Input = new Scanner(System.in);
        this.NumOfProcesses = 0;
        this.mProcesses = new ArrayList<>();
        this.mTimeLine = new ArrayList<>();
        this.mTemp = new ArrayList<>();
        this.mOutput = new ArrayList<>();
        this.mDeadList = new ArrayList<>();
        this.readyQueue = new LinkedList<>();
        this.MaxTime = 0;
        this.TotalQuantum = 0;
        this.TotalTurnAround = 0;
        this.TotalWaitingTime = 0;
    }

    @Override
    public List<Process> executionOrder() {
        return mOutput;
    }

    @Override
    public List<Integer> waitingTime() {
        List<Integer> waitingTimeList = new ArrayList<Integer>();
        waitingtimeCalculations();
        for (Process process : mProcesses) {
            waitingTimeList.add(process.getWaitingTime());
        }
        return waitingTimeList;
    }

    @Override
    public List<Integer> turnaroundTime() {
        List<Integer> turnAroundList = new ArrayList<Integer>();
        turnAroundCalculations();
        for (Process process : mProcesses) {
            turnAroundList.add(process.getTurnAroundTime());
        }
        return turnAroundList;
    }

    @Override
    public double avgWaitingTime() {
        waitingtimeCalculations();
        return this.TotalWaitingTime / this.NumOfProcesses;
    }

    @Override
    public double avgTurnaroundTime() {
        turnAroundCalculations();
        return this.TotalTurnAround / this.NumOfProcesses;
    }

    public void turnAroundCalculations() {
        for (int i = 0; i < this.mProcesses.size(); i++) {
            this.mProcesses.get(i).setTurnAroundTime(
                    this.mProcesses.get(i).getCompletionTime() - this.mProcesses.get(i).getArrivalTime());
            this.TotalTurnAround += this.mProcesses.get(i).getTurnAroundTime();
        }
    }

    public void waitingtimeCalculations() {
        for (int i = 0; i < this.mProcesses.size(); i++) {
            this.mProcesses.get(i).setWaitingTime(
                    this.mProcesses.get(i).getTurnAroundTime() - this.mProcesses.get(i).getBurstTime());
            this.TotalWaitingTime += this.mProcesses.get(i).getWaitingTime();
        }
    }

    public int generateRandomNumber() {
        Random random = new Random();

        // Generate a random number between 0 (inclusive) and 21 (exclusive)
        int randomNumber = random.nextInt(21);

        return randomNumber;
    }

    int calculate_AG_Factor(Process P) {
        int rand = generateRandomNumber();

        if (rand < 10) {
            return rand + P.getArrivalTime() + P.getBurstTime();
        } else if (rand > 10) {
            return 10 + P.getArrivalTime() + P.getBurstTime();
        } else {
            return P.getPriority() + P.getArrivalTime() + P.getBurstTime();
        }

    }

    // get user input return <list>process

    // should be done after taking input after regular input from user
    public void getInput(List<Process> processes) {
        System.out.println("Enter quantum Time of process : ");
        int processQT = this.Input.nextInt();

        for (Process process : processes) {
            process.setQuantum(processQT);
            process.setRemainingTime(process.getBurstTime());
            this.MaxTime = process.getBurstTime();

            if (this.MaxTime < process.getBurstTime() + process.getArrivalTime())
                this.MaxTime = process.getBurstTime() + process.getArrivalTime();

            // calculate AG factor
            int AGFactor = calculate_AG_Factor(process);
            // setting AG factor
            process.setAGFactor(AGFactor);

            this.mProcesses.add(process);
            // this.mTemp.add(process);

        }
    }

    // Print [Time ] -> Quantum ( )
    public void printUpdates(int time) {
        System.out.print("[Time: " + time + "] -> ");
        System.out.print("Quantum ( ");
        for (int i = 0; i < this.mProcesses.size(); i++)
            System.out.print(this.mProcesses.get(i).getQuantum() + " ");
        System.out.print(") -> ceil(50%) = ( ");
        for (int i = 0; i < this.mProcesses.size(); i++)
            System.out.print((int) Math.ceil((this.mProcesses.get(i).getQuantum()) * 0.5) + " ");
        System.out.print(")		");
    }

    // Sorting Processes in List
    public void sortProcesses(int time) {
        this.mTemp.sort((o1, o2) -> {
            if (o1.getArrivalTime() <= time && o2.getArrivalTime() <= time) {
                if (o1.getAGFactor() < o2.getAGFactor())
                    return -1;
                else
                    return 1;
            } else if (o1.getArrivalTime() < o2.getArrivalTime())
                return -1;
            else
                return 1;
        });
    }

    public void isFinished() {
        boolean x = true;
        while (x) {
            for (int i = 0; i < this.mTemp.size(); i++) {
                if (this.readyQueue.peek().getAGFactor() == this.mTemp.get(i).getAGFactor())
                    x = false;
            }

            if (x)
                this.readyQueue.remove();
        }
    }

    @Override
    public void startScheduler() {
        throw new UnsupportedOperationException("Unimplemented method 'startScheduler'");
    }

}
