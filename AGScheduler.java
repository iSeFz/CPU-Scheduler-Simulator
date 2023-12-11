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
            process.setTempQuantum(processQT);
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
        this.mProcesses.sort((o1, o2) -> {
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
            for (int i = 0; i < this.mProcesses.size(); i++) {
                if (this.readyQueue.peek().getAGFactor() == this.mProcesses.get(i).getAGFactor())
                    x = false;
            }

            if (x)
                this.readyQueue.remove();
        }
    }

    @Override
    public void startScheduler() {
        //Process process = mProcesses.get(0);
        int lastProcessAGFactor = 0;
        //int quantum = process.getQuantum();
        boolean addedInTimeLine = false;
        int indexOfProcess = 0;

        for (int time = 0; time < this.MaxTime; time++) {
            if (addedInTimeLine)
                this.mTimeLine.add(time);
            addedInTimeLine = true;

            sortProcesses(time);
            printUpdates(time);

            // Non-Preemptive
            if (mProcesses.size() != 0 && this.mProcesses.get(indexOfProcess).getArrivalTime() <= time) {
                Process tempProcess = new Process(this.mProcesses.get(indexOfProcess).getName(), this.mProcesses.get(indexOfProcess).getArrivalTime(),
                        this.mProcesses.get(indexOfProcess).getBurstTime());

                tempProcess.setPriority(this.mProcesses.get(indexOfProcess).getPriority());
                tempProcess.setAGFactor(this.mProcesses.get(indexOfProcess).getAGFactor());
                tempProcess.setRemainingTime(this.mProcesses.get(indexOfProcess).getRemainingTime());
                tempProcess.setTurnAroundTime(this.mProcesses.get(indexOfProcess).getTurnAroundTime());
                tempProcess.setWaitingTime(this.mProcesses.get(indexOfProcess).getWaitingTime());
                tempProcess.setTempQuantum(this.mProcesses.get(indexOfProcess).getTempQuantum() - 1);

                System.out.print("Process " + tempProcess.getName() + " is running.");

                if (lastProcessAGFactor != tempProcess.getAGFactor()) {
                    this.mOutput.add(tempProcess);
                    lastProcessAGFactor = tempProcess.getAGFactor();
                    this.mTimeLine.add(time);
                }

                // Updating the waiting time and the remaining time of the current process,
                time += Math.min(tempProcess.getRemainingTime(), (int) Math.ceil(tempProcess.getQuantum() * 0.5));
                tempProcess.setRemainingTime(tempProcess.getRemainingTime() - Math.min((int) Math.ceil(tempProcess.getQuantum() * 0.5), tempProcess.getRemainingTime()));

                // Preemptive
                int remainingHalfQT = (int) Math.floor(tempProcess.getQuantum() * 0.5);
                for (int i = 0; i < remainingHalfQT; i++) {
                    int index = 0;
                    boolean flag = false;

                    for (index = 0; index < this.mProcesses.size(); index++) {
                        if (this.mProcesses.get(index).getAGFactor() < tempProcess.getAGFactor() && this.mProcesses.get(index).getArrivalTime() <= time) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) { // there is a process with smaller AG Factor
                        tempProcess.setQuantum(tempProcess.getRemainingTime() + tempProcess.getQuantum());
                        tempProcess.setBurstTime(tempProcess.getBurstTime() - (tempProcess.getBurstTime() - tempProcess.getRemainingTime()));
                        this.readyQueue.add(tempProcess);
                        indexOfProcess = index;
                        break; // may have update later for exiting the temp process
                    } else { // there isn't a process with smaller AG Factor
                        Process nextProcess =  new Process(this.mProcesses.get(index).getName(), this.mProcesses.get(index).getArrivalTime(),
                                this.mProcesses.get(index).getBurstTime());

                        nextProcess.setPriority(this.mProcesses.get(index).getPriority());
                        nextProcess.setAGFactor(this.mProcesses.get(index).getAGFactor());
                        nextProcess.setRemainingTime(this.mProcesses.get(index).getRemainingTime());
                        nextProcess.setTurnAroundTime(this.mProcesses.get(index).getTurnAroundTime());
                        nextProcess.setWaitingTime(this.mProcesses.get(index).getWaitingTime());

                        int mean = ((int) Math.ceil((this.TotalQuantum / this.NumOfProcesses) * 0.1));
                        tempProcess.setQuantum(tempProcess.getQuantum() + mean);

                        this.readyQueue.add(nextProcess);
                    }

                    if (tempProcess.getTempQuantum() == 0 && tempProcess.getBurstTime() != 0) {
                        Process tempQueueProcess = this.readyQueue.peek();

                        for (int j = 0; j < this.mProcesses.size(); j++) {
                            if (this.mProcesses.get(j).getName().equals(tempQueueProcess.getName())) {
                                indexOfProcess = j;
                                break;
                            }
                        }

                        int mean = ((int) Math.ceil((this.TotalQuantum / this.NumOfProcesses) * 0.1));
                        tempProcess.setQuantum(tempProcess.getQuantum() + mean);
                        tempProcess.setBurstTime(tempProcess.getBurstTime() - (tempProcess.getBurstTime() - tempProcess.getRemainingTime())); // may change later if there are errors
                        this.readyQueue.add(tempProcess);
                        break;
                    }

                    if (tempProcess.getBurstTime() == 0) {
                        Process tempQueueProcess = this.readyQueue.peek();

                        for (int j = 0; j < this.mProcesses.size(); j++) {
                            if (this.mProcesses.get(j).getName().equals(tempQueueProcess.getName())) {
                                indexOfProcess = j;
                                break;
                            }
                        }

                        this.mDeadList.add(tempProcess);
                        this.mProcesses.remove(tempProcess);
                        break;
                    }
                }
            }
        }
    }

}
