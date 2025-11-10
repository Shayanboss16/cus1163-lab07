import java.util.*;

public class RoundRobinLab {

    static class Process {
        int id;
        int arrivalTime;
        int burstTime;

        int remainingTime;
        int completionTime;
        int turnaroundTime;
        int waitingTime;

        public Process(int id, int arrivalTime, int burstTime) {
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.remainingTime = burstTime;
        }

        public Process(Process p) {
            this.id = p.id;
            this.arrivalTime = p.arrivalTime;
            this.burstTime = p.burstTime;
            this.remainingTime = p.remainingTime;
            this.completionTime = p.completionTime;
            this.turnaroundTime = p.turnaroundTime;
            this.waitingTime = p.waitingTime;
        }
    }

    public static void scheduleRoundRobin(List<Process> processes, int timeQuantum) {
        int currentTime = 0;
        Deque<Process> ready = new ArrayDeque<>(processes);

        while (!ready.isEmpty()) {
            Process p = ready.removeFirst();

            int exec = Math.min(timeQuantum, p.remainingTime);
            currentTime += exec;
            p.remainingTime -= exec;

            if (p.remainingTime > 0) {
                ready.addLast(p);
            } else {
                p.completionTime = currentTime;
            }
        }

        for (Process p : processes) {
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
        }
    }

    private static void printReport(String title, List<Process> procs, int timeQuantum) {
        System.out.println("========================================");
        System.out.println(title);
        System.out.println("========================================\n");
        System.out.println("Time Quantum: " + timeQuantum + "ms");
        System.out.println("----------------------------------------");
        System.out.println("Process | Arrival | Burst | Completion | Turnaround | Waiting");

        double sumTAT = 0, sumWT = 0;
        for (Process p : procs) {
            sumTAT += p.turnaroundTime;
            sumWT  += p.waitingTime;
            System.out.printf("   %2d   |   %2d    |  %3d  |    %4d     |    %4d     |   %3d%n",
                    p.id, p.arrivalTime, p.burstTime, p.completionTime, p.turnaroundTime, p.waitingTime);
        }
        System.out.printf("Average Turnaround Time: %.2fms%n", sumTAT / procs.size());
        System.out.printf("Average Waiting Time: %.2fms%n", sumWT / procs.size());
        System.out.println("========================================");
        System.out.println();
    }

    public static void main(String[] args) {
        List<Process> base = Arrays.asList(
                new Process(1, 0, 7),
                new Process(2, 0, 4),
                new Process(3, 0, 2)
        );

        List<Process> runQ3 = new ArrayList<>();
        for (Process p : base) runQ3.add(new Process(p));
        scheduleRoundRobin(runQ3, 3);
        printReport("Round Robin Scheduling Simulator", runQ3, 3);

        List<Process> runQ5 = new ArrayList<>();
        for (Process p : base) runQ5.add(new Process(p));
        scheduleRoundRobin(runQ5, 5);
        printReport("Round Robin Scheduling Simulator", runQ5, 5);
    }
}
