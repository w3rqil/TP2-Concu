import java.util.ArrayList;
import java.util.Arrays;

import Jama.Matrix;

public class main 
{

    private static ArrayList<Matrix> threadPaths; //Arreglo que contiene los "caminos" de cada hilo (secuencia de transiciones a ejecutar).

    private static final int amountThreads = 7; // Depende de la cantidad de Path ???????? e asi o no?

    private static double[] initialMark = { 1,1,1,0,3,0,0,1,1,0,2,0,0,0,1,0,0,0,1 }; //Marcado inicial de la red.

    private static PetriNet petrinet  ;    //Red de petri representativa del sistema.

    private static Monitor monitor;  //Monitor que controlará la red de Petri que modela el sistema.
    private static double[] loader1={0,2};
    //1 y 3
    private static double[] loader2= {1,3};
    // 4 y 6
    private static double[] resizer1={4,6}; 
    // 5 y 7
    private static double[] resizer2={5,7};
    // 8 y 10
    private static double[] improver1={8,10};
    // 9 y 11
    private static double[] improver2={9,11};
    // 12 y 13
    private static double[] exit={12,13};

    // matrices con transiciones asociadas a los hilos
    private static Matrix loader1Path = new Matrix(loader1, 1);   // version traspuesta de P0
    private static Matrix loader2Path = new Matrix(loader2, 1);   // version traspuesta de P1
    private static Matrix resizer1Path = new Matrix(resizer1, 1);   // version traspuesta de P2
    private static Matrix resizer2Path = new Matrix(resizer2, 1);   // version traspuesta de P3
    private static Matrix improver1Path = new Matrix(improver1, 1);   // version traspuesta de P4
    private static Matrix improver2Path = new Matrix(improver2, 1);   // version traspuesta de P5
    private static Matrix exitPath = new Matrix(exit, 1);   // version traspuesta de P6

    /**
     * Método principal.
     * 
     * Aquí se instancian y ejecutan los hilos con sus caminos asociados.
     * También se inicializan tanto la red de Petri con su marcado inicial
     * como el monitor y el hilo logger.
     */

    public static void main(String args[]) 
    {

        Log log = new Log();

        log.clearFile();

        long start = System.currentTimeMillis();

        Matrix initialMarking = new Matrix(initialMark, 1);

        petrinet = new PetriNet(log);

        Policy policy = new Policy("Equitative");

        //pNet.setCurrentMarkingVector(initialMarking);  //ESTO NO VA ME PARECE

        monitor = new Monitor(petrinet,policy);

        Threads[] threads = new Threads[amountThreads];  // public Threads(Matrix transitionsSequence, Monitor monitor) 

        //ACA 
     /*   
     for (int i = 0; i < amountThreads; i++) {
             Threads worker = new Threads();
          //  loaders[i] = new Loader(initContainer, "TP1.Loader " + i);
              threads[i] = new Thread(worker);
          //  loadersThreads[i].setName(loaders[i].getName() + " (Thread ID: " + loadersThreads[i].getId() + ")");
        }*/

        petrinet.enableTransitions(); //Seteo de las transiciones sensibilizadas dado el marcado inicial de la red.

        threads[1] = new Threads(loader1Path,monitor,"Loader 1");
        threads[2] = new Threads(loader2Path,monitor,"Loader 2");
        threads[3] = new Threads(resizer1Path,monitor,"Resizer 1");
        threads[4] = new Threads(resizer2Path,monitor,"Resizer 2");
        threads[5] = new Threads(improver1Path,monitor,"Improver 1");
        threads[6] = new Threads(improver2Path,monitor,"Improver 2");
        threads[7] = new Threads(exitPath,monitor,"Exit");

        try {
            for (Thread waiting : threads) {
                waiting.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Vector contador: ");
        System.out.println(Arrays.toString(petrinet.getContador()));
        System.out.println("Duracion del programa: " + (System.currentTimeMillis() - horaInicio));

        /*
         System.out.println("\nPerformance stats:");
        System.out.println("Elapsed Time: " + (float) (timeElapsed / 1000.00) + " seconds");
        System.out.println("Final InitContainer size: " + initContainer.getSize());
        System.out.println("Final FinalContainer size: " + finalContainer.getSize());
        System.out.println("Total Loaded Images: " + totalLoadedImages);
        System.out.println("Total improvements: " + totalImprovements);
        System.out.println("Total resizing: " + totalResizing);
        System.out.println("Total cloned Images " + totalClonedImages); */
    }

        log.closeFile();
    }
}