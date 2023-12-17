import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private Matrix transitionCounter;
    public ArrayList<Integer> tInvariantsAux;
    private ArrayList<String> firedSequence;

    private Matrix alphaTime;

    ////AGREGO AGU
    private int[] invariantCounting;
    private static int completedInvariants;
    private String CurrentRoute;

    private String allTransitionsPrint;
    ////



    private int tInvariantCounter;
    /*
    //  T0	T1	T2	T3	T4	T5	T6	T7	T8	T9	T10	T11	T12	T13
        {-1, -1, 0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1},
        {-1, 0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
        {0,	-1,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
        {1,	0, -1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
        {-1, -1, 1,	1,	0,	0,	0,	0,	0,	0,	0,	0,	-1,	1},
        {0,	1,	0,	-1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
        {0,	0,	1,	1,	-1,	-1,	0,	0,	0,	0,	0,	0,	0,	0},
        {0,	0,	0,	0,	-1,	0,	1,	0,	0,	0,	0,	0,	0,	0},
        {0,	0,	0,	0,	0,	-1,	0,	1,	0,	0,	0,	0,	0,	0},
        {0,	0,	0,	0,	1,	0,	-1,	0,	0,	0,	0,	0,	0,	0},
        {0,	0,	0,	0,	-1,	-1,	1,	1,	0,	0,	0,	0,	0,	0},
        {0,	0,	0,	0,	0,	1,	0,	-1,	0,	0,	0,	0,	0,	0},
        {0,	0,	0,	0,	0,	0,	1,	1,	-1,	-1,	0,	0,	0,	0},
        {0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	-1,	0,	0,	0},
        {0,	0,	0,	0,	0,	0,	0,	0,	-1,	-1,	1,	1,	0,	0},
        {0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	-1,	0,	0},
        {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	-1,	0},
        {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	-1},
        {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	-1,	1}

    * */
    private final double[][] matrixIndicence = {
            //  T0	T1	T2	T3	T4	T5	T6	T7	T8	T9	T10	T11	T12	T13
            {-1, -1, 0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1},
            {-1, 0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	-1,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {1,	0, -1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {-1, -1, 1,	1,	0,	0,	0,	0,	0,	0,	0,	0,	-1,	1},
            {0,	1,	0,	-1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	1,	1,	-1,	-1,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	-1,	0,	1,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	-1,	0,	1,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	1,	0,	-1,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	-1,	-1,	1,	1,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	1,	0,	-1,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	1,	1,	-1,	-1,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	-1,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	-1,	-1,	1,	1,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	-1,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	-1,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	-1},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	-1,	1}
    };

    private final double[][] tInvariant = {
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1     , 1, 1}, // T1 T3 T5 T7 T9 T11      T12 T13
            {0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0     , 1, 1}, // T1 T3 T5 T7 T8 T10      T12 T13
            {0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1     , 1, 1}, // T1 T3 T4 T6 T9 T11      T12 T13
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0     , 1, 1}, // T1 T3 T4 T6 T8 T10      T12 T13
            {1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1     , 1, 1}, // T0 T2 T5 T7 T9 T11      T12 T13
            {1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0     , 1, 1}, // T0 T2 T5 T7 T8 T10      T12 T13
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1     , 1, 1}, // T0 T2 T4 T6 T9 T11      T12 T13
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0     , 1, 1}, // T0 T2 T4 T6 T8 T10      T12 T13
    };
    private final double[][] pInvariant = {
            //0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18
            { 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 }, // 1    0,3,5,6,9,11,12,13,15,16,17
            { 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 2    1,3,
            { 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 3    2,5
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0 }, // 4    13,14,15
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 5    7,9
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // 6    8,11------------------
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 }, // 7    9,10,11
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 }, // 8    17,18----------------
            { 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, // 9    3,4,5,17
    };

    private final double[][] bIncidence = {
            //  T0	T1	T2	T3	T4	T5	T6	T7	T8	T9	T10	T11	T12	T13
            {1,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {1,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0},
            {0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	1,	1,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	1,	1,	0,	0,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	1,	1,	0,	0,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1},
            {0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0}
    };
    //0 1  2  3 4   5  6  7  8  9 10 11 12 13 14 15 16 17 18
    private final double[] initialMarking = {1, 1, 1, 0, 3, 0, 0, 1, 1, 0, 2, 0, 0, 0, 1, 0, 0, 0, 1};

                                //   0  1   2   3  4   5    6   7   8  9  10    11  12  13
    private final double[] alphaT = { 0, 0, 50, 50, 0, 0, 100, 100, 0, 0, 100, 100, 0, 50};

    public List<Integer> tInvariantSum;


    public PetriNet()
    {
        ///AGREGE AGU
        this.CurrentRoute="";
        this.completedInvariants=0;
        this.invariantCounting = new int[8];
        this.incidence = new Matrix(matrixIndicence);
        this.backwardsIncidence = new Matrix(bIncidence);
        this.currentMarking = new Matrix(initialMarking,1);
        this.sensibilizedTransitions = new Matrix(1, incidence.getColumnDimension());
        this.pInvariants = new Matrix(pInvariant);
        this.maxPInvariants = new Matrix(incidence.getRowDimension(), 1); // esto esta mal creo, no hay que ver el la incidencia
<<<<<<< HEAD
        this.sensibilizedTime = new Matrix(1, incidence.getColumnDimension());
        this.alphaTime = new Matrix(alphaT, 1);
=======
>>>>>>> parent of 98bb6e2 (time update)
        this.workingVector = new Matrix(1, incidence.getColumnDimension());
        this.firedSequence = new ArrayList<String>();
        this.transitionCounter = new Matrix(1,14);
        this.tInvariantSum = new ArrayList<>();
        this.tInvariantsAux = new ArrayList<>();
        for (int j = 0; j < 14; j++) {
            transitionCounter.set(0, j, 0.0);
        }
        this.tInvariantCounter = 0;
    }



    /*
     * ********************
     * Public Methods *
     ********************
     */

    /*
     * Caculates fundamental ecuation of a petri net for a given fire vector.
     * new marking = current marking + (incidence matrix * fire vector).
     */
// mi+1= mi+W*s
    public Matrix fundamentalEquation(Matrix v)
    {
        return (currentMarking.transpose().plus(incidence.times(v.transpose()))).transpose();
        //       (mi                          +  w       *       s)          transpose
    }



    public boolean fundamentalEquationTest(Matrix firingVector) {

        matriz = fundamentalEquation(firingVector);

        for(int i = 0; i < this.matriz.getColumnDimension(); i++)
            if(this.matriz.get(0, i) < 0) {
                return false;
            } return true;
    }

    /*
     * Idea: comparar el marcado actual, con el marcado pedido para cada transición.
     * Marcado actual: vector con el marcado individual de todas las plazas
     * Matriz de incidencia: columnas=transiciones| filas= plazas
     * Entonces, si una transición del marcado actual tiene menos tokens que los
     * pedidos por la trasicion, no se puede disparar.
     */
    void enableTransitions()
    {
        //currentMarking.print(2,0);
        for(int i = 0; i < backwardsIncidence.getColumnDimension(); i++) {
            boolean enabledTransition = true;
            for(int j = 0; j < backwardsIncidence.getRowDimension(); j++) {
                if(backwardsIncidence.get(j,i) > currentMarking.get(0,j)) {
                    enabledTransition = false;
                    break;
                }
            }
            if(enabledTransition) {
                sensibilizedTransitions.set(0,i,1);
            }
            else {
                sensibilizedTransitions.set(0,i,0);
            }

        }
        System.out.println("Enabled transitions: " + getEnabledTransitionsInfo());
        //sensibilizedTransitions.print(1, 0);

    }

    public String getEnabledTransitionsInfo() {
        String enabled = "";
        for(int i = 0; i < 14; i++) {
            if(sensibilizedTransitions.get(0,i) == 1) {
                enabled += ("T" + i + "  ");
            }
        }
        return enabled;
    }


    /*
     * - cambiar marcado actual
     * - actualizar sensibilizadas
     * - checkear invariantes
     * - agregar disparo a la secuencia
     *
     * Este es el metodo fire transition pero quería usar el nombre
     * fire transition en monitor
     */

    public String getAllTransitionsPrint() {

        return allTransitionsPrint;
    }

    void fire(Matrix v)             //esta es la que hace el disparo literal, actualizando la rdp
    {
        //this.currentMarking = fundamentalEquation(v);  //.transpose()
        setCurrentMarking(fundamentalEquation(v));
        /*System.out.println("Firing vector: \n");
        v.print(2,0);*/
        setWorkingVector(v, 0);
        testPInvariants();
        enableTransitions();
        firedSequence.add("T" + getIndex(v) + ""); //tiene TODAS las secuencia de transiciones disparadas
        System.out.println("Firing: T" + getIndex(v));
        for(int i = 0; i < v.getRowDimension(); i++) {
            for(int j = 0; j < v.getColumnDimension(); j++) {
                if(v.get(i,j) != 0.0) {
                    transitionCounter.set(i,j,(transitionCounter.get(i,j)+v.get(i,j)));
                }
            }
        }
        System.out.println(transitionsCounterInfo());
        System.out.println("Current marking:\n" + getMarkingInfo());
        //this.currentMarking.print(2,0);


        // T1 T3 T5 T7 T9 T11 T12 T13
        // T1 T3 T5 T7 T8 T10 T12 T13
        // T1 T3 T4 T6 T9 T11 T12 T13
        // T1 T3 T4 T6 T8 T10 T12 T13
        // T0 T2 T5 T7 T9 T11 T12 T13
        // T0 T2 T5 T7 T8 T10 T12 T13
        // T0 T2 T4 T6 T9 T11 T12 T13
        // T0 T2 T4 T6 T8 T10 T11 T13       */

        String lastTransition = getIndex(v)+"";

        allTransitionsPrint +=  "T" +lastTransition;

        followUp(lastTransition);

    }

    public String getMarkingInfo() {
        String marking = "P0 P1 P2 P3 P4 P5 P6 P7 P8 P9 P10 P11 P12 P13 P14 P15 P16 P17 P18\n";
        for(int i = 0; i < currentMarking.getColumnDimension(); i++) {
            if(i < 10) {
                marking += ((int)currentMarking.get(0,i) + "  ");
            }
            else {
                marking += ((int)currentMarking.get(0,i) + "   ");
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
        String arg = "Transitions:\n";
        for(int i = 0; i < transitionCounter.getColumnDimension(); i++) {
            arg += ("   - T" + i + ": " + (int)transitionCounter.get(0,i) + " times\n");
        }
        return arg;
    }

    // ********************* */


    /*ublic boolean isTransitionEnabled(int transition)
    {
        return sensibilizedTransitions.get(transition, 0) == 1;
    }*/



    /*
     * *************************
     * ***** Util Functions*****
     * *************************
     */


    /*
     *
     *          Checkeo el estado de la transición que quiero disparar
     *
     *          Estados:
     *                  1) No hay nadie (0). ESTADO = NONE
     *                  2) Hay alguien que no es el hilo solicitante (IDs no coincidentes) ESTADO = OTHER
     *                  3) Quien estaba trabajando es el hilo solicitante. ESTADO = SELF
     */

    public int workingState(Matrix v)
    {
        int index = getIndex(v);

        if(workingVector.get(0, index) == 0) return 0;
        else if(workingVector.get(0, index) != Thread.currentThread().getId()) return 1;
        else return 2;
    }


    public void followUp(String lastTransition){

        if( !lastTransition.contains("13") )
        {
            CurrentRoute+=lastTransition;
        }
        else{

            // T1 T3 T5 T7 T9 T11 T12 T13
            // T1 T3 T5 T7 T8 T10 T12 T13
            // T1 T3 T4 T6 T9 T11 T12 T13
            // T1 T3 T4 T6 T8 T10 T12 T13
            // T0 T2 T5 T7 T9 T11 T12 T13
            // T0 T2 T5 T7 T8 T10 T12 T13
            // T0 T2 T4 T6 T9 T11 T12 T13
            // T0 T2 T4 T6 T8 T10 T11 T13       */

            if(CurrentRoute.contains("135791112"))
            {
                invariantCounting[0]+=1;
                completedInvariants++;
            }
            else if (CurrentRoute.contains("135781012")) {
                invariantCounting[1]+=1;
                completedInvariants++;
            }
            else if (CurrentRoute.contains("134691112")) {
                invariantCounting[2]+=1;
                completedInvariants++;
            }
            else if (CurrentRoute.contains("134681012")) {
                invariantCounting[3]+=1;
                completedInvariants++;
            }
            else if (CurrentRoute.contains("025791112")) {
                invariantCounting[4]+=1;
                completedInvariants++;
            }
            else if (CurrentRoute.contains("025781012")) {
                invariantCounting[5]+=1;
                completedInvariants++;
            }
            else if (CurrentRoute.contains("024691112")) {
                invariantCounting[6]+=1;
                completedInvariants++;
            }
            else if (CurrentRoute.contains("024681012")) {
                invariantCounting[7]+=1;
                completedInvariants++;
            }

            else{

                for(int i=0; i<8 ; i++){
                    //System.out.println("INVARIANTE "+ i +" " +"OCURRIO " + getValinvariantCounting(i) + " VECES ");
                    System.out.println(i +" T-invariant appears "+ getValinvariantCounting(i) +" times.");
                }


                System.out.println("Error in T-Invariant: " + CurrentRoute );
            }

            for(int i=0; i<8 ; i++){
                //System.out.println("INVARIANTE "+ i +" " +"OCURRIO " + getValinvariantCounting(i) + " VECES ");
                System.out.println(i +" T-invariant appears "+ getValinvariantCounting(i) +" times.");
            }

            CurrentRoute="";
        }

    }


    /*
     *
     */
    public void testPInvariants()
    {
        boolean pInv0, pInv1, pInv2, pInv3, pInv4, pInv5, pInv6, pInv7, pInv8;
        pInv0 = (currentMarking.get(0,0)+ currentMarking.get(0,3)+ currentMarking.get(0,5)+currentMarking.get(0,6)+currentMarking.get(0,9)+currentMarking.get(0,11)+currentMarking.get(0,12)+currentMarking.get(0,13)+ currentMarking.get(0,15)+ currentMarking.get(0,16)+currentMarking.get(0,17))==1;
        pInv1= (currentMarking.get(0,1)+currentMarking.get(0,3))==1;
        pInv2= (currentMarking.get(0,2)+currentMarking.get(0,5))==1;
        pInv3= (currentMarking.get(0,14)+currentMarking.get(0,13)+ currentMarking.get(0,15))==1;
        pInv4 = (currentMarking.get(0,7)+currentMarking.get(0,9))==1;
        pInv5 = (currentMarking.get(0,8)+currentMarking.get(0,11))==1;
        pInv6= (currentMarking.get(0,9)+currentMarking.get(0,10)+ currentMarking.get(0,11))==2;
        pInv7= (currentMarking.get(0,17)+currentMarking.get(0,18))==1;
        pInv8= (currentMarking.get(0,3)+currentMarking.get(0,4)+ currentMarking.get(0,5) + currentMarking.get(0,17))==3;

        if(!(pInv0 && pInv1 && pInv2 && pInv3 && pInv4 && pInv5 && pInv6 && pInv7 && pInv8)){
            System.out.println("Error on a p-invariant.");
        }
    }
    /*
     * ************************
     * *** Geters & Setters ***
     * ************************
     */

    public Matrix getAlphaTime() { return this.alphaTime;}
    public Matrix getCurrentMarking() {
        return currentMarking;
    }

    public void setCurrentMarking(Matrix currentMarking) {
        this.currentMarking = currentMarking;
    }

    public Matrix getSensibilized()
    {
        return sensibilizedTransitions;
    }

    /**
     * la posicion del 
     * primer 1 del vector de disparo
     */
    public int getIndex(Matrix v)
    {
        int index = 0;
        for(int i = 0; i < v.getColumnDimension(); i++)
        {
            if(v.get(0, i) == 1) break; else index++;
        }
        return index;
    }

    public void setWorkingVector(Matrix firingVector, double value)
    {
        this.workingVector.set(0, getIndex(firingVector), value);
    }
    public Matrix getSensibilizedTime(){ return this.sensibilizedTime;}

    public Matrix getIncidenceMatrix()
    {
        return this.incidence;
    }
}
