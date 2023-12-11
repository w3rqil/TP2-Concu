
import java.util.concurrent.Semaphore;
import Jama.Matrix;
import java.util.ArrayList;


public class Monitor {

    private PetriNet petrinet; 
    private Policy policy;
    private Semaphore enMonitor;
    private CQueues conditionQueues;
   // hay q hacer la colas de condición 

    public Monitor (PetriNet petrinet, Policy policy)
    {
        this.petrinet = petrinet;
        this.policy = policy;
        this.enMonitor = new Semaphore(1, true);
    }


    /* 
     * *************************
     * **** Método  TRONCAL ****
     * *************************
     */


    public void fireTransition(Matrix v)
    {
        try {
            catchMonitor();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

    //----------------------------------------------
    // revisar tema enum
    //
    if(petrinet.fundamentalEquation(v) == null || !(petrinet.workingState(v).equals("transitionState.NONE_WORKING"))) {

        exitMonitor();
        
        int queue = conditionQueues.getQueue(v);
        
        try {
            conditionQueues.getQueues().get(queue).acquire();
            if(petrinet.testCondition()) return; //Si un hilo se despierta en este punto y ya se completaron 1000 tareas, debe salir sin disparar nada.
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    }


    /*
     * *************************
     * *** Métodos públicos ****
     * *************************
     */
    public void catchMonitor() throws InterruptedException
    {
        enMonitor.acquire();
    }

    public void exitMonitor() 
    {
        enMonitor.release();
    }







    /*
     * *************************
     * *** Getters & Setters ***
     * *************************
     */
}
