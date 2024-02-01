import java.util.HashMap;
import java.util.concurrent.Semaphore;
import Jama.Matrix;

public class Monitor {

    private PetriNet petrinet;
    private Policy policy;
    private Semaphore mutex;
    private CQueues conditionQueues;
    private HashMap<Long, Long> timeLeft;

    private int deadThreads;

    public Monitor(PetriNet petrinet, Policy policy) {
        this.conditionQueues = new CQueues();
        this.petrinet = petrinet;
        this.policy = policy;
        this.timeLeft = new HashMap<Long, Long>();
        this.mutex = new Semaphore(1, true);
        this.deadThreads = 0;
    }

    public CQueues getConditionQueues() {
        return conditionQueues;
    }

    public void addDeadThreads() {
        this.deadThreads++;
    }

    public Semaphore getMutex() {
        return mutex;
    }

    /*
     * *************************
     * *** PRINCIPAL METHOD  ***
     * *************************
     */

    /*
     * The method checks if the transition is enabled an "time enabled" calling the 'testTime' method.
     * In case all conditions are met, the transition is fired and the method returns true.
     * Otherwise, the thread is queued up and the method returns false.
     *
     * @param v: firing vector
     * @return true if the transition is fired, false otherwise
     */
    public boolean fireTransition(Matrix v) {

        // printHash();
        try {
            if (petrinet.getCompletedInvariants() < 200) {
                catchMonitor();
            } else {
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // -----------------------------------------------
        boolean k = true;


            if (petrinet.fundamentalEquationTest(v)
                    && ((petrinet.workingState(v) == 0) || (petrinet.workingState(v) == 2)))
            {
                if (testTime(v))
                {
                    k = true;
                } else
                {
                    petrinet.setWorkingVector(v, (double) Thread.currentThread().getId());
                    k = false;
                    exitMonitor();
                    return false;
                }
            } else
            {
                k = false;
            }
            if (k)
            {

                petrinet.fire(v);
                Matrix sensibilized = petrinet.getSensibilized();

                Matrix queued = conditionQueues.queuedUp();
                Matrix and = sensibilized.arrayTimes(queued); //  'and' '&'

                int m = result(and); // sensibilized and queued transitions
                if (m > 0) // queued and enabled transitions
                {
                    // cual
                    int choice = policy.fireChoice(and);
                    // release
                    conditionQueues.getQueued().get(choice).release();

                    System.out.println("Thread ID: " + Thread.currentThread().getId() + " wakes up");

                } else // there's no transition enabled and queued
                {
                    System.out.println("k turns to false");
                    k = false;
                }
            } else
            {
                exitMonitor();

                int queue = conditionQueues.getQueue(v);

                try {
                    conditionQueues.getQueued().get(queue).acquire();
                    if (petrinet.getCompletedInvariants() >= 200)
                        return false;
                } catch(Exception e) {
                    System.err.println("❌  current thread is interrupted   ❌");
                    System.exit(1);     // Stop the program with a non-zero exit code
                }

            }

        exitMonitor();
        return true;
    }

    /*
     * Test if the transition is "time enabled". Checking if the elapsed time since
     * the sensibilization of the transition is greater than the alpha time.
     * Returns true if the transition is "time enabled", if it's not, returns false and save's the remaining time.
     *
     * @param v: firing vector
     * @return true if the transition is "time enabled", false otherwise
     */
    private boolean testTime(Matrix v)
    {
        long time = System.currentTimeMillis();
        long alpha = (long) petrinet.getAlphaTimes().get(0, getIndex(v));
        long initTime = (long) petrinet.getSensibilizedTime().get(0, getIndex(v));
        if (alpha < (time - initTime) || alpha == 0) {
            return true;
        } else {
            setTimeLeft(Thread.currentThread().getId(), alpha - (time - initTime));
            return false;
        }
    }

    /*
     * *************************
     * **** PUBLIC  METHODS ****
     * *************************
     */

    public void printDeadThreads()
    {
        System.out.println("Dead threads: " + deadThreads + "/14");
    }

    public void catchMonitor() throws InterruptedException {
        mutex.acquire();
    }

    public void exitMonitor() {
        mutex.release();
    }

    /*
     * Returns the number of enabled and queued transitions.
     *
     * @param and: matrix resulting from the 'and' operation between the sensibilized and queued transitions.
     * @return the number of enabled and queued transitions.
     */
    public int result(Matrix and)
    {
        int m = 0;

        for (int i = 0; i < and.getColumnDimension(); i++)
            if (and.get(0, i) > 0)
                m++;

        return m;
    }

    /*
     * Returns the index of the transition that is going to be fired.
     *
     * @param v: firing vector
     * @return index of the transition
     */
    private int getIndex(Matrix vector) {
        int index = 0;

        for (int i = 0; i < vector.getColumnDimension(); i++) {
            if (vector.get(0, i) == 1)
                break;
            else
                index++;
        }

        return index;
    }

    /*
     * *************************
     * *** Getters & Setters ***
     * *************************
     */

    public synchronized long getTimeLeft(long id) {
        return this.timeLeft.get(id);
    }

    private synchronized void setTimeLeft(long id, long time) {
        timeLeft.put(id, time);
    }

    public PetriNet getPetriNet() {
        return this.petrinet;
    }
}