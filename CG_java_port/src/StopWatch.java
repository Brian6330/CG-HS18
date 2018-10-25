public class StopWatch {

    private double startTime, endTime;

    /**
     * Start time measurement
     */
    public void start() {
        startTime = System.nanoTime();
    }

    /**
     * Stop time measurement.
     *
     * @return elapsed time in ms
     */
    public double stop() {
        endTime = System.nanoTime();
        return elapsed();
    }

    /**
     * @return elapsed time in ms (watch has to be stopped)
     */
    public double elapsed() {
        return (endTime - startTime) / 1_000_000;
    }

    public String toString() {
        return elapsed() - (elapsed() % 0.1) + " ms";
    }
}
