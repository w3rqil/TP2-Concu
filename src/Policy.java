import Jama.Matrix;

//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class Policy {
    private String policyType;
    private ArrayList<Integer> transitions;
    private Random randomGenerator;
    // private int segment;

    public Policy(String policyType) {

        this.policyType = policyType;
        this.transitions = new ArrayList<>();
        randomGenerator = new Random();
    }
    // segment = matrix with queued and sensibilized transitions
// 
    /*
     * Segment 1: T0 T1
     * Segment 2: T4 T5
     * Segment 3: T8 T9
     */
    public int fireChoice(Matrix matrix) {

        int indexChosen = 0;

        if (policyType == "8020") { // asks the policy type

            if (matrix.get(0, 8) >= 1 || matrix.get(0, 9) >= 1) { // if transitions 8 or 9 are enabled, changes the
                                                                  // probability to 80-20

                double probabilidadT8 = 0.8;

                // generates a random number between 0 and 1
                double numeroAleatorio = randomGenerator.nextDouble();

                if (numeroAleatorio < probabilidadT8) {
                    indexChosen = 8; // Chose index 8 
                    System.out.println("Ocurrió la transición T8");
                } else {
                    indexChosen = 9; // Chose index 9 
                    System.out.println("Ocurrió la transición T9");
                }
            } else {
                // If the sensibilized transitions are not T8 or T9, chooses randomly
                transitions.clear(); // clear array

                for (int i = 0; i < matrix.getColumnDimension(); i++)
                    if (matrix.get(0, i) > 0)
                        transitions.add(i);

                int choice = (int) Math.round(randomGenerator.nextInt(transitions.size()));
                indexChosen = (int) Math.round(transitions.get(choice));
            }
        } else if (this.policyType == "Equitative") { 
            // if policy type is equitative chooses randomly with a normal distribution of probabilities                                                     // red
            transitions.clear(); // Clears array

            for (int i = 0; i < matrix.getColumnDimension(); i++)
                if (matrix.get(0, i) > 0)
                    transitions.add(i);

            int choice = (int) Math.round(randomGenerator.nextInt(transitions.size()));
            indexChosen = (int) Math.round(transitions.get(choice));

        }

        return indexChosen;

    }

    private void setTransitions(Matrix matrix) {
        this.transitions.clear(); // Clears array.

        for (int i = 0; i < matrix.getColumnDimension(); i++)
            if (matrix.get(0, i) > 0)
                transitions.add(i);
    }

    /*
     * Segment 1: T0 T1
     * Segment 2: T4 T5
     * Segment 3: T8 T9
     */

    public int setSegment(ArrayList<Integer> index) {
        for (int i = 0; i < index.size(); i++) {
            if (index.get(i) == 1) {
                if (0 <= i && i <= 1) {
                    // segment=1;
                    return 1;
                } else if (4 <= i && i <= 5) {
                    // segment=2;
                    return 2;
                } else if (8 <= i && i <= 9) {
                    // segment=3;
                    return 3;
                }
            }
        }
        return 0;
    }

}
