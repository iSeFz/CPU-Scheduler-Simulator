import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Scheduler for Non-Preemptive Priority Scheduling
public class PriorityScheduler extends Scheduler {
    private List<Process> processes;
    private int contextSwitchingTime;
    private int currentTime;
    private double avgWaitingTime;
    private double avgTurnaroundTime;

    // Constructor to initialize the scheduler with a list of processes
    public PriorityScheduler(List<Process> processes, int contextSwitchingTime) {
        this.processes = processes;
        this.contextSwitchingTime = contextSwitchingTime;
        this.currentTime = 0;
        this.avgWaitingTime = 0;
        this.avgTurnaroundTime = 0;
    }

    // Return the execution order of the processes
    @Override
    public List<Process> executionOrder() {
        // Custom comparator to compare between processes based on arrival time
        Comparator<Process> atComparator = Comparator.comparingInt(process -> process.getArrivalTime());
        // Custom comparator to compare between processes based on priority + current time
        // In order to apply Aging solution to solve the starvation
        // That means incrementing the priority of processes relative to the current time
        Comparator<Process> priorityComparator = Comparator.comparingInt(process -> process.getPriority() + currentTime);
        // Custom comparator to merge the two comparators
        Comparator<Process> customComparator = atComparator.thenComparing(priorityComparator);
        // Sort the list in ascending order based on custom comparator
        Collections.sort(processes, customComparator);
        // Return the sorted list
        return processes;
    }

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

    // Search for the suitable next process to execute based on the arrival time
    public Process findNextProcess() {
        for (Process process : processes)
            if (process.getArrivalTime() <= currentTime)
                return process;
        return null;
    }

    // Main function to run the scheduler
    @Override
    public void startScheduler() {
        // Temporary list to save the resulted process execution order
        List<Process> resultList = new ArrayList<Process>();
        // Apply Actual Scheduling Simulation
        while (!processes.isEmpty()) {
            // Sort processes after every process execution to maintain the order
            // Smallest number is the highest priority
            processes = executionOrder();
            // Store the highest priority process based on the arrival time & priority
            Process currentProcess = findNextProcess();
            // Check for the returned value to continue scheduling
            if (currentProcess != null) {
                // Set the process waiting time relative to its arrival time
                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
                // Increment the time by the burst time of the current running process
                currentTime += currentProcess.getBurstTime();
                // Set the completion time of the current process by the current time
                currentProcess.setCompletionTime(currentTime);
                // Set the process turnaround time relative to its arrival time
                currentProcess.setTurnAroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                // Increment the average waiting time by the current process waiting time
                avgWaitingTime += currentProcess.getWaitingTime();
                // Increment the average turnaround time by the current process turnaround time
                avgTurnaroundTime += currentProcess.getTurnAroundTime();
                // Add the process to the result list
                resultList.add(currentProcess);
                // Remove process from the list as it has been executed
                processes.remove(currentProcess);
                // Increment the current time with the context switching time
                currentTime += contextSwitchingTime;
            }
            else // No process with higher priority available, move time forward
                currentTime++;
        }
        // Save the result list back into the processes list
        processes = resultList;
        // Calculate the average waiting & turnaround times
        avgWaitingTime /= processes.size();
        avgTurnaroundTime /= processes.size();
        // Print the name of the scheduler
        System.out.println("\t\tNon-Preemptive Priority Scheduler");
        // Call the main function of the abstract class
        super.PrintOUTPUT();
    }
}
