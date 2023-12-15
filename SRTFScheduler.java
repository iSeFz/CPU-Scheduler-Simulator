import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

// Scheduler for Shortest Remaining Time First (SRTF)
public class SRTFScheduler extends Scheduler {
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