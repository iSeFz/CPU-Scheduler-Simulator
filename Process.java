public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priorityNumber;

    // Constructor to initialize or create new Process
    public Process(String name, int arrivalTime, int burstTime, int priorityNumber) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
    }

    // Getters for process attributes
    public String getName() { return name; }

    public int getArrivalTime() { return arrivalTime; }

    public int getBurstTime() { return burstTime; }

    public int getPriority() { return priorityNumber; }

    // Setters for process attributes
    public void setName(String name) { this.name = name; }

    public void setArrivalTime(int arrivalTime) { this.arrivalTime = arrivalTime; }

    public void setBurstTime(int burstTime) { this.burstTime = burstTime; }

    public void setPriority(int priorityNumber) { this.priorityNumber = priorityNumber; }
}
