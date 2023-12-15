import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AGScheduler extends Scheduler {
    private List<Process> mProcesses;
    private List<Process> mOutput;
    private List<Process> mDeadList;
    private List<Process> readyQueue;
    private int NumOfProcesses;
    private double avgTurnaroundTime;
    private double avgWaitingTime;

    // Default constructor
    public AGScheduler() {
        this.mProcesses = new ArrayList<>();
        this.mOutput = new ArrayList<>();
        this.mDeadList = new ArrayList<>();
        this.readyQueue = new ArrayList<>();
        this.NumOfProcesses = 0;
        this.avgTurnaroundTime = 0;
        this.avgWaitingTime = 0;
    }

    // Parameterized constructor
    public AGScheduler(List<Process> processList) {
        this(); // Calling the default constructor to set the default values
        this.mProcesses = processList;
        this.NumOfProcesses = mProcesses.size();

        for (Process process : mProcesses) {
            // Calculate & set the process AG factor
            calculateAGFactor(process);

            // Set the process temp quantum
            process.setTempQuantum(process.getQuantum());
        }
    }

    // Return the execution order of the processes
    @Override
    public List<Process> executionOrder() {
        return mOutput;
    }

    // Return the waiting time of the processes
    @Override
    public List<Integer> waitingTime() {
        List<Integer> waitingTimeList = new ArrayList<Integer>();
        // Sort the finished processes by name
        mDeadList.sort(Comparator.comparing(Process::getName));
        // Add the waiting time of each process to the list
        for (Process process : mDeadList)
            waitingTimeList.add(process.getWaitingTime());
        System.out.println("Note: Processes' waiting times are sorted alphabetically!");
        return waitingTimeList;
    }

    // Return the turnaround time of the processes
    @Override
    public List<Integer> turnaroundTime() {
        List<Integer> turnaroundTimeList = new ArrayList<Integer>();
        // Sort the finished processes by name
        mDeadList.sort(Comparator.comparing(Process::getName));
        // Add the waiting time of each process to the list
        for (Process process : mDeadList)
            turnaroundTimeList.add(process.getTurnAroundTime());
        System.out.println("Note: Processes' turnaround times are sorted alphabetically!");
        return turnaroundTimeList;
    }

    // Return the average waiting time of the processes
    @Override
    public double avgWaitingTime() {
        return this.avgWaitingTime;
    }

    // Return the average turnaround time of the processes
    @Override
    public double avgTurnaroundTime() {
        return this.avgTurnaroundTime;
    }

    // Calculate & set the AG Factor for a certain process
    private void calculateAGFactor(Process P) {
        int agFactor;
        // Generate a random number between 0 (inclusive) and 21 (exclusive)
        Random random = new Random();
        int randomNumber = random.nextInt(21);
        // Compute the formula based on the assignment sheet
        if (randomNumber < 10)
            agFactor = randomNumber + P.getArrivalTime() + P.getBurstTime();
        else if (randomNumber > 10)
            agFactor = 10 + P.getArrivalTime() + P.getBurstTime();
        else
            agFactor = P.getPriority() + P.getArrivalTime() + P.getBurstTime();
        // Set the process ag factor after computing it via the formula
        P.setAGFactor(agFactor);
    }

    // Print [Time] => Quantum ()
    private void printUpdates(int time) {
        List<Process> tempProcesses = this.mProcesses;
        List<Process> tempDead = this.mDeadList;
        List<Process> finalresult = new ArrayList<>();

        for (int i = 0; i < tempProcesses.size(); i++) {
            finalresult.add(tempProcesses.get(i));
        }

        for (int i = 0; i < tempDead.size(); i++) {
            finalresult.add(tempDead.get(i));
        }

        finalresult.sort(Comparator.comparing(Process::getName));

        System.out.print("[Current Time: " + time + "] => Quantum ( ");

        for (int i = 0; i < finalresult.size(); i++)
            System.out.print(finalresult.get(i).getQuantum() + " ");

        System.out.print(") => ceil(50%) ==> ( ");

        for (int i = 0; i < finalresult.size(); i++)
            System.out.print((int) Math.ceil((finalresult.get(i).getQuantum()) * 0.5) + " ");
        System.out.print(")");
    }

    // Sorting Processes in List
    private void sortProcesses(int time) {
        this.mProcesses.sort((process1, process2) -> {
            if (process1.getArrivalTime() <= time && process2.getArrivalTime() <= time) {
                if (process1.getAGFactor() < process2.getAGFactor())
                    return -1;
                else
                    return 1;
            } else if (process1.getArrivalTime() < process2.getArrivalTime())
                return -1;
            else
                return 1;
        });
    }

    private int calcMean() {
        int sum = 0;
        int numberOfProcesses = this.NumOfProcesses;
        // loop over all processes in the mProcesses
        for (Process p : mProcesses) {
            // loop over all processes in the current time
            sum += p.getQuantum();
        }
        double tmp = 0.1 * (sum / (double) numberOfProcesses);
        return (int) Math.ceil(tmp);
    }

    // Main function to run the scheduler
    @Override
    public void startScheduler() {
        int time = 0; // current time flow
        int indexOfProcess = 0;
        int lastProcessAGFactor = 0;
        String name = ""; // name to get the process from ready queue or smallest AG
        boolean finished = false;

        printUpdates(time);
        while (this.mProcesses.size() != 0) {
            sortProcesses(time); // sort processes based on arrival time and AG factor

            if (!name.equals("")) { // to get the index of the process name
                for (int i = 0; i < this.mProcesses.size(); i++) {
                    if (this.mProcesses.get(i).getName().equals(name)) {
                        indexOfProcess = i;
                        break;
                    }
                }
            }
            name = ""; // to reset the name

            // Non-Preemptive
            if (this.mProcesses.get(indexOfProcess).getArrivalTime() <= time) {
                Process tempProcess = this.mProcesses.get(indexOfProcess);
                tempProcess.setTempQuantum(this.mProcesses.get(indexOfProcess).getTempQuantum());
                int tempQT = tempProcess.getQuantum();

                System.out.println("  " + tempProcess.getName() + " is running! ");

                if (lastProcessAGFactor != tempProcess.getAGFactor()) {
                    this.mOutput.add(tempProcess);
                    lastProcessAGFactor = tempProcess.getAGFactor();
                }

                int fHalfQT = (int) Math.ceil(tempQT * 0.5); // first half of quantem
                int remainingHalfQT = (int) (tempQT - fHalfQT); // second half of quantem

                // finish the first half of the quantum
                while (tempProcess.getBurstTime() > 0 && fHalfQT != 0) {
                    tempProcess.setBurstTime(tempProcess.getBurstTime() - 1);
                    fHalfQT--;
                    time++;
                    // Check if the process is not the last process in the execution order list to
                    // prevent duplicates
                    if (mOutput.isEmpty() || mOutput.get(mOutput.size() - 1) != tempProcess)
                        // Add the process to the execution order list
                        mOutput.add(tempProcess);
                }

                // Preemptive
                while (tempProcess.getBurstTime() >= 0 && remainingHalfQT >= 0) {
                    int index = 0;
                    boolean flag = false;
                    Process nextProcess = null;

                    // to check if any process with smaller AG factor in current time
                    for (index = 0; index < this.mProcesses.size(); index++) {
                        if (this.mProcesses.get(index).getArrivalTime() <= time) {
                            nextProcess = this.mProcesses.get(index);
                            if (this.mProcesses.get(index).getAGFactor() < tempProcess.getAGFactor()) {
                                flag = true;
                                break;
                            }
                        }
                    }

                    if (flag) { // there is a process with smaller AG Factor
                        tempProcess.setQuantum(remainingHalfQT + tempProcess.getQuantum());

                        if (nextProcess != null && !(nextProcess.getName().equals(tempProcess.getName()))) {
                            boolean in = false; // flag to check if there is a process in ready queue with the same name
                                                // of temp process
                            for (int j = 0; j < this.readyQueue.size(); j++) {
                                if (this.readyQueue.get(j).getName().equals(tempProcess.getName())) {
                                    in = true;
                                    break;
                                }
                            }
                            if (!in) {
                                name = nextProcess.getName();
                                this.readyQueue.add(tempProcess);
                            }
                        }
                        break;
                    } else { // there isn't a process with smaller AG Factor
                        if (nextProcess != null && !(nextProcess.getName().equals(tempProcess.getName()))) {
                            boolean in = false;
                            for (int j = 0; j < this.readyQueue.size(); j++) {
                                if (this.readyQueue.get(j).getName().equals(nextProcess.getName())) {
                                    in = true;
                                    break;
                                }
                            }
                            if (!in) {
                                this.readyQueue.add(nextProcess);
                            }
                        }
                    }

                    // Process Quantum is done but there is still some progress to do
                    if (remainingHalfQT == 0 && tempProcess.getBurstTime() != 0) {
                        Process tempQueueProcess = this.readyQueue.get(0); // get the first process in the ready queue
                        name = tempQueueProcess.getName();

                        int mean = calcMean();
                        tempProcess.setQuantum(tempProcess.getQuantum() + mean); // Add 10% of the mean of quantum to
                                                                                 // the quantum

                        boolean in = false;
                        for (int j = 0; j < this.readyQueue.size(); j++) {
                            if (this.readyQueue.get(j).getName().equals(tempProcess.getName())) {
                                in = true;
                                break;
                            }
                        }
                        if (!in)
                            this.readyQueue.add(tempProcess);

                        this.readyQueue.remove(0); // remove first element of the queue
                        break;
                    }

                    // Process finished
                    if (tempProcess.getBurstTime() == 0) {
                        // Set process crucial data
                        tempProcess.setCompletionTime(time);
                        tempProcess.setTurnAroundTime(tempProcess.getCompletionTime() - tempProcess.getArrivalTime());
                        tempProcess.setWaitingTime(tempProcess.getTurnAroundTime() - tempProcess.getRemainingTime());
                        this.avgWaitingTime += tempProcess.getWaitingTime();
                        this.avgTurnaroundTime += tempProcess.getTurnAroundTime();

                        if (this.mProcesses.size() == 0 && this.readyQueue.size() == 0) {
                            finished = true;
                            break;
                        }

                        while (this.readyQueue.size() > 0) {
                            Process firstProcess = this.readyQueue.get(0);
                            if (firstProcess.getBurstTime() == 0) {
                                this.readyQueue.remove(0);
                            } else {
                                break;
                            }
                        }
                        // Check if the queue is not empty before accessing elements
                        if (!this.readyQueue.isEmpty()) {
                            Process tempQueueProcess = this.readyQueue.get(0);
                            name = tempQueueProcess.getName();
                            tempProcess.setQuantum(0);
                            this.mDeadList.add(tempProcess);
                            this.mProcesses.remove(tempProcess);
                            this.readyQueue.remove(0);
                            break;
                        } else {// Check if the queue is empty before accessing elements
                            tempProcess.setQuantum(0);
                            this.mDeadList.add(tempProcess);
                            this.mProcesses.remove(tempProcess);
                            break;
                        }
                    }
                    remainingHalfQT--; // decrease the remaining second half quantum
                    time++; // increase the flow of time
                    tempProcess.setBurstTime(tempProcess.getBurstTime() - 1); // decrease burst time with one
                    // Check if the process is not the last process in the execution order list to
                    // prevent duplicates
                    if (mOutput.isEmpty() || mOutput.get(mOutput.size() - 1) != tempProcess)
                        // Add the process to the execution order list
                        mOutput.add(tempProcess);

                }
                if (finished) // if the mProcesses is empty and ready queue is empty
                    break;
                else
                    printUpdates(time); // print the processes updates
            }
        }
        // Calculate the average waiting & turnaround times
        this.avgWaitingTime /= this.NumOfProcesses;
        this.avgTurnaroundTime /= this.NumOfProcesses;
        // Print the name of the scheduler
        System.out.println("\n\n\t\tAG Scheduler");
        // Call the main function of the abstract class
        super.PrintOUTPUT();
    }
}
