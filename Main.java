import java.util.ArrayList;
import java.util.List;
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
            ShortestJobScheduler SJF = new ShortestJobScheduler(processList,
                    contextSwitchingTime);
            SJF.startScheduler();
        } else if (choice == 2) {
            System.out.println("\tMaintenance in progress!");
            SRTFScheduler SRTF = new SRTFScheduler(processList);
            SRTF.startScheduler();
        } else if (choice == 3) {
            PriorityScheduler priority = new PriorityScheduler(processList,
                    contextSwitchingTime);
            priority.startScheduler();
        } else if (choice == 4) {
            AGScheduler AG = new AGScheduler(processList);
            AG.startScheduler();
        }
        scanner.close();
        System.out.println("\tThanks for using our CPU Scheduler Simulator!");

        /*
         * // Priority Test Case
         * Process p1 = new Process("P1", 0, 4);
         * p1.setPriority(3);
         * Process p2 = new Process("P2", 2, 1);
         * p2.setPriority(2);
         * Process p3 = new Process("P3", 4, 2);
         * p3.setPriority(4);
         * Process p4 = new Process("P4", 6, 1);
         * p4.setPriority(1);
         * Process p5 = new Process("P5", 12, 5);
         * p5.setPriority(5);
         * // Add the processes to the list
         * ArrayList<Process> processes = new ArrayList<>();
         * processes.add(p1);
         * processes.add(p2);
         * processes.add(p3);
         * processes.add(p4);
         * processes.add(p5);
         * // Create a scheduler for the processes
         * PriorityScheduler priority = new PriorityScheduler(processes, 2);
         * priority.startScheduler();
         */

        // AG Assignment Sheet Test Case
        // Process p1 = new Process("P1", 0, 17);
        // p1.setPriority(4);
        // p1.setQuantum(4);
        // p1.setAGFactor(20);
        // Process p2 = new Process("P2", 3, 6);
        // p2.setPriority(9);
        // p2.setQuantum(4);
        // p2.setAGFactor(17);
        // Process p3 = new Process("P3", 4, 10);
        // p3.setPriority(2);
        // p3.setQuantum(4);
        // p3.setAGFactor(16);
        // Process p4 = new Process("P4", 29, 4);
        // p4.setPriority(8);
        // p4.setQuantum(4);
        // p4.setAGFactor(43);
        // // Add the processes to the list
        // List<Process> processes = new ArrayList<>();
        // processes.add(p1);
        // processes.add(p2);
        // processes.add(p3);
        // processes.add(p4);
        // // Create a scheduler for the processes
        // AGScheduler ag = new AGScheduler(processes);
        // ag.startScheduler();
        // }
    }
}
