import java.util.List;

// Abstract scheduler class to apply any scheduling algorithm
public abstract class Scheduler {
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

    public void simulate() {
        startScheduler();
        PrintOUTPUT();
    }
}
