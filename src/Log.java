import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

import java.util.concurrent.Semaphore;

import Jama.Matrix;

// Clase que crea el Log estadístico para la ejecución del programa
public class Log extends Thread {

    private int[] occurrences;

    // Campos privados
    private PetriNet petrinet; // Red de Petri del sistema.
    private Monitor monitor; // Monitor que controla la red de Petri.

    private boolean isLog;

    private long startTime;

    // Método que crea un archivo txt limpio

    // Constructor
    public Log(PetriNet petrinet, Monitor monitor, long startTime, boolean isLog) throws IOException {

        this.petrinet = petrinet;
        this.monitor = monitor;
        this.startTime = startTime;
        this.isLog = isLog;

        this.occurrences = new int[14];

    }

    // Sobreescritura del método run()
    @Override
    public void run() {

        while (this.monitor.getPetriNet().getCompletedInvariants() < 200) {
            try {
                sleep(5);
                // pw_log.print("\n" + petrinet.getAllTransitionsPrint());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        writeLog();

    }

    // Se escribe el Log
    private void writeLog() {

        try {

            if (isLog) {

                File archivo = new File(".//statistics.txt");

                archivo.delete();

                PrintWriter pw_log = new PrintWriter(new FileWriter(".//statistics.txt", true));

                pw_log.print("\n\n");

                pw_log.print(
                        "\n                                                                                                   ");

                pw_log.print(
                        "\n          ░██████╗████████╗░█████╗░████████╗██╗░██████╗████████╗██╗░█████╗░░██████╗               ");
                pw_log.print(
                        "\n          ██╔════╝╚══██╔══╝██╔══██╗╚══██╔══╝██║██╔════╝╚══██╔══╝██║██╔══██╗██╔════╝               ");
                pw_log.print(
                        "\n          ╚█████╗░░░░██║░░░███████║░░░██║░░░██║╚█████╗░░░░██║░░░██║██║░░╚═╝╚█████╗░               ");
                pw_log.print(
                        "\n          ░╚═══██╗░░░██║░░░██╔══██║░░░██║░░░██║░╚═══██╗░░░██║░░░██║██║░░██╗░╚═══██╗               ");
                pw_log.print(
                        "\n          ██████╔╝░░░██║░░░██║░░██║░░░██║░░░██║██████╔╝░░░██║░░░██║╚█████╔╝██████╔╝               ");
                pw_log.print(
                        "\n          ╚═════╝░░░░╚═╝░░░╚═╝░░╚═╝░░░╚═╝░░░╚═╝╚═════╝░░░░╚═╝░░░╚═╝░╚════╝░╚═════╝░               ");

                pw_log.print(
                        "\n                                                                                               ");
                pw_log.print(
                        "\n                                                                                               ");
                pw_log.print(
                        "\n                    ➡️ T̲r̲a̲n̲s̲i̲t̲i̲o̲n̲ I̲n̲v̲a̲r̲i̲a̲n̲t̲s̲ P̲a̲r̲t̲i̲c̲i̲p̲a̲t̲i̲o̲n̲ R̲e̲g̲i̲s̲t̲e̲r̲                           ");
                pw_log.print(
                        "\n                                                                                               ");

                pw_log.print("\n                        0️⃣  T-invariant appears " + petrinet.getValinvariantCounting(0)
                        + " times.                                ");
                pw_log.print("\n                        1️⃣  T-invariant appears " + petrinet.getValinvariantCounting(1)
                        + " times.                                  ");
                pw_log.print("\n                        2️⃣  T-invariant appears " + petrinet.getValinvariantCounting(2)
                        + " times.                                  ");
                pw_log.print("\n                        3️⃣  T-invariant appears " + petrinet.getValinvariantCounting(3)
                        + " times.                                  ");
                pw_log.print("\n                        4️⃣  T-invariant appears " + petrinet.getValinvariantCounting(4)
                        + " times.                                  ");
                pw_log.print("\n                        5️⃣  T-invariant appears " + petrinet.getValinvariantCounting(5)
                        + " times.                                  ");
                pw_log.print("\n                        6️⃣  T-invariant appears " + petrinet.getValinvariantCounting(6)
                        + " times.                                  ");
                pw_log.print("\n                        7️⃣  T-invariant appears " + petrinet.getValinvariantCounting(7)
                        + " times.                                  ");
                pw_log.print(
                        "\n                                                                                               ");
                pw_log.print(
                        "\n                                                                                               ");

                long finishTime = System.currentTimeMillis();

                pw_log.print(
                        "\n                    ➡️ T̲r̲i̲g̲g̲e̲r̲e̲d̲ t̲r̲a̲n̲s̲i̲t̲i̲o̲n̲s                                                ");
                pw_log.print(
                        "\n                                                                                               ");

                pw_log.print("\n                        🔹"
                        + petrinet.getAllTransitionsPrint().substring(4, petrinet.getAllTransitionsPrint().length()));

                pw_log.print(
                        "\n                                                                                               ");
                pw_log.print(
                        "\n                                                                                               ");

                pw_log.print(
                        "\n                    ➡️ T̲h̲e̲ e̲x̲e̲c̲u̲t̲i̲o̲n̲ t̲i̲m̲e̲ w̲a̲s̲:                                              ");
                pw_log.print(
                        "\n                                                                                               ");

                pw_log.print("\n                        ⏲️ " + (float) ((finishTime - startTime) / 1000) + " second.");

                Matrix finalMarkingVector = petrinet.getCurrentMarking();

                String finalMarking = "[ ";

                for (int i = 0; i < finalMarkingVector.getColumnDimension(); i++)
                    finalMarking += (int) finalMarkingVector.get(0, i) + " ";

                finalMarking += "]";

                // Despierta a los hilos encolados en las colas de condición de la red.
                for (Semaphore queue : monitor.getConditionQueues().getSemaphore())
                    if (queue.hasQueuedThreads())
                        queue.release(queue.getQueueLength());

                // Chequeo de hilos encolados en ArrivalRate.
                if (monitor.getMutex().hasQueuedThreads())
                    monitor.getMutex().release(monitor.getMutex().getQueueLength());

                int aux[] = petrinet.occurrencesArr();

                pw_log.print(
                        "\n                    ➡️ T̲r̲a̲n̲s̲i̲t̲i̲o̲n̲ T̲r̲i̲g̲g̲e̲r̲ O̲c̲c̲u̲r̲r̲e̲n̲c̲e̲s̲                                                ");

                pw_log.print("\n                     🔹 Transition  T0  appears " + aux[0]
                        + "   times.             ➡️    Percentage        " + aux[0] / 14);
                pw_log.print("\n                     🔹 Transition  T1  appears " + aux[1]
                        + "   times.             ➡️    Percentage        " + aux[1] / 14);
                pw_log.print("\n                     🔹 Transition  T2  appears " + aux[2]
                        + "   times.             ➡️    Percentage        " + aux[2] / 14);
                pw_log.print("\n                     🔹 Transition  T3  appears " + aux[3]
                        + "   times.             ➡️    Percentage        " + aux[3] / 14);
                pw_log.print("\n                     🔹 Transition  T4  appears " + aux[4]
                        + "   times.             ➡️    Percentage        " + aux[4] / 14);
                pw_log.print("\n                     🔹 Transition  T5  appears " + aux[5]
                        + "   times.             ➡️    Percentage        " + aux[5] / 14);
                pw_log.print("\n                     🔹 Transition  T6  appears " + aux[6]
                        + "   times.             ➡️    Percentage        " + aux[6] / 14);
                pw_log.print("\n                     🔹 Transition  T7  appears " + aux[7]
                        + "   times.             ➡️    Percentage        " + aux[7] / 14);
                pw_log.print("\n                     🔹 Transition  T8  appears " + aux[8]
                        + "   times.             ➡️    Percentage        " + aux[8] / 14);
                pw_log.print("\n                     🔹 Transition  T9  appears " + aux[9]
                        + "   times.             ➡️    Percentage        " + aux[9] / 14);
                pw_log.print("\n                     🔹 Transition  T10 appears " + aux[10]
                        + " times.             ➡️    Percentage        " + aux[10] / 14);
                pw_log.print("\n                     🔹 Transition  T11 appears " + aux[11]
                        + " times.             ➡️    Percentage        " + aux[11] / 14);
                pw_log.print("\n                     🔹 Transition  T12 appears " + aux[12]
                        + " times.             ➡️    Percentage        " + aux[12] / 14);
                pw_log.print("\n                     🔹 Transition  T13 appears " + aux[13]
                        + " times.             ➡️    Percentage        " + aux[13] / 14);

                pw_log.println();
                pw_log.print("\n\n");
                pw_log.close();

            } else {

                File archivo = new File(".//transitions.txt");

                archivo.delete();

                PrintWriter inv_log = new PrintWriter(new FileWriter(".//transitions.txt", false));
                String withoutNull = "";

                withoutNull = petrinet.getAllTransitionsPrint().substring(4,
                        petrinet.getAllTransitionsPrint().length());

                inv_log.print(withoutNull);

                inv_log.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
