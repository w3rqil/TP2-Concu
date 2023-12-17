import Jama.Matrix;

public class Main {
    private static final int amountThreads = 14;
    private static final String policyType = "Equitative";
    private static PetriNet petrinet; // Red de petri representativa del sistema.
    private static Monitor monitor; // Monitor que controlará la red de Petri que modela el sistema.
    private static double[] loader1 = { 0 };
    private static double[] loader11 = { 2 };
    // 1 y 3
    private static double[] loader2 = { 1 };
    private static double[] loader21 = { 3 };
    // 4 y 6
    private static double[] resizer1 = { 4 };
    private static double[] resizer11 = { 6 };
    // 5 y 7
    private static double[] resizer2 = { 5 };
    private static double[] resizer21 = { 7 };
    // 8 y 10
    private static double[] improver1 = { 8 };
    private static double[] improver11= { 10 };
    // 9 y 11
    private static double[] improver2 = { 9 };
    private static double[] improver21 = { 11 };
    // 12 y 13
    private static double[] exit = { 12 };
    private static double[] exit1 = { 13 };

    // matrices con transiciones asociadas a los hilos
    private static Matrix loader1Path = new Matrix(loader1, 1); // version traspuesta de P0
    private static Matrix loader2Path = new Matrix(loader2, 1); // version traspuesta de P1
    private static Matrix resizer1Path = new Matrix(resizer1, 1); // version traspuesta de P2
    private static Matrix resizer2Path = new Matrix(resizer2, 1); // version traspuesta de P3
    private static Matrix improver1Path = new Matrix(improver1, 1); // version traspuesta de P4
    private static Matrix improver2Path = new Matrix(improver2, 1); // version traspuesta de P5
    private static Matrix exitPath = new Matrix(exit, 1); // version traspuesta de P6
    private static Matrix loader11Path = new Matrix(loader11, 1); // version traspuesta de P0
    private static Matrix loader21Path = new Matrix(loader21, 1); // version traspuesta de P1
    private static Matrix resizer11Path = new Matrix(resizer11, 1); // version traspuesta de P2
    private static Matrix resizer21Path = new Matrix(resizer21, 1); // version traspuesta de P3
    private static Matrix improver11Path = new Matrix(improver11, 1); // version traspuesta de P4
    private static Matrix improver21Path = new Matrix(improver21, 1); // version traspuesta de P5
    private static Matrix exit1Path = new Matrix(exit1, 1); // version traspuesta de P6

    /**
     * Método principal.
     *
     * Aquí se instancian y ejecutan los hilos con sus caminos asociados.
     * También se inicializan tanto la red de Petri con su marcado inicial
     * como el monitor y el hilo logger.
     */
//
    public static void main (String args[]) {

        long start = System.currentTimeMillis();

        //log.clearFile(); // que poronga es esto gordo te voy a cagar a bifes

        petrinet = new PetriNet();

        // Policy policy = new Policy("8020");
        Policy policy = new Policy(policyType);
        System.out.println("Policy type: "+ policyType +" \n");
        // pNet.setCurrentMarkingVector(initialMarking); //ESTO NO VA ME PARECE

        monitor = new Monitor(petrinet, policy);

        Threads[] threads = new Threads[amountThreads]; // public Threads(Matrix transitionsSequence, Monitor monitor)

        petrinet.enableTransitions(); // Seteo de las transiciones sensibilizadas dado el marcado inicial de la red.

        //Creación y ejecución del hilo logger.
        try {
            long startTime = System.currentTimeMillis();
            Log log = new Log( petrinet, monitor, startTime,true);
            log.start();

            Log invariant = new Log( petrinet, monitor, startTime,false);
            invariant.start();


        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error creating logger.");
        }



        threads[0] = new Threads(loader1Path, monitor, "Loader 1");

        threads[1] = new Threads(loader2Path, monitor, "Loader 2");
        threads[2] = new Threads(resizer1Path, monitor, "Resizer 1");
        threads[3] = new Threads(resizer2Path, monitor, "Resizer 2");
        threads[4] = new Threads(improver1Path, monitor, "Improver 1");
        threads[5] = new Threads(improver2Path, monitor, "Improver 2");
        threads[6] = new Threads(exitPath, monitor, "Exit");

        threads[7] = new Threads(loader11Path, monitor, "Loader 1.1");
        threads[8] = new Threads(loader21Path, monitor, "Loader 2.1");
        threads[9] = new Threads(resizer11Path, monitor, "Resizer 1.1");
        threads[10] = new Threads(resizer21Path, monitor, "Resizer 2.1");

        threads[11] = new Threads(improver11Path, monitor, "Improver 1.1");
        threads[12] = new Threads(improver21Path, monitor, "Improver 2.1");
        threads[13] = new Threads(exit1Path, monitor, "Exit1");

        for (Threads thread : threads) {
            thread.start();
        }

        /*
        for(int i=0; i<9 ; i++){
            //System.out.println("Invariante "+ i +" aparece "+ petrinet.getValinvariantCounting(i) +" VECES ");
            System.out.println(i +" T-invariant appears "+ petrinet.getValinvariantCounting(i) +" times.");
        }*/



        monitor.printDaDead();


        System.out.println(petrinet.transitionsCounterInfo());



    }
}