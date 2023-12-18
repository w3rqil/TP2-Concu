import java.util.*;
import java.util.concurrent.Semaphore;
import Jama.Matrix;

public class CQueues {

    private final int maxQueues = 14;
    private ArrayList<Semaphore> conditionQueues;

    public CQueues() {
        conditionQueues = new ArrayList<Semaphore>();
        for (int i = 0; i < maxQueues; i++) {
            conditionQueues.add(new Semaphore(0));
        }
    }

    /*
     * *************************
     * **** PUBLIC  METHODS ****
     * *************************
     */

    /*
     * *************************
     * *** getters y setters ***
     * *************************
     */

    public ArrayList<Semaphore> getQueued() {
        return conditionQueues;
    }

    /*
     * Returns the index of the first thread queued up for the transition associated with the vector v.
     * 
     * @param v: firing vector
     * @return the index of the first thread queued up for the transition associated with the vector v
     */
    public int getQueue(Matrix v) {
        int index = 0;

        for (int i = 0; i < v.getColumnDimension(); i++) {
            if (v.get(0, i) == 1)
                break;
            else
                index++;
        }
        return index;
    }

    /*
     * Returns the number of threads queued up transitions.
     * 
     * @param 
     * @return the number of threads queued up for the transition associated with
     */
    public Matrix queuedUp() {
        double[] aux = new double[this.maxQueues];
        for (Semaphore queue : conditionQueues) {
            if (queue.hasQueuedThreads())
                aux[conditionQueues.indexOf(queue)] = 1;
            else
                aux[conditionQueues.indexOf(queue)] = 0;
        }
        Matrix waitingThreads = new Matrix(aux, 1);
        return waitingThreads;
    }
}
