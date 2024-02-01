import Jama.Matrix;

public class Main {
    private static final int amountThreads = 7;
    private static final String policyType = "Equitative";
    //private static final String policyType = "8020";
    private static PetriNet petrinet;               // Petri net representative of the system.
    private static Monitor monitor;                 // Monitor that will control the Petri net that models the system.
    private static double[] loaderLeft = { 0,2 };

    private static double[] loaderRight = { 1,3 };

    private static double[] resizerLeft = { 4, 6 };

    private static double[] resizerRight = { 5,7 };

    private static double[] improverLeft = { 8,10 };

    private static double[] improverRight = { 9,11 };

    private static double[] exit = { 12,13 };

    // arrays with transitions associated with threads
    private static Matrix loaderLeftPath = new Matrix(loaderLeft, 1);         // transposed version of de loaderLeft
    private static Matrix loaderRightPath = new Matrix(loaderRight, 1);       // transposed version of de loaderRight
    private static Matrix resizerLeftPath = new Matrix(resizerLeft, 1);       // transposed version of de resizerLeft
    private static Matrix resizerRightPath = new Matrix(resizerRight, 1);     // transposed version of de resizerRight
    private static Matrix improverLeftPath = new Matrix(improverLeft, 1);     // transposed version of de improverLeft
    private static Matrix improverRightPath = new Matrix(improverRight, 1);   // transposed version of de improverRight
    private static Matrix exitPath = new Matrix(exit, 1);                     // transposed version of de exit

    /**
     * Main method.
     *
     * Here the threads with their associated paths are instantiated and executed.
     * Both the Petri net are also initialized with their initial marking
     * like the monitor and the logger thread.
     */

    public static void main(String args[]) {
        Long initTime = System.currentTimeMillis();
        petrinet = new PetriNet();

        Policy policy = new Policy(policyType);
        System.out.println("Policy type: " + policyType + " \n");

        monitor = new Monitor(petrinet, policy);

        Threads[] threads = new Threads[amountThreads];

        petrinet.enableTransitions();

        try {
            long startTime = System.currentTimeMillis();
            Log log = new Log(petrinet, monitor, startTime, true);
            log.start();

            Log transition = new Log(petrinet, monitor, startTime, false);
            transition.start();

        } catch (Exception e) {
            System.err.println("❌ Error creating logger. ❌");
            System.exit(1);     // Stop the program with a non-zero exit code
        }

        threads[0] = new Threads(loaderLeftPath, monitor, "Loader Left");
        threads[1] = new Threads(loaderRightPath, monitor, "Loader Right");
        threads[2] = new Threads(resizerLeftPath, monitor, "Resizer Left");
        threads[3] = new Threads(resizerRightPath, monitor, "Resizer Right");
        threads[4] = new Threads(improverLeftPath, monitor, "Improver Left");
        threads[5] = new Threads(improverRightPath, monitor, "Improver Right");
        threads[6] = new Threads(exitPath, monitor, "Exit");


        for (Threads thread : threads) {
            thread.start();
        }
        try {
            for (Threads thread : threads) {
                thread.join();
            }
        }
        catch(Exception e) {
            System.err.println("❌ I can't wait, I'm tired. ❌");
            System.exit(1);     // Stop the program with a non-zero exit code
        }
        Long finalTime = System.currentTimeMillis();
        //monitor.printDeadThreads();
        System.out.println("\nEnding program!");
        System.out.println(petrinet.transitionsCounterInfo());
        System.out.println("\nT invariants:");
        petrinet.tInvariantsInfo();
        System.out.println("\nElapsed Time: " + (double)((finalTime-initTime)/1000.0) + " seconds");
    }
}
