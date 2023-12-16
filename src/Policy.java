import Jama.Matrix;

//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class Policy {
    private String policyType;
    private ArrayList<Integer> transitions;
    private Random randomGenerator;
    //private int segment;

    public Policy(String policyType) {

        this.policyType = policyType;
        this.transitions = new ArrayList<>();
        randomGenerator = new Random();
    }
// segment = matrix con transiciones sensibilizadas y encoladas

    /*
    Segment 1: T0 T1
    Segment 2: T4 T5
    Segment 3: T8 T9
    */
    public int fireChoice(Matrix matrix) {

        int indexChosen = 0;

        if(policyType == "8020"){ // Pregunto si la politica es 80% y 20% para el tercer segmento.

            if(matrix.get(0,8)>=1 || matrix.get(0,9)>=1){  // Si las transiciones son la 8 y la 9, cambia la probabilidad a 8020 , el resto es igual

            double probabilidadT8 = 0.8;

            // Generar un número aleatorio entre 0 y 1
            double numeroAleatorio = randomGenerator.nextDouble();

            if (numeroAleatorio < probabilidadT8) {
                indexChosen=8;    // Pongo el indice en 8, ya que la choice es la transicion 8
                System.out.println("Ocurrió la transición T8");
            } else {
                indexChosen=9;  // Pongo el indice en 9, ya que la choice es la transicion 9
                System.out.println("Ocurrió la transición T9");
            }
        } else{
            // Si las transiciones habilitadas no son t8 o t9, elijo entre el resto con una carga 50 50
            transitions.clear(); //Vaciar el arreglo.

            for(int i = 0; i < matrix.getColumnDimension(); i++)
                if(matrix.get(0, i) > 0) transitions.add(i);

            int choice = (int)Math.round(randomGenerator.nextInt(transitions.size()));
            indexChosen = (int)Math.round(transitions.get(choice));
        }
        }else if(this.policyType == "Equitative"){ // Si es equitativa el tipo de politica, carga 50 50 en toda la red
            transitions.clear(); //Vaciar el arreglo.

            for(int i = 0; i < matrix.getColumnDimension(); i++)
                if(matrix.get(0, i) > 0) transitions.add(i);

            int choice = (int)Math.round(randomGenerator.nextInt(transitions.size()));
            indexChosen = (int)Math.round(transitions.get(choice));

        }

        return indexChosen;

    }


    private void setTransitions(Matrix matrix) {
        this.transitions.clear(); //Vaciar el arreglo.
        
        for(int i = 0; i < matrix.getColumnDimension(); i++)
            if(matrix.get(0, i) > 0) transitions.add(i);
    }

    /*
    Segment 1: T0 T1
    Segment 2: T4 T5
    Segment 3: T8 T9
    */

    public int setSegment(ArrayList<Integer> index) 
    {
        for(int i=0; i<index.size(); i++) 
        {
            if(index.get(i)==1)
            {
                if(0<=i && i<=1)
                {
                    //segment=1;
                    return 1;
                }
                else if(4<=i && i<=5)
                {
                    //segment=2;
                    return 2;
                }
                else if(8<=i && i<=9)
                {
                    //segment=3;
                    return 3;
                }
            }
        }
        return 0;
    }
 

}
