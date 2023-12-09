import java.util.ArrayList;

// Main class to run the program
public class Main {
    public static void main(String[] args) {
        // Create a list of processes
        ArrayList<Process> processes = new ArrayList<>();
        // Create the processes
        Process p1 = new Process("P1", 0, 4, 3);
        Process p2 = new Process("P2", 1, 3, 2);
        Process p3 = new Process("P3", 1, 5, 1);
        // Add the processes to the list
        processes.add(p1);
        processes.add(p2);
        processes.add(p3);
        // Create a scheduler for the processes
        PriorityScheduler priority = new PriorityScheduler(processes);
        priority.startScheduler();
        
    }
}
