import java.util.Arrays;

public class ThreadScheduler {

    /** maximum optimum threads the JVM supports */
    private final int MAX_THRADS;

    private Thread[] threads;

    /**
     * Constructs a simple thread creator for the ray tracer.
     */
    public ThreadScheduler() {
        MAX_THRADS = Runtime.getRuntime().availableProcessors();
    }

    /**
     * Creates threads (but not starts them) for the specified function.
     * The upper_bound serves the maximum number of operations to be done.
     * These will be split up between each created thread.
     *
     * @param f the function to perform for i = 0 to upper_bound
     * @param upper_bound max operations
     */
    public void createThreads(final Function f, final int upper_bound) {
        assert (MAX_THRADS == Runtime.getRuntime().availableProcessors());

        // operations per thread
        final int ops = Math.min(1, upper_bound / MAX_THRADS);
        this.threads = new Thread[Math.min(upper_bound, MAX_THRADS)];

        // leftover operations due to precision loss on dividing integers
        final int leftover = upper_bound % MAX_THRADS;

        // lower bounds to use
        final int[] lower_bounds = new int[threads.length];

        // calculate lower bounds
        lower_bounds[0] = 0;
        for (int i=1; i<lower_bounds.length; i++) {
            lower_bounds[i] = lower_bounds[i-1] + ops;
            if (i == lower_bounds.length-1) lower_bounds[i] += leftover;
        }

        // setup threads
        Arrays.setAll(threads, i -> new Thread(() -> {
            // set limit according
            int limit = i+1 < lower_bounds.length ? lower_bounds[i+1] : upper_bound;
            for (int j=lower_bounds[i]; j<limit; j++)
                f.run(j);
        }));
    }

    /**
     * Starts all previously created threads.
     *
     * @throws RuntimeException if no threads were previously created
     */
    public void start() {
        if (threads == null || threads[0] == null) throw new RuntimeException("Threads not created");
        for (Thread t : threads) t.start();
    }

    public void waitForFinish() {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This interface may be used to create lambda functions with only one integer argument.
     */
    public interface Function {
        public void run(int x);
    }
}
