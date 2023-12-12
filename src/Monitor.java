
import java.util.concurrent.Semaphore;
import Jama.Matrix;
import java.util.ArrayList;

public class Monitor {

    private PetriNet petrinet;
    private Policy policy;
    private Semaphore mutex;
    private CQueues conditionQueues;
    private int tInvariantsCounter;
    private InvariantsManager invariantsManager;
    // hay q hacer la colas de condición

    public Monitor(PetriNet petrinet, Policy policy) {
        this.petrinet = petrinet;
        this.policy = policy;
        this.mutex = new Semaphore(1, true);
        this.tInvariantsCounter = 0;
        this.invariantsManager = new InvariantsManager();
    }

    /*
     * *************************
     * **** Método TRONCAL *****                 pene 8============================================================D
     * *************************
     * 
     */
    // gestor del monitor
    public boolean fireTransition(Matrix v) 
    {
        try {
            catchMonitor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // -----------------------------------------------
        boolean k = true;
        while (k) 
        {
            // esto sería el equivalente a k=false. Se manda a dormir al hilo q no puede
            // disparar
            if (petrinet.fundamentalEquation(v) == null || !(petrinet.workingState(v) == 0)) 
            {
                exitMonitor();
                int queue = conditionQueues.getQueue(v);
                try 
                {
                    conditionQueues.getQueued().get(queue).acquire();
                    if (testCondition()) {
                        return false; // Si un hilo se despierta en este punto y ya se completo la condicion, debe
                    }         // salir sin disparar nada.

                } catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
            }

            petrinet.fire(v);   // en realidad acá seria un if(Pudo disparar) -no wacho no
            if (k) 
            {
                Matrix sensibilized = petrinet.getSensibilized();
                Matrix queued = conditionQueues.queuedUp();
                Matrix and = sensibilized.arrayTimes(queued); // operación 'and'

                int m = result(and); // cantidad de transiciones sensibilizadas y encoladas

                if (m != 0) 
                {
                    // cual
                    int choice = policy.fireChoice(and);
                    // release
                    conditionQueues.getQueued().get(choice).release();
                    this.tInvariantsCounter += this.invariantsManager.countTransition(choice);
                } else 
                {
                    k = false;
                }

            }
        }

        exitMonitor();
        return true;
    }


    /*  _____
     * |*****|                                      PENE 8============================================================D
     * |*****|                                                                                       ____
     * |*****| ______________________________________________________________________________________|    \
     *  _____ _______________________________________________________________________________________|   --|
     * |*****|                                                                                       |____/
     * |*****|
     * |*****|
     */



    /*
     * *************************
     * *** Métodos públicos ****
     * *************************
     */
    public void catchMonitor() throws InterruptedException {
        mutex.acquire();
    }

    public void exitMonitor() {
        mutex.release();
    }

    public int result(Matrix and) {
        int m = 0;

        for (int i = 0; i < and.getColumnDimension(); i++)
            if (and.get(0, i) > 0)
                m++;

        return m;
    }

    private boolean testCondition() {
        return (this.tInvariantsCounter == 200);
    }

    /*
     * *************************
     * *** Getters & Setters ***
     * *************************
     */
}
