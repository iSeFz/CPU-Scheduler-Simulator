import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

// Main class to run the program
class Main {
    public static void main(String[] args) {
        System.out.println("\tWelcome to our CPU Scheduler Simulator!");
        // Get the required input from the user at the start of the program
        Scanner scanner = new Scanner(System.in);
        System.out.print("Number of processes? ");
        int nProcesses = scanner.nextInt();

        System.out.print("Context switching time? ");
        int contextSwitchingTime = scanner.nextInt();

        System.out.print("Round robin quantum time? ");
        int quantumTime = scanner.nextInt();

        scanner.nextLine(); // Clean the input stream

        // Create a list with the entered number of processes
        List<Process> processList = new ArrayList<>(nProcesses);
        // Get each process data from the user
        for (int i = 0; i < nProcesses; ++i) {
            System.out.println("\tProcess #" + (i + 1) + " Data");
            System.out.print("Enter name: ");
            String pName = scanner.nextLine();
            System.out.print("Enter arrival time: ");
            int pArrivalTime = scanner.nextInt();
            System.out.print("Enter burst time: ");
            int pBurstTime = scanner.nextInt();
            System.out.print("Enter priority number: ");
            int pPriorityNumber = scanner.nextInt();
            scanner.nextLine(); // Clean the input stream

            // Create the process object with the entered data
            Process newProcess = new Process(pName, pArrivalTime, pBurstTime);
            newProcess.setQuantum(quantumTime);
            newProcess.setPriority(pPriorityNumber);
            processList.add(newProcess);
        }

        // Allow the user to choose from different scheduling algorithms
        System.out.println("\n\tAvailable Scheduling Algorithms" +
                "\n1. Non-Preemptive Shortest Job First\n" +
                "2. Shortest Remaining Time First\n" +
                "3. Non-Preemptive Priority Scheduling\n" +
                "4. AG Scheduling");
        System.out.print("Choose scheduling algorithm to apply (1-4) >> ");
        int choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println("\tMaintenance in progress!");
            ShortestJobScheduler SJF = new ShortestJobScheduler(processList, contextSwitchingTime);
            SJF.startScheduler();
        } else if (choice == 2) {
            System.out.println("\tMaintenance in progress!");
            SRTFScheduler SRTF = new SRTFScheduler(processList);
            SRTF.startScheduler();
        } else if (choice == 3) {
            PriorityScheduler priority = new PriorityScheduler(processList);
            priority.startScheduler();
        } else if (choice == 4) {
            AGScheduler AG = new AGScheduler(processList);
            AG.startScheduler();
        }
        scanner.close();
        System.out.println("\tThanks for using our CPU Scheduler Simulator!");

        /* // SRTF Test Case
        Process p1 = new Process("P1", 0, 8);
        Process p2 = new Process("P2", 1, 4);
        Process p3 = new Process("P3", 2, 9);
        Process p4 = new Process("P4", 3, 5);
        // Add the processes to the list
        List<Process> processes = new ArrayList<>();
        processes.add(p1);
        processes.add(p2);
        processes.add(p3);
        processes.add(p4);
        // Create a scheduler for the processes
        SRTFScheduler srtf = new SRTFScheduler(processes);
        srtf.startScheduler(); */
        
        /* // Priority Test Case
        Process p1 = new Process("P1", 0, 17);
        p1.setPriority(4);
        Process p2 = new Process("P2", 3, 6);
        p2.setPriority(9);
        Process p3 = new Process("P3", 4, 10);
        p3.setPriority(2);
        Process p4 = new Process("P4", 29, 4);
        p4.setPriority(1);
        // Add the processes to the list
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(p1);
        processes.add(p2);
        processes.add(p3);
        processes.add(p4);
        // Create a scheduler for the processes
        PriorityScheduler priority = new PriorityScheduler(processes);
        priority.startScheduler(); */

        /* // AG Assignment Sheet Test Case
        Process p1 = new Process("P1", 0, 17);
        p1.setPriority(4);
        p1.setQuantum(4);
        p1.setAGFactor(20);
        Process p2 = new Process("P2", 3, 6);
        p2.setPriority(9);
        p2.setQuantum(4);
        p2.setAGFactor(17);
        Process p3 = new Process("P3", 4, 10);
        p3.setPriority(2);
        p3.setQuantum(4);
        p3.setAGFactor(16);
        Process p4 = new Process("P4", 29, 4);
        p4.setPriority(8);
        p4.setQuantum(4);
        p4.setAGFactor(43);
        // Add the processes to the list
        List<Process> processes = new ArrayList<>();
        processes.add(p1);
        processes.add(p2);
        processes.add(p3);
        processes.add(p4);
        // Create a scheduler for the processes
        AGScheduler ag = new AGScheduler(processes);
        ag.startScheduler(); */
    }
}

// Abstract scheduler class to apply any scheduling algorithm
abstract class Scheduler {
    // Return the execution order of the processes
    public abstract List<Process> executionOrder();

    // Return the waiting time of the processes
    public abstract List<Integer> waitingTime();

    // Return the turnaround time of the processes
    public abstract List<Integer> turnaroundTime();

    // Return the average waiting time of the processes
    public abstract double avgWaitingTime();

    // Return the average turnaround time of the processes
    public abstract double avgTurnaroundTime();

    // Main function to run the scheduler
    public abstract void startScheduler();

    // Common method between different scheduling algorithms
    // Used to print the desired output after simulating the scheduling
    public void PrintOUTPUT() {
        // Print the execution order of the processes
        System.out.println("\tExecution Order Of Processes");
        for (Process p : this.executionOrder())
            System.out.print(" => " + p.getName());

        // Print the waiting time of the processes
        System.out.println("\n\tWaiting Time Of Processes");
        for (Integer time : this.waitingTime())
            System.out.print(" => " + time);

        // Print the turnaround time of the processes
        System.out.println("\n\tTurnaround Time Of Processes");
        for (Integer time : this.turnaroundTime())
            System.out.print(" => " + time);

        // Print the average waiting time of the processes
        System.out.println("\nAverage Waiting Time: " + this.avgWaitingTime());
        // Print the average turnaround time of the processes
        System.out.println("Average Turnaround Time: " + this.avgTurnaroundTime());
    }
}

// Non-Preemptive Shortest Job First Scheduling
class ShortestJobScheduler extends Scheduler {
    private List<Process> processes;
    private double avgWaitingTime;
    private double avgTurnaroundTime;
    private int contextTime;

    // this class to sort based on burst time
    private class compareBurst implements java.util.Comparator<Process> {
        @Override
        public int compare(Process o1, Process o2) {
            return o1.getBurstTime() - o2.getBurstTime();
        }
    }

    // to sort based on arrived time
    private class compareArrivedTime implements java.util.Comparator<Process> {
        @Override
        public int compare(Process p1, Process p2) {
            return p1.getArrivalTime() - p2.getArrivalTime();
        }
    }

    // Default constructor
    public ShortestJobScheduler() {
        this.processes = new ArrayList<Process>();
        this.avgWaitingTime = 0;
        this.avgTurnaroundTime = 0;
        this.contextTime = 0;
    }

    // Initialize the scheduler with a list of processes & the context switching time
    public ShortestJobScheduler(List<Process> list, int c) {
        this();
        this.processes = list;
        this.avgTurnaroundTime = 0;
        this.avgWaitingTime = 0;
        this.contextTime = c;
    }

    // Return the execution order of the processes
    @Override
    public List<Process> executionOrder() { return this.processes; }

    // Return the waiting time of the processes
    @Override
    public List<Integer> waitingTime() {
        List<Integer> waitingTimeList = new ArrayList<Integer>();
        for (Process process : processes)
            waitingTimeList.add(process.getWaitingTime());
        return waitingTimeList;
    }

    // Return the turnaround time of the processes
    @Override
    public List<Integer> turnaroundTime() {
        List<Integer> turnaroundTimeList = new ArrayList<Integer>();
        for (Process process : processes)
            turnaroundTimeList.add(process.getTurnAroundTime());
        return turnaroundTimeList;
    }

    // Return the average waiting time of the processes
    @Override
    public double avgWaitingTime() { return this.avgWaitingTime; }

    // Return the average turnaround time of the processes
    @Override
    public double avgTurnaroundTime() { return this.avgTurnaroundTime; }

    // Main function to run the scheduler
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
        System.out.println("\t\tShortest Job First Scheduler:\n");
        super.PrintOUTPUT();
    }
}

// Preemptive Shortest Remaining Time First (SRTF)
class SRTFScheduler extends Scheduler {
    private List<Process> processes;
    private int currentTime;
    private double avgWaitingTime;
    private double avgTurnaroundTime;
    
    // Default constructor
    public SRTFScheduler() {
        this.processes = new ArrayList<Process>();
        this.currentTime = 0;
        this.avgTurnaroundTime = 0;
        this.avgWaitingTime = 0;
    }

    // Constructor to initialize the scheduler with a list of processes
    public SRTFScheduler(List<Process> processes) {
        this(); // Calling default constructor to set default values
        this.processes = processes;
    }

    // Return the execution order of the processes
    @Override
    public List<Process> executionOrder() { return this.processes; }

    // Return the waiting time of the processes
    @Override
    public List<Integer> waitingTime() {
        List<Integer> waitingTimeList = new ArrayList<Integer>();
        for (Process process : processes)
            waitingTimeList.add(process.getWaitingTime());
        return waitingTimeList;
    }

    // Return the turnaround time of the processes
    @Override
    public List<Integer> turnaroundTime() {
        List<Integer> turnaroundTimeList = new ArrayList<Integer>();
        for (Process process : processes)
            turnaroundTimeList.add(process.getTurnAroundTime());
        return turnaroundTimeList;
    }

    // Return the average waiting time of the processes
    @Override
    public double avgWaitingTime() { return this.avgWaitingTime; }

    // Return the average turnaround time of the processes
    @Override
    public double avgTurnaroundTime() { return this.avgTurnaroundTime; }

    // Main function to run the scheduler
    @Override
    public void startScheduler() {
        // List to store the final process execution order
        List<Process> executionOrderList = new ArrayList<Process>();
        // List to store the arrived processes
        List<Process> readyQueue = new ArrayList<Process>();
        // Save the size of the original processes list
        int numOfProcesses = processes.size();
        // Sort the list in ascending order based on arrival time
        processes.sort(Comparator.comparingInt(process -> process.getArrivalTime()));
        // Apply Actual Scheduling Simulation
        while (!processes.isEmpty()) {
            // If there are no arrived processes at the current time
            if (readyQueue.isEmpty()) {
                // Add the first process in the processes list to the ready queue
                readyQueue.add(processes.get(0));
                // If the first process arrival time is greater than the current time
                if (readyQueue.get(0).getArrivalTime() > currentTime)
                    // Move the current time forward to the first process arrival time
                    currentTime = readyQueue.get(0).getArrivalTime();
            }
            // Continue adding processes to the ready queue based on the current time
            for (Process process : processes)
                // Check for duplicates before adding the process to the ready queue
                if (process.getArrivalTime() <= currentTime && !readyQueue.contains(process))
                    readyQueue.add(process);
            // Sort processes after every process execution to maintain the order of execution
            readyQueue.sort(Comparator.comparingInt(process -> process.getRemainingTime()));
            // Store the shortest remaining time process, it'll always be the first element
            // As the list is sorted in ascending order based on the remaining time of the processes
            Process currentProcess = readyQueue.get(0);
            // Decrement its remaining time by 1, meaning it has executed 1 time unit
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
            currentTime++; // Move time forward by 1 second
            // Check if the process is not the last process in the execution order list to prevent duplicates
            if (executionOrderList.isEmpty() || executionOrderList.get(executionOrderList.size() - 1) != currentProcess)
                // Add the process to the execution order list
                executionOrderList.add(currentProcess);
            // If the process finished its execution
            if (currentProcess.getRemainingTime() == 0) {
                // Remove process from the ready queue & the original list as it has been executed
                readyQueue.remove(currentProcess);
                processes.remove(currentProcess);
                // Set the process completion time by the current time
                currentProcess.setCompletionTime(currentTime);
                // Set the process turnaround time relative to its arrival time
                currentProcess.setTurnAroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                // Set the process waiting time
                currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getBurstTime());
                // Increment the average waiting time by the current process waiting time
                avgWaitingTime += currentProcess.getWaitingTime();
                // Increment the average turnaround time by the current process turnaround time
                avgTurnaroundTime += currentProcess.getTurnAroundTime();
            }
        }
        // Save the execution order list back into the processes list
        processes = executionOrderList;
        // Calculate the average waiting & turnaround times
        avgWaitingTime /= numOfProcesses;
        avgTurnaroundTime /= numOfProcesses;
        // Print the name of the scheduler
        System.out.println("\t\tShortest Remaining Time First (SRTF) Scheduler");
        // Call the main function of the abstract class
        super.PrintOUTPUT();
    }
}

// Non-Preemptive Priority Scheduling
class PriorityScheduler extends Scheduler {
    private List<Process> processes;
    private int currentTime;
    private double avgWaitingTime;
    private double avgTurnaroundTime;

    // Default constructor
    public PriorityScheduler() {
        this.processes = new ArrayList<Process>();
        this.currentTime = 0;
        this.avgWaitingTime = 0;
        this.avgTurnaroundTime = 0;
    }

    // Constructor to initialize the scheduler with a list of processes
    public PriorityScheduler(List<Process> processes) {
        this(); // Calling Default Constructor To Initialize Default Values
        this.processes = processes;
    }

    // Return the execution order of the processes
    @Override
    public List<Process> executionOrder() { return this.processes; }

    // Return the waiting time of the processes
    @Override
    public List<Integer> waitingTime() {
        List<Integer> waitingTimeList = new ArrayList<Integer>();
        for (Process process : processes)
            waitingTimeList.add(process.getWaitingTime());
        return waitingTimeList;
    }

    // Return the turnaround time of the processes
    @Override
    public List<Integer> turnaroundTime() {
        List<Integer> turnaroundTimeList = new ArrayList<Integer>();
        for (Process process : processes)
            turnaroundTimeList.add(process.getTurnAroundTime());
        return turnaroundTimeList;
    }

    // Return the average waiting time of the processes
    @Override
    public double avgWaitingTime() { return this.avgWaitingTime; }

    // Return the average turnaround time of the processes
    @Override
    public double avgTurnaroundTime() { return this.avgTurnaroundTime; }

    // Main function to run the scheduler
    @Override
    public void startScheduler() {
        // Temporary list to save the final process execution order
        List<Process> executionOrderList = new ArrayList<Process>();
        // Temporary list to save the arrived processes
        List<Process> readyQueue = new ArrayList<Process>();
        // Save the size of the original processes list
        int nProcesses = processes.size();
        // Sort the list in ascending order based on arrival time
        processes.sort(Comparator.comparingInt(process -> process.getArrivalTime()));
        // Apply Actual Scheduling Simulation
        while (executionOrderList.size() != nProcesses) {
            // If there are no arrived processes at the current time
            if (readyQueue.isEmpty()) {
                // Add the first process in the processes list to the ready queue
                readyQueue.add(processes.get(0));
                // If the first process arrival time is greater than the current time
                if (readyQueue.get(0).getArrivalTime() > currentTime)
                    // Move the current time forward to the first process arrival time
                    currentTime = readyQueue.get(0).getArrivalTime();
            }
            // Continue adding processes to the ready queue based on the current time
            for (Process process : processes)
                // Check for duplicates before adding the process to the ready queue
                if (process.getArrivalTime() <= currentTime && !readyQueue.contains(process))
                    readyQueue.add(process);
            // Sort processes after every process execution to maintain the order
            // The list is sorted in ascending order based on priority number, smallest number is the highest priority
            readyQueue.sort(Comparator.comparingInt(process -> process.getPriority()));
            // Store the highest priority process based on the arrival time & priority
            Process currentProcess = readyQueue.get(0);
            // Increment the time by the burst time of the current running process
            currentTime += currentProcess.getBurstTime();
            // Set the completion time of the current process by the current time
            currentProcess.setCompletionTime(currentTime);
            // Set the process turnaround time relative to its arrival time
            currentProcess.setTurnAroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
            // Set the process waiting time relative to its burst time
            currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getBurstTime());
            // Increment the average waiting time by the current process waiting time
            avgWaitingTime += currentProcess.getWaitingTime();
            // Increment the average turnaround time by the current process turnaround time
            avgTurnaroundTime += currentProcess.getTurnAroundTime();
            // Add the process to the execution order list
            executionOrderList.add(currentProcess);
            // Remove process from the ready queue & the original list as it has been executed
            readyQueue.remove(currentProcess);
            processes.remove(currentProcess);
        }
        // Save the execution order list back into the processes list
        processes = executionOrderList;
        // Calculate the average waiting & turnaround times
        avgWaitingTime /= processes.size();
        avgTurnaroundTime /= processes.size();
        // Print the name of the scheduler
        System.out.println("\t\tNon-Preemptive Priority Scheduler");
        // Call the main function of the abstract class
        super.PrintOUTPUT();
    }
}

// AG Scheduling
class AGScheduler extends Scheduler {
    private List<Process> mProcesses;
    private List<Process> mOutput;
    private List<Process> mDeadList;
    private List<Process> readyQueue;
    private int NumOfProcesses;
    private double avgTurnaroundTime;
    private double avgWaitingTime;

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

    // Calculate quantum mean of current processes
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
    public List<Process> executionOrder() { return this.mOutput; }

    // Return the waiting time of the processes
    @Override
    public List<Integer> waitingTime() {
        List<Integer> waitingTimeList = new ArrayList<Integer>();
        // Sort the finished processes by name
        this.mDeadList.sort(Comparator.comparing(Process::getName));
        // Add the waiting time of each process to the list
        for (Process process : this.mDeadList)
            waitingTimeList.add(process.getWaitingTime());
        System.out.println("Note: Processes' waiting times are sorted alphabetically!");
        return waitingTimeList;
    }

    // Return the turnaround time of the processes
    @Override
    public List<Integer> turnaroundTime() {
        List<Integer> turnaroundTimeList = new ArrayList<Integer>();
        // Sort the finished processes by name
        this.mDeadList.sort(Comparator.comparing(Process::getName));
        // Add the waiting time of each process to the list
        for (Process process : this.mDeadList)
            turnaroundTimeList.add(process.getTurnAroundTime());
        System.out.println("Note: Processes' turnaround times are sorted alphabetically!");
        return turnaroundTimeList;
    }

    // Return the average waiting time of the processes
    @Override
    public double avgWaitingTime() { return this.avgWaitingTime; }

    // Return the average turnaround time of the processes
    @Override
    public double avgTurnaroundTime() { return this.avgTurnaroundTime; }

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
