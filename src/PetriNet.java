import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Jama.Matrix;
//import MyEnum.transitionState;

public class PetriNet {

    private Matrix incidence;
    private Matrix backwardsIncidence;
    private Matrix currentMarking;
    private Matrix sensibilizedTransitions; // vector de transiciones sensibilizadas
    private Matrix pInvariants;
    private Matrix matriz;
    private Matrix maxPInvariants;
    private Matrix workingVector;
    private Matrix alphaTime;
    private Matrix transitionCounter;
    private Matrix sensibilizedTime;
    public ArrayList<Integer> tInvariantsAux;
    private ArrayList<String> firedSequence;

    private int[] invariantCounting;
    private static int completedInvariants;
    private String CurrentRoute;

    private String allTransitionsPrint;

    private int tInvariantCounter;
    /*
     * // T0 T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11 T12 T13
     * {-1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
     * {-1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
     * {0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
     * {1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
     * {-1, -1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1},
     * {0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
     * {0, 0, 1, 1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, -1, -1, 1, 1, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0},
     * {0, 0, 0, 0, 0, 0, 1, 1, -1, -1, 0, 0, 0, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 1, 1, 0, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, -1, 0},
     * {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1},
     * {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1}
     * 
     */
    private final double[][] matrixIndicence = {
            // T0 T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11 T12 T13
            { -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { -1, -1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1 },
            { 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, -1, -1, 1, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 1, -1, -1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 1, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, -1, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1 }
    };

    private final double[][] tInvariant = {
            { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1 }, // T1 T3 T5 T7 T9 T11 T12 T13
            { 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1 }, // T1 T3 T5 T7 T8 T10 T12 T13
            { 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1 }, // T1 T3 T4 T6 T9 T11 T12 T13
            { 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1 }, // T1 T3 T4 T6 T8 T10 T12 T13
            { 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1 }, // T0 T2 T5 T7 T9 T11 T12 T13
            { 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1 }, // T0 T2 T5 T7 T8 T10 T12 T13
            { 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1 }, // T0 T2 T4 T6 T9 T11 T12 T13
            { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1 }, // T0 T2 T4 T6 T8 T10 T12 T13
    };
    private final double[][] pInvariant = {
            // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
            { 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 }, // 1 0,3,5,6,9,11,12,13,15,16,17
            { 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 2 1,3,
            { 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 3 2,5
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0 }, // 4 13,14,15
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 5 7,9
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // 6 8,11------------------
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 }, // 7 9,10,11
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 }, // 8 17,18----------------
            { 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, // 9 3,4,5,17
    };

    private final double[][] bIncidence = {
            // T0 T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11 T12 T13
            { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }
    };
    // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
    private final double[] initialMarking = { 1, 1, 1, 0, 3, 0, 0, 1, 1, 0, 2, 0, 0, 0, 1, 0, 0, 0, 1 };

                                     // 0 1 2 3 4 5 6 7 8 9 10 11 12 13
    private final double[] aTimes = { 0, 0, 5, 5, 0, 0, 10, 10, 0, 0, 10, 10, 0, 5 };
    public List<Integer> tInvariantSum;

    private List<List<Integer>> tInvariants;

    public PetriNet() {

        this.CurrentRoute = "";
        this.completedInvariants = 0;
        this.invariantCounting = new int[8];
        this.incidence = new Matrix(matrixIndicence);
        this.backwardsIncidence = new Matrix(bIncidence);
        this.currentMarking = new Matrix(initialMarking, 1);
        this.sensibilizedTransitions = new Matrix(1, incidence.getColumnDimension());
        this.pInvariants = new Matrix(pInvariant);
        this.maxPInvariants = new Matrix(incidence.getRowDimension(), 1);
        this.workingVector = new Matrix(1, incidence.getColumnDimension());
        this.alphaTime = new Matrix(aTimes, 1);
        this.sensibilizedTime = new Matrix(1, incidence.getColumnDimension());
        this.firedSequence = new ArrayList<String>();
        this.transitionCounter = new Matrix(1, 14);
        this.tInvariantSum = new ArrayList<>();
        this.tInvariantsAux = new ArrayList<>();
        for (int j = 0; j < 14; j++) {
            transitionCounter.set(0, j, 0.0);
        }
        this.tInvariants = new ArrayList<>();
        setTInvariants();
        this.tInvariantCounter = 0;

    }

    private void setTInvariants() {
        Integer[] tInvariant0 = { 1, 3, 5, 7, 9, 11, 12, 13 };
        Integer[] tInvariant1 = { 1, 3, 5, 7, 8, 10, 12, 13 };
        Integer[] tInvariant2 = { 1, 3, 4, 6, 9, 11, 12, 13 };
        Integer[] tInvariant3 = { 1, 3, 4, 6, 8, 10, 12, 13 };
        Integer[] tInvariant4 = { 0, 2, 5, 7, 9, 11, 12, 13 };
        Integer[] tInvariant5 = { 0, 2, 5, 7, 8, 10, 12, 13 };
        Integer[] tInvariant6 = { 0, 2, 4, 6, 9, 11, 12, 13 };
        Integer[] tInvariant7 = { 0, 2, 4, 6, 8, 10, 11, 13 };
        this.tInvariants.add(Arrays.asList(tInvariant0));
        this.tInvariants.add(Arrays.asList(tInvariant1));
        this.tInvariants.add(Arrays.asList(tInvariant2));
        this.tInvariants.add(Arrays.asList(tInvariant3));
        this.tInvariants.add(Arrays.asList(tInvariant4));
        this.tInvariants.add(Arrays.asList(tInvariant5));
        this.tInvariants.add(Arrays.asList(tInvariant6));
        this.tInvariants.add(Arrays.asList(tInvariant7));
    }

    /*
     * ********************
     * Public Methods *
     ********************
     */

    /*
     * This method calculates the fundamental ecuation of the petrinet: mi+1= mi+W*s
     * where mi is the current marking, W is the incidence matrix and s is the firing vector.
     * 
    * @param v: firing vector
    * @return fundamental equation
    */
    public Matrix fundamentalEquation(Matrix v) {
        return (currentMarking.transpose().plus(incidence.times(v.transpose()))).transpose();
        // (mi + w * s) transpose
    }

    public boolean fundamentalEquationTest(Matrix firingVector) {

        matriz = fundamentalEquation(firingVector);

        for (int i = 0; i < this.matriz.getColumnDimension(); i++)
            if (this.matriz.get(0, i) < 0) {
                return false;
            }
        return true;
    }

    /*
     * Idea: compare the current marking to the marking requested for each transition. 
     * Current marking: vector with the individual marking of all the places
     * Incidence matrix: columns = transitions | rows = places
     * Then, if a transition of the current marking has fewer tokens than those requested by the transition, it cannot be fired.
     * 
     * @param 
     * @return 
     */
    void enableTransitions() {
        // currentMarking.print(2,0);
        Long time = System.currentTimeMillis();// tiempo actual

        for (int i = 0; i < backwardsIncidence.getColumnDimension(); i++) {
            boolean enabledTransition = true;
            for (int j = 0; j < backwardsIncidence.getRowDimension(); j++) {
                if (backwardsIncidence.get(j, i) > currentMarking.get(0, j)) {
                    enabledTransition = false;
                    break;
                }
            }
            if (enabledTransition) {
                sensibilizedTransitions.set(0, i, 1);
                sensibilizedTime.set(0, i, (double) time);
            } else {
                sensibilizedTransitions.set(0, i, 0);
            }

        }
        System.out.println("Enabled transitions: " + getEnabledTransitionsInfo());
        // sensibilizedTransitions.print(1, 0);

    }

    /*
     * returns a string with the enabled transitions info.
     * 
     * @return enabled transitions info
     */
    public String getEnabledTransitionsInfo() {
        String enabled = "";
        for (int i = 0; i < 14; i++) {
            if (sensibilizedTransitions.get(0, i) == 1) {
                enabled += ("T" + i + "  ");
            }
        }
        return enabled;
    }

    public String getAllTransitionsPrint() {

        return allTransitionsPrint;
    }

    /*
     * - cambiar marcado actual
     * - actualizar sensibilizadas
     * - checkear invariantes
     * - agregar disparo a la secuencia
     *
     * Este es el metodo fire transition pero quería usar el nombre
     * fire transition en monitor
     * 
     * This method fires a transition if it is enabled.
     * - change current marking.
     * - update current working vector.
     * - update sensibilized transitions.
     * - adds to the fired secuence the fired transition.
     * 
     * @param v: firing vector
     * @return
     */

    void fire(Matrix v) // esta es la que hace el disparo literal, actualizando la rdp
    {
        setCurrentMarking(fundamentalEquation(v));
    
        setWorkingVector(v, 0);

        testPInvariants();
        enableTransitions();
        firedSequence.add("T" + getIndex(v) + ""); // tiene TODAS las secuencia de transiciones disparadas
        System.out.println("Firing: T" + getIndex(v));
        for (int i = 0; i < v.getRowDimension(); i++) {
            for (int j = 0; j < v.getColumnDimension(); j++) {
                if (v.get(i, j) != 0.0) {
                    transitionCounter.set(i, j, (transitionCounter.get(i, j) + v.get(i, j)));
                }
            }
        }
        System.out.println(transitionsCounterInfo());
        System.out.println("Current marking:\n" + getMarkingInfo());
        // this.currentMarking.print(2,0);

        // T1 T3 T5 T7 T9 T11 T12 T13
        // T1 T3 T5 T7 T8 T10 T12 T13
        // T1 T3 T4 T6 T9 T11 T12 T13
        // T1 T3 T4 T6 T8 T10 T12 T13
        // T0 T2 T5 T7 T9 T11 T12 T13
        // T0 T2 T5 T7 T8 T10 T12 T13
        // T0 T2 T4 T6 T9 T11 T12 T13
        // T0 T2 T4 T6 T8 T10 T11 T13 */

        String lastTransition = getIndex(v) + "";

        allTransitionsPrint += "T" + lastTransition;

        followUp(lastTransition);

    }

    public String getMarkingInfo() {
        String marking = "P0 P1 P2 P3 P4 P5 P6 P7 P8 P9 P10 P11 P12 P13 P14 P15 P16 P17 P18\n";
        for (int i = 0; i < currentMarking.getColumnDimension(); i++) {
            if (i < 10) {
                marking += ((int) currentMarking.get(0, i) + "  ");
            } else {
                marking += ((int) currentMarking.get(0, i) + "   ");
            }
        }
        return (marking + "\n");
    }

    public int getCompletedInvariants() {
        return completedInvariants;
    }

    public int getValinvariantCounting(int i) {
        return invariantCounting[i];
    }

    public String transitionsCounterInfo()
    {
        int totalCount = 0;
        String arg = "Transitions:\n";
        for (int i = 0; i < transitionCounter.getColumnDimension(); i++) {
            arg += ("                         🔹T" + i + ": " + (int) transitionCounter.get(0, i) + " times\n");
            totalCount += (int) transitionCounter.get(0, i);
        }
        arg += ("                                   🔹Total transitions: " +totalCount);
        return arg;
    }

    
    /*
     *
     * 
     * Checks the 'state' of the transition that is going to be fired.
     * 0 - No one is working on it. STATE = NONE
     * 1 - Someone is working on it, but it is not the thread that is requesting it. STATE = OTHER
     * 2 - The thread that is requesting it is already working on it. STATE = SELF
     * 
     * @param v: firing vector
     * @return
     */

    public int workingState(Matrix v) {
        int index = getIndex(v);

        if (workingVector.get(0, index) == 0)
            return 0;
        else if (workingVector.get(0, index) != Thread.currentThread().getId())
            return 1;
        else
            return 2;
    }


    /*
     * Follows the transitions fired in order to check the T-invariants.
     * 
     * @param lastTransition: last transition fired
     * @return
     */
    public void followUp(String lastTransition) {

        if (!lastTransition.contains("13")) {
            CurrentRoute += lastTransition;
        } else {

            // T1 T3 T5 T7 T9 T11 T12 T13
            // T1 T3 T5 T7 T8 T10 T12 T13
            // T1 T3 T4 T6 T9 T11 T12 T13
            // T1 T3 T4 T6 T8 T10 T12 T13
            // T0 T2 T5 T7 T9 T11 T12 T13
            // T0 T2 T5 T7 T8 T10 T12 T13
            // T0 T2 T4 T6 T9 T11 T12 T13
            // T0 T2 T4 T6 T8 T10 T11 T13 */

            if (CurrentRoute.contains("135791112")) {
                invariantCounting[0] += 1;
                completedInvariants++;
            } else if (CurrentRoute.contains("135781012")) {
                invariantCounting[1] += 1;
                completedInvariants++;
            } else if (CurrentRoute.contains("134691112")) {
                invariantCounting[2] += 1;
                completedInvariants++;
            } else if (CurrentRoute.contains("134681012")) {
                invariantCounting[3] += 1;
                completedInvariants++;
            } else if (CurrentRoute.contains("025791112")) {
                invariantCounting[4] += 1;
                completedInvariants++;
            } else if (CurrentRoute.contains("025781012")) {
                invariantCounting[5] += 1;
                completedInvariants++;
            } else if (CurrentRoute.contains("024691112")) {
                invariantCounting[6] += 1;
                completedInvariants++;
            } else if (CurrentRoute.contains("024681012")) {
                invariantCounting[7] += 1;
                completedInvariants++;
            }

            else {

                for (int i = 0; i < 8; i++) {
                    // System.out.println("INVARIANTE "+ i +" " +"OCURRIO " +
                    // getValinvariantCounting(i) + " VECES ");
                    System.out.println(i + " T-invariant appears " + getValinvariantCounting(i) + " times.");
                }

                System.out.println("Error in T-Invariant: " + CurrentRoute);
            }

            for (int i = 0; i < 8; i++) {
                // System.out.println("INVARIANTE "+ i +" " +"OCURRIO " +
                // getValinvariantCounting(i) + " VECES ");
                System.out.println(i + " T-invariant appears " + getValinvariantCounting(i) + " times.");
            }

            CurrentRoute = "";
        }

    }

    /*
     * This method checks the P-invariants using the current marking of the places.
     * 
     * @param
     * @return
     */
    public void testPInvariants() {
        boolean pInv0, pInv1, pInv2, pInv3, pInv4, pInv5, pInv6, pInv7, pInv8;
        pInv0 = (currentMarking.get(0, 0) + currentMarking.get(0, 3) + currentMarking.get(0, 5)
                + currentMarking.get(0, 6) + currentMarking.get(0, 9) + currentMarking.get(0, 11)
                + currentMarking.get(0, 12) + currentMarking.get(0, 13) + currentMarking.get(0, 15)
                + currentMarking.get(0, 16) + currentMarking.get(0, 17)) == 1;
        pInv1 = (currentMarking.get(0, 1) + currentMarking.get(0, 3)) == 1;
        pInv2 = (currentMarking.get(0, 2) + currentMarking.get(0, 5)) == 1;
        pInv3 = (currentMarking.get(0, 14) + currentMarking.get(0, 13) + currentMarking.get(0, 15)) == 1;
        pInv4 = (currentMarking.get(0, 7) + currentMarking.get(0, 9)) == 1;
        pInv5 = (currentMarking.get(0, 8) + currentMarking.get(0, 11)) == 1;
        pInv6 = (currentMarking.get(0, 9) + currentMarking.get(0, 10) + currentMarking.get(0, 11)) == 2;
        pInv7 = (currentMarking.get(0, 17) + currentMarking.get(0, 18)) == 1;
        pInv8 = (currentMarking.get(0, 3) + currentMarking.get(0, 4) + currentMarking.get(0, 5)
                + currentMarking.get(0, 17)) == 3;

        if (!(pInv0 && pInv1 && pInv2 && pInv3 && pInv4 && pInv5 && pInv6 && pInv7 && pInv8)) {
            System.out.println("Error in a p-invariant.");
        }
    }
    /*
     * ************************
     * *** Geters & Setters ***
     * ************************
     */

    public Matrix getCurrentMarking() {
        return currentMarking;
    }

    public void setCurrentMarking(Matrix currentMarking) {
        this.currentMarking = currentMarking;
    }

    public Matrix getSensibilized() {
        return sensibilizedTransitions;
    }

    /*
     * Returns the index of the transition that is going to be fired.
     * 
     * @param v: firing vector
     * @return index of the transition
    */
    public int getIndex(Matrix v) {
        int index = 0;
        for (int i = 0; i < v.getColumnDimension(); i++) {
            if (v.get(0, i) == 1)
                break;
            else
                index++;
        }
        return index;
    }

    public Matrix getAlphaTimes() {
        return alphaTime;
    }

    public Matrix getSensibilizedTime() {
        return sensibilizedTime;
    }

    public void setWorkingVector(Matrix firingVector, double value) {
        this.workingVector.set(0, getIndex(firingVector), value);
    }

    public Matrix getIncidenceMatrix() {
        return this.incidence;
    }

}
