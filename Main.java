import java.util.ArrayList;
import java.util.Scanner;

// Main class to run the program
public class Main {
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
        ArrayList<Process> processList = new ArrayList<>(nProcesses);
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
        scanner.close();

        /* // For Testing
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
        
        */
    }
}
