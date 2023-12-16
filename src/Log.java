/*
        Imprimir: Disparos de transiciones 
                  Contador para cantidad de invariantes completadas
*/

import java.util.concurrent.Semaphore;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Jama.Matrix;

import java.io.File;
import java.io.IOException;

public class Log extends Thread{

    private PetriNet    petrinet; //Red de Petri del sistema.
    private Monitor     monitor; //Monitor que controla la red de Petri.
    private File        f;         //v
    private FileHandler FH; //Campos necesarios para loggear.
    private Logger      logger;  //^

    public Log(String fileName, PetriNet pNet, Monitor monitor) {

        this.petrinet = pNet;
        this.monitor = monitor;
        f = new File(fileName);

        if(!f.exists()) f.createNewFile();

        SimpleFormatter formatter = new SimpleFormatter();

        FH = new FileHandler(fileName, true);

        FH.setFormatter(formatter);


    }

    /* Escribe en el archivo log la transicion disparada */
    public void writeLog(int transicion) {


    }

    /* Cierra el archivo log */
    public void closeFile(PetriNet petrinet) {

    }

    public void clearFile() {

}




    public void run() {
        long startTime = System.currentTimeMillis();

        logger = Logger.getLogger("ReportTest");

        logger.addHandler(FH);
        logger.setLevel(Level.INFO);

        while(!pNet.hasCompleted()) {
            try {
                sleep(1000);
                logger.info("\n" + pNet.getMemoriesLoad() +
                        "\n" + pNet.getProcessorsLoad() +
                        "\n" + pNet.getProcessorsTasks());
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        long finishTime = System.currentTimeMillis();

        logger.info("\n" + pNet.getMemoriesLoad() +
                "\n" + pNet.getProcessorsLoad() +
                "\n" + pNet.getProcessorsTasks());

        logger.info("\nEl tiempo de ejecucion fue de: " + (int)((finishTime - startTime) / 1000) + " segundos.");

        Matrix finalMarkingVector = pNet.getCurrentMarkingVector();

        String finalMarking = "[ ";

        for(int i = 0; i < finalMarkingVector.getColumnDimension(); i++)
            finalMarking += (int)finalMarkingVector.get(0, i) + " ";

        finalMarking += "]";

        //Despierta a los hilos encolados en las colas de condición de la red.
        for(Semaphore queue : monitor.getConditionQueues().getSemaphore())
            if(queue.hasQueuedThreads())
                queue.release(queue.getQueueLength());

        //Chequeo de hilos encolados en ArrivalRate.
        if(monitor.getEntryQueue().hasQueuedThreads())
            monitor.getEntryQueue().release(monitor.getEntryQueue().getQueueLength());

        logger.info("Secuencia de transiciones disparadas: \"" + pNet.getTransitionsSequence().toString() + "\"");
        logger.info("Marcado final de la red: " + finalMarking);
    }
}