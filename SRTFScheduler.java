import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Scheduler for Shortest Remaining Time First (SRTF)
public class SRTFScheduler extends Scheduler {
    private List<Process> processes;
    private int currentTime;
    private double avgWaitingTime;
    private double avgTurnaroundTime;

    // Constructor to initialize the scheduler with a list of processes
    public SRTFScheduler(List<Process> processes) {
        this.processes = processes;
        this.currentTime = 0;
        this.avgWaitingTime = 0;
        this.avgTurnaroundTime = 0;
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

    // Sort the list in ascending order based on arrival time
    public void sortByArrivalTime(List<Process> arrivedProcesses) {
        Collections.sort(arrivedProcesses, Comparator.comparingInt(Process::getArrivalTime));
    }

    // Sort the ready queue in ascending order based on remaining time
    public void sortByRemainingTime(List<Process> readyQueue) {
        Collections.sort(readyQueue, Comparator.comparingInt(Process::getRemainingTime));
    }


    // Function to initialize remaining time for every process
    private void initializeRemainingTime() {
        for (Process process : processes) {
            process.setRemainingTime(process.getBurstTime());
        }
    }

    // Main function to run the scheduler
    @Override
    public void startScheduler() {
        // List to store the final process execution order
        List<Process> executionOrderList = new ArrayList<>();
        // List to store the arrived processes ready for execution
        List<Process> readyQueue = new ArrayList<>();
        // Total number of processes to be scheduled
        int nProcesses = processes.size();

        // Initialize remaining time for every process
        initializeRemainingTime();

        // Continue scheduling until all processes are executed
        while (!processes.isEmpty()) {
            // some prints just to trace the code
            System.out.println("Current Time: " + currentTime);
            System.out.println("Processes: " + processes);
            System.out.println("Ready Queue: " + readyQueue);
            System.out.println("Execution Order: " + executionOrderList);

            // Check for arrived processes and add them to the ready queue
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            // Sort processes in the ready queue by arrival time
            sortByArrivalTime(readyQueue);
            // Then sort by remaining time (in case of ties in arrival time)
            sortByRemainingTime(readyQueue);

            // Get the process with the shortest remaining time in the readyQueue
            Process currentProcess = readyQueue.get(0);

            // Update waiting time and remaining time for the current process
            currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);

            // Increment the time by 1 second
            currentTime += 1;

            // If the current process finishes its execution
            if (currentProcess.getRemainingTime() == 0) {
                // Set completion time, turnaround time, and update averages
                currentProcess.setCompletionTime(currentTime);
                currentProcess.setTurnAroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                avgWaitingTime += currentProcess.getWaitingTime();
                avgTurnaroundTime += currentProcess.getTurnAroundTime();

                // Remove the finished process from the ready queue and processes list
                readyQueue.remove(currentProcess);
                processes.remove(currentProcess);

                // Add the process to the execution order list
                executionOrderList.add(currentProcess);
            }
        }

        // Print scheduler information
        System.out.println("\t\tShortest Remaining Time First (SRTF) Scheduler");
        super.PrintOUTPUT();
    }

}