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
    private int tempQuantum;
    private int agFactor;
    // Related ONLY to SRTF & AG Scheduling
    private int remainingTime;

    // Default Constructor
    public Process() {
        this.name = "";
        this.arrivalTime = 0;
        this.burstTime = 0;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completionTime = 0;
        this.priorityNumber = 0;
        this.quantum = 0;
        this.tempQuantum = 0;
        this.agFactor = 0;
        this.remainingTime = 0;
    }
    
    // Constructor to initialize or create new Process
    public Process(String name, int arrivalTime, int burstTime) {
        this(); // Calling Default Constructor To Initialize Attributes
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
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

    public int getTempQuantum() {
        return this.tempQuantum;
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

    public void setTempQuantum(int tempQuantum) {
        this.tempQuantum = tempQuantum;
    }

    public void setAGFactor(int agFactor) {
        this.agFactor = agFactor;
    }
}
