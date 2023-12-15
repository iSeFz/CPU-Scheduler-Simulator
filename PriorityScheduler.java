import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

// Scheduler for Non-Preemptive Priority Scheduling
public class PriorityScheduler extends Scheduler {
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
