public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priority;

    // Constructor to initialize or create new Process
    public Process(String pName, int pArrivalTime, int pBurstTime, int pPriority) {
        name = pName;
        arrivalTime = pArrivalTime;
        burstTime = pBurstTime;
        priority = pPriority;
    }

    // Getters for process attributes
    public String getName() { return name; }

    public int getArrivalTime() { return arrivalTime; }

    public int getBurstTime() { return burstTime; }

    public int getPriority() { return priority; }

    // Setters for process attributes
    public void setName(String name) { this.name = name; }

    public void setArrivalTime(int arrivalTime) { this.arrivalTime = arrivalTime; }

    public void setBurstTime(int burstTime) { this.burstTime = burstTime; }

    public void setPriority(int priority) { this.priority = priority; }
}
