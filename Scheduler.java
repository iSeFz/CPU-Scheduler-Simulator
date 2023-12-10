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
    public void startScheduler() {
        // Simple counter to make printing more readable
        Integer counter = 1;

        // Print the execution order of the processes
        System.out.println("\tExecution Order Of Processes");
        for (Process p : this.executionOrder())
            System.out.println(counter++ + ". " + p.getName());

        counter = 1; // Reset the counter
        // Print the waiting time of the processes
        System.out.println("\tWaiting Time Of Processes");
        for (Integer time : this.waitingTime())
            System.out.println("Process #" + counter++ + ": " + time);

        counter = 1; // Reset the counter
        // Print the turnaround time of the processes
        System.out.println("\tTurnaround Time Of Processes");
        for (Integer time : this.turnaroundTime())
            System.out.println("Process #" + counter++ + ": " + time);

        // Print the average waiting time of the processes
        System.out.println("Average Waiting Time: " + this.avgWaitingTime());
        // Print the average turnaround time of the processes
        System.out.println("Average Turnaround Time: " + this.avgTurnaroundTime());
    }
}
