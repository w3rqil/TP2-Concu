import java.util.ArrayList;
import java.util.Arrays;

import Jama.Matrix;

public class main {

    private static ArrayList<Matrix> threadPaths; //Arreglo que contiene los "caminos" de cada hilo (secuencia de transiciones a ejecutar).
   
    private static int threadAmount;

    private static double[] initialMark = { 1,1,1,0,3,0,0,1,1,0,2,0,0,0,1,0,0,0,1 }; //Marcado inicial de la red.

    private static PetriNet pNet  ;    //Red de petri representativa del sistema.

    private static Monitor monitor;  //Monitor que controlará la red de Petri que modela el sistema.
    

    /**
     * Método principal.
     * 
     * Aquí se instancian y ejecutan los hilos con sus caminos asociados.
     * También se inicializan tanto la red de Petri con su marcado inicial
     * como el monitor y el hilo logger.
     */
    
    public static void main(String args[]) {

        Log log = new Log(pNet, monitor);
       
        Matrix initialMarking = new Matrix(initialMark, 1);

        pNet = new PetriNet();

        pNet.setCurrentMarkingVector(initialMarking);  //AGREGAR

        monitor = new Monitor(pNet);

        
        MyThread[] threads = new MyThread[];
        
        pNet.setEnabledTransitions(); //Seteo de las transiciones sensibilizadas dado el marcado inicial de la red.

        //Creación y ejecución del hilo logger.
        try {
            log.start();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error al crear el hilo logger.");
        }
        
        //Creación de hilos de ejecución.
        for(int i = 0; i < threadAmount; i++) {
            threads[i] = new MyThread(threadPaths.get(i), monitor);
            threads[i].start();
        }


         System.out.println("Vector contador: ");
        System.out.println(Arrays.toString(rdp.getContador()));
        System.out.println("Duracion del programa: " + (System.currentTimeMillis() - horaInicio));

        log.closeFile();
    }
}