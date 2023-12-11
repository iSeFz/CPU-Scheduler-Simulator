public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priorityNumber;// for priority and AG scheduling.
    int WaitingTime;
    int TurnAroundTime;
    int CompletionTime;
    int RemainingTime; // for SJF and AG scheduling.
    int Quantum; // for AG scheduling.
    int tempQuantum; // for AG scheduling.
    int AGFactor; // for AG scheduling.

    // Constructor to initialize or create new Process
    public Process(String name, int arrivalTime, int burstTime) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }

    // Getters for process attributes
    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priorityNumber;
    }

    // Setters for process attributes
    public void setName(String name) {
        this.name = name;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public void setPriority(int priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    // Setter and Getter for WaitingTime
    public int getWaitingTime() {
        return WaitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        WaitingTime = waitingTime;
    }

    // Setter and Getter for TurnAroundTime
    public int getTurnAroundTime() {
        return TurnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        TurnAroundTime = turnAroundTime;
    }

    // Setter and Getter for CompletionTime
    public int getCompletionTime() {
        return CompletionTime;
    }

    public void setCompletionTime(int completionTime) {
        CompletionTime = completionTime;
    }

    // Setter and Getter for RemainingTime
    public int getRemainingTime() {
        return RemainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        RemainingTime = remainingTime;
    }

    // Setter and Getter for Quantum
    public int getQuantum() {
        return Quantum;
    }

    public void setQuantum(int quantum) {
        Quantum = quantum;
    }

    // Setter and Getter for AGFactor
    public int getAGFactor() {
        return AGFactor;
    }

    public void setAGFactor(int agFactor) {
        AGFactor = agFactor;
    }

    public void setTempQuantum(int tempQuantum) {
        this.tempQuantum = tempQuantum;
    }

    public int getTempQuantum() {
        return tempQuantum;
    }
}
