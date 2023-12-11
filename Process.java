/* Process Class To Manage Process Related Attributes & Methods */
public class Process {
                        /* Process Related Attributes */
    // General Process Attributes
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int waitingTime;
    private int turnaroundTime;
    private int completionTime;
    // Related ONLY to Priority & AG Scheduling
    private int priorityNumber;
    // Related ONLY To AG Scheduling
    private int quantum;
    private int agFactor;
    // Related ONLY to SJF & AG Scheduling
    private int remainingTime;

    // Constructor to initialize or create new Process
    public Process(String name, int arrivalTime, int burstTime) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.waitingTime = 0;
    }

                        /* Getters for process attributes */
    public String getName() {
        return this.name;
    }

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    public int getBurstTime() {
        return this.burstTime;
    }

    public int getWaitingTime() {
        return this.waitingTime;
    }

    public int getTurnAroundTime() {
        return this.turnaroundTime;
    }

    public int getCompletionTime() {
        return this.completionTime;
    }

    public int getPriority() {
        return this.priorityNumber;
    }

    public int getQuantum() {
        return this.quantum;
    }

    public int getAGFactor() {
        return this.agFactor;
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

                        /* Setters for process attributes */
    public void setName(String name) {
        this.name = name;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnAroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public void setPriority(int priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public void setAGFactor(int agFactor) {
        this.agFactor = agFactor;
    }
}
