import java.util.ArrayList;
import java.util.Arrays;

import Jama.Matrix;

public class Main {
    private static final int amountThreads = 7; // Depende de la cantidad de Path ???????? e asi o no?
    private static PetriNet petrinet; // Red de petri representativa del sistema.
    private static Monitor monitor; // Monitor que controlará la red de Petri que modela el sistema.
    private static double[] loader1 = { 0, 2 };
    // 1 y 3
    private static double[] loader2 = { 1, 3 };
    // 4 y 6
    private static double[] resizer1 = { 4, 6 };
    // 5 y 7
    private static double[] resizer2 = { 5, 7 };
    // 8 y 10
    private static double[] improver1 = { 8, 10 };
    // 9 y 11
    private static double[] improver2 = { 9, 11 };
    // 12 y 13
    private static double[] exit = { 12, 13 };

    // matrices con transiciones asociadas a los hilos
    private static Matrix loader1Path = new Matrix(loader1, 1); // version traspuesta de P0
    private static Matrix loader2Path = new Matrix(loader2, 1); // version traspuesta de P1
    private static Matrix resizer1Path = new Matrix(resizer1, 1); // version traspuesta de P2
    private static Matrix resizer2Path = new Matrix(resizer2, 1); // version traspuesta de P3
    private static Matrix improver1Path = new Matrix(improver1, 1); // version traspuesta de P4
    private static Matrix improver2Path = new Matrix(improver2, 1); // version traspuesta de P5
    private static Matrix exitPath = new Matrix(exit, 1); // version traspuesta de P6

    /**
     * Método principal.
     * 
     * Aquí se instancian y ejecutan los hilos con sus caminos asociados.
     * También se inicializan tanto la red de Petri con su marcado inicial
     * como el monitor y el hilo logger.
     */

    public static void main (String args[]) {

        long start = System.currentTimeMillis();

        Log log = new Log(start);

        //log.clearFile(); // que poronga es esto gordo te voy a cagar a bifes

        petrinet = new PetriNet(log);

        Policy policy = new Policy("Equitative");

        // pNet.setCurrentMarkingVector(initialMarking); //ESTO NO VA ME PARECE

        monitor = new Monitor(petrinet, policy);

        Threads[] threads = new Threads[amountThreads]; // public Threads(Matrix transitionsSequence, Monitor monitor)

        petrinet.enableTransitions(); // Seteo de las transiciones sensibilizadas dado el marcado inicial de la red.

        threads[0] = new Threads(loader1Path, monitor, "Loader 1");
        threads[1] = new Threads(loader2Path, monitor, "Loader 2");
        threads[2] = new Threads(resizer1Path, monitor, "Resizer 1");
        threads[3] = new Threads(resizer2Path, monitor, "Resizer 2");
        threads[4] = new Threads(improver1Path, monitor, "Improver 1");
        threads[5] = new Threads(improver2Path, monitor, "Improver 2");
        threads[6] = new Threads(exitPath, monitor, "Exit");

        for (Threads thread : threads) {
            thread.start();
        }

        try {
            for (Threads waiting : threads) {
                waiting.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(petrinet.transitionsCounterInfo());

        log.closeFile(petrinet);
    }
}