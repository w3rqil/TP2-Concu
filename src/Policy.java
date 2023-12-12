//import java.lang.reflect.Array;???????????''
import java.util.Random;
import Jama.Matrix;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Policy {
    private String policyType;
    private ArrayList<Integer> transitions;
    //private int segment;

    public Policy(String policyType) {
        this.policyType = policyType;
    }
// segment = matrix con transiciones sensibilizadas y encoladas

    /*
    Segment 1: T0 T1
    Segment 2: T4 T5
    Segment 3: T8 T9
    */
    public int fireChoice(Matrix matrix) {
        double prob1 = 0.5;
        double prob2 = 0.2;
        double randomNum = Math.random();
        int segment;
        setTransitions(matrix);
        segment = setSegment(transitions);
        if(this.policyType.equalsIgnoreCase("Equitative")) {
            if(segment == 1) {
                if(randomNum < prob1) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
            else if(segment == 2) {
                if(randomNum < prob1) {
                    return 4;
                }
                else {
                    return 5;
                }
            }
            else if(segment == 3) {
                if(randomNum < prob1) {
                    return 8;
                }
                else {
                    return 9;
                }
            }
        }
        else {
            if(segment == 1) {
                if(randomNum < prob1) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
            else if(segment == 2) {
                if(randomNum < prob1) {
                    return 4;
                }
                else {
                    return 5;
                }
            }
            else if(segment == 3) {
                if(randomNum > prob2) {
                    return 8;
                }
                else {
                    return 9;
                }
            }
        }
        return 5000;
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
