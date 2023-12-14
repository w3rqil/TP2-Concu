import java.lang.reflect.Array;
import java.util.ArrayList;
import Jama.Matrix;
//import MyEnum.transitionState;

public class PetriNet {
    
    private Matrix incidence;
    private Matrix backwardsIncidence;
    private Matrix currentMarking;
    private Matrix sensibilizedTransitions; // vector de transiciones sensibilizadas
    private Matrix pInvariants;
    private Matrix tInvariants;
    private Matrix matriz;
    private Matrix maxPInvariants;
    private Matrix workingVector;
    private Matrix transitionCounter;
    private ArrayList<String> firedSequence;
    private Log log;

    private final double[][] matrixIndicence = {

            { -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //P0
            { -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //P1
            { 0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { -1, -1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1 },
            { 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0 },//p6
            { 0, 0, 0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },//p7
            { 0, 0, 0, 0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 0 },//p8
            { 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0 },//p9
            { 0, 0, 0, 0, -1, -1, 1, 1, 0, 0, 0, 0, 0, 0 },//P10
            { 0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0 },//P11
            { 0, 0, 0, 0, 0, 0, 1, 1, -1, -1, 0, 0, 0, 0 },//P12
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0 },//P13
            { 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 1, 1, 0, 0},//P14
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0 },//P15
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, -1, 0 },//P16
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1 },//p17
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1 } //P18
    };

    private final double[][] tInvariant = {
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1},// T1 T3 T5 T7 T9 T11 T12 T13
            {0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1}, // T1 T3 T5 T7 T8 T10 T12 T13
            {0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1}, // T1 T3 T4 T6 T9 T11 T12 T13
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1}, // T1 T3 T4 T6 T8 T10 T12 T13
            {1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1}, // T0 T2 T5 T7 T9 T11 T12 T13
            {1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1}, // T0 T2 T5 T7 T8 T10 T12 T13
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1}, // T0 T2 T4 T6 T9 T11 T12 T13
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1}, // T0 T2 T4 T6 T8 T10 T11 T13
    };
    private final double[][] pInvariant = {
            //0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18
            { 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 }, // 1    0,3,5,6,9,11,12,13,15,16,17
            { 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 2    1,3,
            { 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 3    2,5
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0 }, // 4    13,14,15
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 5    7,9
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // 6    8,11
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 }, // 7    9,10,11
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 }, // 8    17,18
            { 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, // 9    3,4,5,17
    };

    private final double[][] bIncidence = {
         //T 0,1,2,3,4,5,6,7,8,9,0,1,2,3
            {1,1,0,0,0,0,0,0,0,0,0,0,0,0},// P0
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0},// P1
            {0,1,0,0,0,0,0,0,0,0,0,0,0,0},// P2
            {0,0,1,0,0,0,0,0,0,0,0,0,0,0},// P3
            {1,1,0,0,0,0,0,0,0,0,0,0,1,0},// P4
            {0,0,0,1,0,0,0,0,0,0,0,0,0,0},// P5
            {0,0,0,0,1,1,0,0,0,0,0,0,0,0},// P6
            {0,0,0,0,1,0,0,0,0,0,0,0,0,0},// P7
            {0,0,0,0,0,1,0,0,0,0,0,0,0,0},// P8
            {0,0,0,0,0,0,1,0,0,0,0,0,0,0},// P9
            {0,0,0,0,1,1,0,0,0,0,0,0,0,0},// P10
            {0,0,0,0,0,0,0,1,0,0,0,0,0,0},// P11
            {0,0,0,0,0,0,0,0,1,1,0,0,0,0},// P12
            {0,0,0,0,0,0,0,0,0,0,1,0,0,0},// P13
            {0,0,0,0,0,0,0,0,1,1,0,0,0,0},// P14
            {0,0,0,0,0,0,0,0,0,0,0,1,0,0},// P15
            {0,0,0,0,0,0,0,0,0,0,0,0,1,0},// P16
            {0,0,0,0,0,0,0,0,0,0,0,0,0,1},// P17
            {0,0,0,0,0,0,0,0,0,0,0,0,1,0}  //P18
    };

    private final double[] initialMarking = {1, 1, 1, 0, 3, 0, 0, 1, 1, 0, 2, 0, 0, 0, 1, 0, 0, 0, 1};

    public PetriNet(Log log) 
    {
        this.log= log;
        this.incidence = new Matrix(matrixIndicence);
        this.backwardsIncidence = new Matrix(bIncidence);
        //------------------------imprimir matriz funcion-------------------------------
        //backwardsIncidence.print(backwardsIncidence.getRowDimension(), 0);
        //------------------------imprimir matriz funcion-------------------------------
        this.currentMarking = new Matrix(initialMarking,1);
        currentMarking.print(currentMarking.getRowDimension(), 0);
        this.sensibilizedTransitions = new Matrix(1, incidence.getColumnDimension());
        this.pInvariants = new Matrix(pInvariant);
        this.maxPInvariants = new Matrix(incidence.getRowDimension(), 1);
        this.workingVector = new Matrix(1, incidence.getColumnDimension());
        this.firedSequence = new ArrayList<String>();
        this.transitionCounter = new Matrix(1,14);
        for (int j = 0; j < 14; j++) {
            transitionCounter.set(0, j, 0.0);
        }
        //this.currentMarking = this.currentMarking.transpose();
        System.out.println("Marcado inicial columnas: "+ currentMarking.getColumnDimension());
        System.out.println("Marcado inicial filas: "+ currentMarking.getRowDimension());
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
        /*boolean flag = true;
        matriz = (currentMarking.transpose().plus(incidence.times(v.transpose())));
        for (int i = 0; i < this.matriz.getColumnDimension(); i++)
            if (this.matriz.get(0, i) < 0)
                flag = false;
        System.out.println("Matriz columnas: "+ matriz.getColumnDimension());
        System.out.println("Matriz filas: "+ matriz.getRowDimension());
        return flag ? matriz : null;*/
       return (currentMarking.transpose().plus(incidence.times(v.transpose()))).transpose();
       //       (mi                          +  w       *       s)          transpose??
    }

    // aguante el paco, las putas, la droga, los enanos, las enanas

    public boolean fundamentalEquationTest(Matrix firingVector) {
        System.out.println("marcado actual ");
        currentMarking.print(2,0);
        matriz = fundamentalEquation(firingVector);
        matriz.print(2,0);
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
        System.out.println("transpuesta columnas " + backwardsIncidence.getColumnDimension());
        System.out.println("transpuesta filas " + backwardsIncidence.getRowDimension());

        System.out.println("Marcado columnas: "+ currentMarking.getColumnDimension());
        System.out.println("Marcado filas: "+ currentMarking.getRowDimension());

        /*for (int i = 0; i < backwardsIncidence.getColumnDimension(); i++) {
            boolean flag = true;
            for (int j = 0; j < backwardsIncidence.getRowDimension(); j++) {
                double aux1 = backwardsIncidence.get(j, i);
                double aux2 = currentMarking.get(0,j);
                if (aux1 > aux2) {
                    flag = false;
                    break;
                } else {
                    flag = true;
                }
            }
            if(flag) {
                sensibilizedTransitions.set(0, i, 1);
            } else sensibilizedTransitions.set(0, i, 0);
        }*/

        currentMarking.print(2,0);
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
        System.out.println("sensibileas");
        sensibilizedTransitions.print(sensibilizedTransitions.getRowDimension(), 0);


    }


    /* 
     * - cambiar marcado actual
     * - actualizar sensibilizadas
     * - checkear invariantes
     * - agregar disparo a la secuencia
     * 
     * Este es el metodo fire transition pero quería usar el nombre 
     * fire transition en monitor asi q yo le pongo el nombre q se me canta el culo manga de putossss
     * chupenme la pija
     */

    void fire(Matrix v)             //esta es la que hace el disparo literal, actualizando la rdp
    {
        this.currentMarking = fundamentalEquation(v);  //.transpose()
        v.print(2,0);
        enableTransitions();
        setWorkingVector(v, 0);
        testPInvariants();
        firedSequence.add("T" + getIndex(v) + ""); //tiene TODAS las secuencia de transiciones disparadas
        System.out.println("Disparo: T" + getIndex(v));
        log.writeLog(getIndex(v));
        for(int i = 0; i < v.getRowDimension(); i++) {
            for(int j = 0; j < v.getColumnDimension(); j++) {
                if(v.get(i,j) != 0.0) {
                    transitionCounter.set(i,j,(transitionCounter.get(i,j)+v.get(i,j)));
                }
            }
        }
        System.out.println(transitionsCounterInfo());
        this.currentMarking.print(currentMarking.getRowDimension(),0);
    }

    public String transitionsCounterInfo() {
        String arg = "Cantidad de disparos de transiciones:\n";
        for(int i = 0; i < transitionCounter.getColumnDimension(); i++) {
            arg += ("   - T" + i + ": " + transitionCounter.get(0,i) + "veces\n");
        }
        return arg;
    }

    // ********************* */

    /*
     * hay q buscar una forma de hacer que reciba un vector de disparo
     * y me diga si es posible realizar un vector de disparo
     */

    public boolean isTransitionEnabled(int transition) 
    {
        return sensibilizedTransitions.get(transition, 0) == 1;
    }

    /*
     * 
     * HACER
     * 
     * 
     */
    public boolean testCondition()
    {
        return true;
    }

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


    /*
     * 
     */
    public void testPInvariants() {
        /*
        boolean pInv0, pInv1, pInv2, pInv3, pInv4, pInv5, pInv6, pInv7, pInv8;
        int i = 0;

        double[] pInv_0 = { currentMarking.get(0, 0), currentMarking.get(0, 3), currentMarking.get(0, 5),
                currentMarking.get(0, 6), currentMarking.get(0, 9), currentMarking.get(0, 11),
                currentMarking.get(0, 12), currentMarking.get(0, 13), currentMarking.get(0, 15),
                currentMarking.get(0, 16), currentMarking.get(0, 17) };
        double sumInv0 = 0;
        for (double markAux : pInv_0) {
            sumInv0 += markAux;
        }
        pInv0 = (sumInv0 == maxPInvariants.get(0, 0));

        double[] pInv_1 = { currentMarking.get(0, 1), currentMarking.get(0, 3) };
        double sumInv1 = 0;
        for (double markAux : pInv_1) {
            sumInv1 += markAux;
        }
        pInv1 = (sumInv1 == maxPInvariants.get(1, 0));

        double[] pInv_2 = { currentMarking.get(0, 2), currentMarking.get(0, 5) };
        double sumInv2 = 0;
        for (double markAux : pInv_2) {
            sumInv2 += markAux;
        }
        pInv2 = (sumInv2 == maxPInvariants.get(2, 0));

        double[] pInv_3 = { currentMarking.get(0, 13), currentMarking.get(0, 14), currentMarking.get(0, 15) };
        double sumInv3 = 0;
        for (double markAux : pInv_3) {
            sumInv3 += markAux;
        }
        pInv3 = (sumInv3 == maxPInvariants.get(3, 0));

        double[] pInv_4 = { currentMarking.get(0, 7), currentMarking.get(0, 9) };
        double sumInv4 = 0;
        for (double markAux : pInv_4) {
            sumInv4 += markAux;
        }
        pInv4 = (sumInv4 == maxPInvariants.get(4, 0));

        double[] pInv_5 = { currentMarking.get(0, 8), currentMarking.get(0, 10) };
        double sumInv5 = 0;
        for (double markAux : pInv_5) {
            sumInv5 += markAux;
        }
        pInv5 = (sumInv5 == maxPInvariants.get(5, 0));

        double[] pInv_6 = { currentMarking.get(0, 9), currentMarking.get(0, 10), currentMarking.get(0, 11) };
        double sumInv6 = 0;
        for (double markAux : pInv_6) {
            sumInv6 += markAux;
        }
        pInv6 = (sumInv6 == maxPInvariants.get(6, 0));

        double[] pInv_7 = { currentMarking.get(0, 17), currentMarking.get(0, 18) };
        double sumInv7 = 0;
        for (double markAux : pInv_7) {
            sumInv7 += markAux;
        }
        pInv7 = (sumInv7 == maxPInvariants.get(7, 0));

        double[] pInv_8 = { currentMarking.get(0, 3), currentMarking.get(0, 4), currentMarking.get(0, 5),
                currentMarking.get(0, 17) };
        double sumInv8 = 0;
        for (double markAux : pInv_8) {
            sumInv8 += markAux;
        }
        pInv8 = (sumInv8 == maxPInvariants.get(8, 0));

        boolean[] pInv = { pInv0, pInv1, pInv2, pInv3, pInv4, pInv5, pInv6, pInv7, pInv8 };

        for (boolean pInvariant : pInv) {
            if (!pInvariant) {
                System.out.println("No se cumple el invariante de plaza: pInv" + i);
            }
            i++;
        }*/

        int invariantAmount; //La cantidad de tokens que se mantiene invariante.
        int tokensAmount; //La cantidad de tokens que se van contando en las plazas.



        for(int j = 0; j < pInvariants.getRowDimension(); j++) {
            invariantAmount = 0;
            tokensAmount = 0;

            for(int i = 0; i < currentMarking.getColumnDimension(); i++)
                if(pInvariants.get(j, i) > 0) {
                    invariantAmount = (int)pInvariants.get(j, i);
                    tokensAmount = tokensAmount + (int)currentMarking.get(0, i);
                }

            if(tokensAmount != invariantAmount)
                System.out.println("Error en el invariante de plaza: IP" + j);

        }

    }

    /*
     * ************************
     * *** Geters & Setters ***
     * ************************
     */

    /*
     * Idea: tener en un vector la cantidad máxima de tokens para c/p invariante
     * esto sirve para checkear los p invariants
     */
    public Matrix setMaxPInvariants() 
    {
        Matrix auxMatrix = new Matrix(pInvariants.getColumnDimension(), 1);
        for (int i = 0; i < pInvariants.getColumnDimension(); i++) {
            int max = 0;
            for (int j = 0; j < pInvariants.getRowDimension(); j++) {
                max += pInvariants.get(i, j);
            }
            auxMatrix.set(i, 0, max);
        }
        return auxMatrix;

    }

    public void setMarking(Matrix marking) 
    {
        this.currentMarking = marking;
    }

    public Matrix getSensibilized() 
    {
        return sensibilizedTransitions;
    }

    public ArrayList<String> getFiredSequence() 
    {
        return firedSequence;
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

    public Matrix getIncidenceMatrix() 
    {
        return this.incidence;
    }
}
