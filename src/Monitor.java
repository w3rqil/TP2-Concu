
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import Jama.Matrix;
//import java.util.ArrayList;

public class Monitor {

    private PetriNet petrinet;
    private Policy policy;
    private Semaphore mutex;            //BORRAR

    //  private Mutex mutex;



    private CQueues conditionQueues;
    private int tInvariantsCounter;
    private ArrayList<String> invariantsManager;
    private String transitionTrace;

    private int deadThreads;



    public Monitor(PetriNet petrinet, Policy policy) {
        //  this.mutex = Mutex.getInstance();       //INSTANCIA UNICA
        this.conditionQueues= new CQueues();
        this.petrinet = petrinet;
        this.policy = policy;
        this.mutex = new Semaphore(1 );        //BORRAR
        this.tInvariantsCounter = 0;
        this.invariantsManager = new ArrayList<>();
        setTInvariants();
        this.transitionTrace = "";
        this.deadThreads = 0;
    }

    private void setTInvariants() {
        this.invariantsManager.add("13579111213");
        this.invariantsManager.add("13578101213");
        this.invariantsManager.add("13469111213");
        this.invariantsManager.add("13468101213");
        this.invariantsManager.add("02579111213");
        this.invariantsManager.add("02578101213");
        this.invariantsManager.add("02469111213");
        this.invariantsManager.add("02468101213");
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
    // gestor del monitor
    public boolean fireTransition(Matrix v)
    {


        try
        {
            if(petrinet.getfullCounters()<200) {
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

            v.print(2,0);
            if(petrinet.fundamentalEquationTest(v)&&(petrinet.workingState(v)==0))
                k=true;
            else
                k=false;
            if(k){

                petrinet.fire(v);
                Matrix sensibilized = petrinet.getSensibilized();
                sensibilized.print(0,2);
                Matrix queued = conditionQueues.queuedUp();
                Matrix and = sensibilized.arrayTimes(queued); // operación 'and'

                int m=result(and); //sensibilizadas y encoladas
                if (m > 0) // hay transiciones habilitadas y encoladas
                {
                    // cual
                    int choice = policy.fireChoice(and);
                    // release
                    conditionQueues.getQueued().get(choice).release();

                    this.tInvariantsCounter++;

                    System.out.println("Hilo " + Thread.currentThread().getId() + " se despierta");

                } else // no hay transiciones habilitadas y encoladas
                {
                    System.out.println("SETEO K = FALSE");
                    k = false;
                }
            } else {
                exitMonitor();
                int queue = conditionQueues.getQueue(v);
                try {
                    conditionQueues.getQueued().get(queue).acquire();
                    if(petrinet.getfullCounters()>=200) return false;
                } catch ( InterruptedException e){
                    e.printStackTrace();
                }

            }
            // esto sería el equivalente a k=false. Se manda a dormir al hilo q no puede
            // disparar
            /*if (!petrinet.fundamentalEquationTest(v) || !(petrinet.workingState(v) == 0))
            {
                exitMonitor();
                System.out.println("Hilo " + Thread.currentThread().getId() + " se va a dormir");
                int queue = conditionQueues.getQueue(v);
                try 
                {
                    conditionQueues.getQueued().get(queue).acquire();
                    if (petrinet.getfullCounters() == 200)
                    {
                        return false; // Si un hilo se despierta en este punto y ya se completo la condicion, debe
                    }         // salir sin disparar nada
                } catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
            }*/
            /*if (k)
            {
                petrinet.fire(v);
                Matrix sensibilized = petrinet.getSensibilized();
                sensibilized.print(0,2);
                Matrix queued = conditionQueues.queuedUp();
                Matrix and = sensibilized.arrayTimes(queued); // operación 'and'

                int m = result(and); // cantidad de transiciones sensibilizadas y encoladas

                if (m > 1) // hay transiciones habilitadas y encoladas
                {
                    // cual
                    int choice = policy.fireChoice(and);
                    // release
                    conditionQueues.getQueued().get(choice).release();

                    this.tInvariantsCounter++;

                    System.out.println("Hilo " + Thread.currentThread().getId() + " se despierta");

                } else if(m==1)
                {
                    conditionQueues.getQueued().get(getIndex(and)).release();

                } else // no hay transiciones habilitadas y encoladas
                {
                    k = false;
                }
            }*/

        }

        exitMonitor();
        return true;
    }

    public String backState(){
        if(petrinet.getfullCounters() >= 200){
            return "si";
        }
        return "no";
    }
    /*
     * *************************
     * *** Métodos públicos ****
     * *************************
     */
    public void catchMonitor() throws InterruptedException {
        //BORRAR
        mutex.acquire();

    }

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

    public boolean testCondition() {
        return (this.tInvariantsCounter == (200*8));
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

    public PetriNet getPetriNet() {
        return this.petrinet;
    }

    public int getTInvariantsCount() {
        return this.tInvariantsCounter;
    }
    public void printDaDead(){
        System.out.println("Hilos muertos: " + deadThreads);
    }
}
