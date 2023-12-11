import java.util.*;
import java.util.concurrent.Semaphore;
import Jama.Matrix;

/* 
 * FALTA TERMINAR
 */

public class CQueues {

    private final int maxQueues = 10;  // cambiar (num transiciones)
    private ArrayList<Semaphore> conditionQueues; 

    public CQueues()
    {
        conditionQueues = new ArrayList<Semaphore>();
        for(int i = 0; i < maxQueues; i++)
            conditionQueues.add(new Semaphore(0));  //les juro q lo hice sin mirar xd

    }


    /*
     * *************************
     * *** Métodos públicos ****
     * *************************
     */

     /*
      * *************************
      * *** getters y setters ***
      * *************************
    */


    public ArrayList<Semaphore> getQueues() {
        return conditionQueues;
    }
    /* 
     * retorno cola para la transicion correspondiente
     */
    public int getQueue(Matrix v) 
    {
        int index = 0;

        for(int i = 0; i < v.getColumnDimension(); i++) 
        {
            if(v.get(0, i) == 1) break;else index++;
        }

        return index;
    }

    
}
