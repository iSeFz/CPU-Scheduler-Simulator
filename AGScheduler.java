import java.util.*;

public class AGScheduler extends Scheduler {
    //Scanner Input;
    int NumOfProcesses;
    ArrayList<Process> mProcesses;
    ArrayList<Process> mTemp;
    ArrayList<Process> mOutput;
    ArrayList<Process> mDeadList;
    // ArrayList<Integer> mTimeLine;
    ArrayList<Process> readyQueue;
    //int TimeLine; //
    //int MaxTime;
    int TotalQuantum;
    int TotalTurnAround;
    int TotalWaitingTime;
    int ContextSwitching;

    // Default constructor
    public AGScheduler() {
        //this.Input = new Scanner(System.in);
        this.NumOfProcesses = 0;
        this.mProcesses = new ArrayList<>();
        //this.TimeLine = 0;
        this.mTemp = new ArrayList<>();
        this.mOutput = new ArrayList<>();
        this.mDeadList = new ArrayList<>();
        this.readyQueue = new ArrayList<>();
        //this.MaxTime = 0;
        this.TotalQuantum = 0;
        this.TotalTurnAround = 0;
        this.TotalWaitingTime = 0;
    }

    // Parameterized constructor
    public AGScheduler(ArrayList<Process> processList, int ContextSwitching) {
        this(); // Calling the default constructor to set the default values
        this.mProcesses = processList;
        this.NumOfProcesses = mProcesses.size();
        this.ContextSwitching = ContextSwitching;

        for (Process process : mProcesses) {
            // Calculate & set the process AG factor
            //calculateAGFactor(process);
            // Set the process temp quantum
            process.setTempQuantum(process.getQuantum());
            process.setRemainingTime(process.getBurstTime());

            /*this.MaxTime = process.getBurstTime();
            if (this.MaxTime < process.getBurstTime() + process.getArrivalTime())
                this.MaxTime = process.getBurstTime() + process.getArrivalTime();*/
        }
    }

    @Override
    public List<Process> executionOrder() {
        return mOutput;
    }

    @Override
    public List<Integer> waitingTime() {
        List<Integer> waitingTimeList = new ArrayList<Integer>();
        waitingtimeCalculations();
        for (Process process : mProcesses) {
            waitingTimeList.add(process.getWaitingTime());
        }
        return waitingTimeList;
    }

    @Override
    public List<Integer> turnaroundTime() {
        List<Integer> turnAroundList = new ArrayList<Integer>();
        turnAroundCalculations();
        for (Process process : mProcesses) {
            turnAroundList.add(process.getTurnAroundTime());
        }
        return turnAroundList;
    }

    @Override
    public double avgWaitingTime() {
        waitingtimeCalculations();
        return this.TotalWaitingTime / this.NumOfProcesses;
    }

    @Override
    public double avgTurnaroundTime() {
        turnAroundCalculations();
        return this.TotalTurnAround / this.NumOfProcesses;
    }

    public void turnAroundCalculations() {
        for (int i = 0; i < this.mProcesses.size(); i++) {
            this.mProcesses.get(i).setTurnAroundTime(
                    this.mProcesses.get(i).getCompletionTime() - this.mProcesses.get(i).getArrivalTime());
            this.TotalTurnAround += this.mProcesses.get(i).getTurnAroundTime();
        }
    }

    public void waitingtimeCalculations() {
        for (int i = 0; i < this.mProcesses.size(); i++) {
            this.mProcesses.get(i).setWaitingTime(
                    this.mProcesses.get(i).getTurnAroundTime() - this.mProcesses.get(i).getBurstTime());
            this.TotalWaitingTime += this.mProcesses.get(i).getWaitingTime();
        }
    }

    // Calculate & set the AG Factor for a certain process
    void calculateAGFactor(Process P) {
        int agFactor;
        // Generate a random number between 0 (inclusive) and 21 (exclusive)
        Random random = new Random();
        int randomNumber = random.nextInt(21);
        // Compute the formula based on the assignment sheet
        if (randomNumber < 10)
            agFactor = randomNumber + P.getArrivalTime() + P.getBurstTime();
        else if (randomNumber > 10)
            agFactor = 10 + P.getArrivalTime() + P.getBurstTime();
        else
            agFactor = P.getPriority() + P.getArrivalTime() + P.getBurstTime();
        // Set the process ag factor after computing it via the formula
        P.setAGFactor(agFactor);
    }

    // Print [Time ] -> Quantum ( )
    public void printUpdates(int time) {
        ArrayList<Process> tempProcesses = this.mProcesses;
        ArrayList<Process> tempDead = this.mDeadList;
        ArrayList<Process> finalresult = new ArrayList<>();

        for (int i = 0; i < tempProcesses.size(); i++) {
            finalresult.add(tempProcesses.get(i));
        }

        for (int i = 0; i < tempDead.size(); i++) {
            finalresult.add(tempDead.get(i));
        }

        finalresult.sort(Comparator.comparing(Process::getName));

        System.out.print("[Time: " + time + "] -> ");
        System.out.print("Quantum ( ");

        for (int i = 0; i < finalresult.size(); i++)
            System.out.print(finalresult.get(i).getQuantum() + " ");

        System.out.print(") -> ceil(50%) = ( ");

        for (int i = 0; i < finalresult.size(); i++)
            System.out.print((int) Math.ceil((finalresult.get(i).getQuantum()) * 0.5) + " ");
        System.out.println(")		");
    }

    // Sorting Processes in List
    public void sortProcesses(int time) {
        this.mProcesses.sort((o1, o2) -> {
            if (o1.getArrivalTime() <= time && o2.getArrivalTime() <= time) {
                if (o1.getAGFactor() < o2.getAGFactor())
                    return -1;
                else
                    return 1;
            } else if (o1.getArrivalTime() < o2.getArrivalTime())
                return -1;
            else
                return 1;
        });
    }

    public void isFinished() {
        boolean x = true;
        while (x) {
            for (int i = 0; i < this.mProcesses.size(); i++) {
                if (this.readyQueue.get(0).getAGFactor() == this.mProcesses.get(i).getAGFactor())
                    x = false;
            }

            if (x)
                this.readyQueue.remove(0);
        }
    }

    public int calcMean() {
        int sum = 0;
        int numberOfProcesses = this.NumOfProcesses;
        // loop over all processes in the mProcesses
        for (Process p : mProcesses) {
            // loop over all processes in the current time
            sum += p.getQuantum();
        }
        double tmp = 0.1 * (sum / (double) numberOfProcesses);
        return (int) Math.ceil(tmp);
    }


    @Override
    public void startScheduler() {
        int time = 0; // current time flow
        int indexOfProcess = 0;
        int lastProcessAGFactor = 0;
        String name = ""; // name to get the process from ready queue or smallest AG
        boolean finished = false;

        while (this.mProcesses.size() != 0) {
            sortProcesses(time); // sort processes based on arrivel time and AG factor

            if (!name.equals("")) { // to get the index of the process name
                for (int i = 0; i < this.mProcesses.size(); i++) {
                    if (this.mProcesses.get(i).getName().equals(name)) {
                        indexOfProcess = i;
                        break;
                    }
                }
            }
            name = ""; // to reset the name

            // Non-Preemptive
            if (this.mProcesses.get(indexOfProcess).getArrivalTime() <= time) {
                Process tempProcess = this.mProcesses.get(indexOfProcess);
                tempProcess.setTempQuantum(this.mProcesses.get(indexOfProcess).getTempQuantum());
                int tempQT = tempProcess.getQuantum();

                System.out.print("Process " + tempProcess.getName() + " is running.");

                if (lastProcessAGFactor != tempProcess.getAGFactor()) {
                    this.mOutput.add(tempProcess);
                    lastProcessAGFactor = tempProcess.getAGFactor();
                }


                int fHalfQT = (int) Math.ceil(tempQT  * 0.5); // first half of quantem
                int remainingHalfQT = (int) (tempQT - fHalfQT); // second half of quantem

                // finish the first half of the quantum
                while (tempProcess.getBurstTime() > 0 && fHalfQT != 0) {
                    tempProcess.setBurstTime(tempProcess.getBurstTime() - 1);
                    fHalfQT--;
                    time++;
                }


                // Preemptive
                while (tempProcess.getBurstTime() >= 0 && remainingHalfQT >= 0) {
                    int index = 0;
                    boolean flag = false;
                    Process nextProcess = null;


                    // to check if any process with smaller AG factor in current time
                    for (index = 0; index < this.mProcesses.size(); index++) {
                        if (this.mProcesses.get(index).getArrivalTime() <= time) {
                            nextProcess = this.mProcesses.get(index);
                            if (this.mProcesses.get(index).getAGFactor() < tempProcess.getAGFactor()) {
                                flag = true;
                                break;
                            }
                        }
                    }


                    if (flag) { // there is a process with smaller AG Factor
                        tempProcess.setQuantum(remainingHalfQT + tempProcess.getQuantum());

                        if (nextProcess != null && !(nextProcess.getName().equals(tempProcess.getName()))) {
                            boolean in = false; // flag to check if there is a process in ready queue with the same name of temp process
                            for (int j = 0; j < this.readyQueue.size(); j++) {
                                if (this.readyQueue.get(j).getName().equals(tempProcess.getName())) {
                                    in = true;
                                    break;
                                }
                            }
                            if (!in) {
                                name = nextProcess.getName();
                                this.readyQueue.add(tempProcess);
                            }
                        }
                        break;
                    }
                    else { // there isn't a process with smaller AG Factor
                        if (nextProcess != null && !(nextProcess.getName().equals(tempProcess.getName()))) {
                            boolean in = false;
                            for (int j = 0; j < this.readyQueue.size(); j++) {
                                if (this.readyQueue.get(j).getName().equals(nextProcess.getName())) {
                                    in = true;
                                    break;
                                }
                            }
                            if (!in) {
                                this.readyQueue.add(nextProcess);
                            }
                        }
                    }

                    // Process Quantum is done but there is still some progress to do
                    if (remainingHalfQT == 0 && tempProcess.getBurstTime() != 0) {
                        Process tempQueueProcess = this.readyQueue.get(0); // get the first process in the ready queue
                        name = tempQueueProcess.getName();

                        int mean = calcMean();
                        tempProcess.setQuantum(tempProcess.getQuantum() + mean); // Add 10% of the mean of quantum to the quantum

                        boolean in = false;
                        for (int j = 0; j < this.readyQueue.size(); j++) {
                            if (this.readyQueue.get(j).getName().equals(tempProcess.getName())) {
                                in = true;
                                break;
                            }
                        }
                        if (!in)
                            this.readyQueue.add(tempProcess);

                        this.readyQueue.remove(0); // remove first element of the queue
                        break;
                    }

                    // Process finished
                    if (tempProcess.getBurstTime() == 0) {
                        if (this.mProcesses.size() == 0 && this.readyQueue.size() == 0) {
                            finished = true;
                            break;
                        }

                        while (this.readyQueue.size() > 0) {
                            Process firstProcess = this.readyQueue.get(0);
                            if (firstProcess.getBurstTime() == 0) {
                                this.readyQueue.remove(0);
                            }
                            else {
                                break;
                            }
                        }
                        // Check if the queue is not empty before accessing elements
                        if (!this.readyQueue.isEmpty()) {
                            Process tempQueueProcess = this.readyQueue.get(0);
                            name = tempQueueProcess.getName();
                            tempProcess.setQuantum(0);
                            this.mDeadList.add(tempProcess);
                            this.mProcesses.remove(tempProcess);
                            this.readyQueue.remove(0);
                            break;
                        } else {// Check if the queue is empty before accessing elements
                            tempProcess.setQuantum(0);
                            this.mDeadList.add(tempProcess);
                            this.mProcesses.remove(tempProcess);
                            break;
                        }
                    }
                    remainingHalfQT--; // decrease the remaining second half quantum
                    time++; // increase the flow of time
                    tempProcess.setBurstTime(tempProcess.getBurstTime() - 1); // decrease burst time with one

                }
                if (finished) // if the mProcesses is empty and ready queue is empty
                    break;
                else
                    printUpdates(time); // print the processes updates
            }
        }
    }
}
