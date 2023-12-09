import java.util.List;

// Scheduler for Non-Preemptive Priority Scheduling
public class PriorityScheduler extends Scheduler {
    private List<Process> processes;
    private double avgWaitingTime;
    private double avgTurnaroundTime;

    // Constructor to initialize the scheduler with a list of processes
    public PriorityScheduler(List<Process> processes) {
        this.processes = processes;
        this.avgWaitingTime = 0;
        this.avgTurnaroundTime = 0;
    }

    // Return the execution order of the processes
    public List<Process> executionOrder() {
        // To be implemented
        return null;
    }

    // Return the waiting time of the processes
    public List<Integer> waitingTime() {
        // To be implemented
        return null;
    }
    
    // Return the turnaround time of the processes
    public List<Integer> turnaroundTime() {
        // To be implemented
        return null;
    }

    // Return the average waiting time of the processes
    public double avgWaitingTime() {
        // To be implemented
        return avgWaitingTime;
    }

    // Return the average turnaround time of the processes
    public double avgTurnaroundTime() {
        // To be implemented
        return avgTurnaroundTime;
    }
    
    // Main function to run the scheduler
    public void startScheduler() {
        // Print the name of the scheduler
        System.out.println("\tNon-Preemptive Priority Scheduler");
        // Call the main function of the abstract class
        super.startScheduler(this);
    }
}
