
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import Jama.Matrix;
//import java.util.ArrayList;

public class Monitor {

    private PetriNet petrinet;
    private Policy policy;
    private Semaphore mutex;
    private CQueues conditionQueues;
    private HashMap<Long,Long> timeLeft;

    private int deadThreads;



    public Monitor(PetriNet petrinet, Policy policy) {
        this.conditionQueues= new CQueues();
        this.petrinet = petrinet;
        this.policy = policy;
        this.timeLeft= new HashMap<Long, Long>();
        this.mutex = new Semaphore(1 );
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
     * **** Método TRONCAL *****
     * *************************
     *
     */

    public void printHash() {
        System.out.println("Hashmap: -----------------------");
        for (long id: timeLeft.keySet()) {
            String value = timeLeft.get(id).toString();
            System.out.println(id + " " + value);
        }
    }

    public boolean fireTransition(Matrix v)
    {

        //printHash();
        try
        {
            if(petrinet.getCompletedInvariants()<200) {
                catchMonitor();
            }
            else{
                return false;
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // -----------------------------------------------
        boolean k = true;
        while (k )
        {
            System.out.println("Firing vector: ");
            v.print(2,0);
            if(petrinet.fundamentalEquationTest(v)&&(petrinet.workingState(v)==0))
            {
                if(testTime(v)){
                    k = true;
                }else{
                    petrinet.setWorkingVector(v, (double)Thread.currentThread().getId());
                    k=false;
                    exitMonitor();
                    return false;
                }
            }else {
                k = false;
            }
            if(k){

                petrinet.fire(v);
                Matrix sensibilized = petrinet.getSensibilized();
                //sensibilized.print(0,2);

                Matrix queued = conditionQueues.queuedUp();
                Matrix and = sensibilized.arrayTimes(queued); // operación 'and'

                int m=result(and); //sensibilizadas y encoladas
                if (m > 0) // hay transiciones habilitadas y encoladas
                {
                    // cual
                    int choice = policy.fireChoice(and);
                    // release
                    conditionQueues.getQueued().get(choice).release();

                    System.out.println("Thread ID: " + Thread.currentThread().getId() + " wakes up");

                } else // no hay transiciones habilitadas y encoladas
                {
                    System.out.println("k turns to false");
                    k = false;
                }
            } else {
                exitMonitor();
                int queue = conditionQueues.getQueue(v);
                try {
                    conditionQueues.getQueued().get(queue).acquire();
                    if(petrinet.getCompletedInvariants()>=200) return false;
                } catch ( InterruptedException e){
                    e.printStackTrace();
                }

            }

        }

        exitMonitor();
        return true;
    }

    private boolean testTime(Matrix v)
    {
        long time = System.currentTimeMillis();
        long alpha = (long) petrinet.getAlphaTimes().get(0, getIndex(v));
        long initTime = (long) petrinet.getSensibilizedTime().get(0, getIndex(v));
        System.out.println("tiempo actual "+time+" y time menos init " + ( time - initTime) + "\n alpha: "+ alpha);
        if(alpha <( time - initTime) || alpha==0){
            System.out.println("alpha retorna true");
            return true;
        }else{
            System.out.println("alpha retorna FALSE");
            setTimeLeft(Thread.currentThread().getId(), alpha- (time - initTime));
            return false;
        }
    }

    public String backState(){
        if(petrinet.getCompletedInvariants() >= 200){
            return "si";
        }
        return "no";
    }
    /*
     * *************************
     * *** Métodos públicos ****
     * *************************
     */
    public void catchMonitor() throws InterruptedException {    mutex.acquire();    }

    public void exitMonitor() {                 //BORRAR
        mutex.release();
    }

    public int result(Matrix and) {
        int m = 0;

        for (int i = 0; i < and.getColumnDimension(); i++)
            if (and.get(0, i) > 0)
                m++;

        return m;
    }

    private int getIndex(Matrix vector) {
        int index = 0;

        for(int i = 0; i < vector.getColumnDimension(); i++) {
            if(vector.get(0, i) == 1) break;
            else index++;
        }

        return index;
    }
    /*
     * *************************
     * *** Getters & Setters ***
     * *************************
     */
    public synchronized long getTimeLeft(long id){ return this.timeLeft.get(id);}
    private synchronized void setTimeLeft(long id, long time) {
        timeLeft.put(id, time);
    }

    public PetriNet getPetriNet() {
        return this.petrinet;
    }

    public void printDaDead(){
        System.out.println("Dead threads: " + deadThreads + "/14");
    }
}
