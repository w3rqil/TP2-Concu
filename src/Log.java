/*
        Imprimir: Disparos de transiciones 
                  Contador para cantidad de invariantes completadas
*/

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Log {

    private PrintWriter pw;
    private FileWriter file;
    // crea el archivo log
    private static long start;

    public Log(long start) {
        this.start = start;
        try {
            file = new FileWriter("./log.txt");
            pw = new PrintWriter(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* Escribe en el archivo log la transicion disparada */
    public void writeLog(int transicion) {
        try {
            if (transicion < 10)
                pw.write("T0" + transicion);
            else
                pw.write("T" + transicion);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* Cierra el archivo log */
    public void closeFile(PetriNet petrinet) {
        try {
            pw.write(petrinet.transitionsCounterInfo());  //      rdp.transitionsCounterInfo() devuelve string
            pw.write("Tiempo de ejecucion: " + start);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearFile() {
        try {
            PrintWriter pw_log = new PrintWriter(".//Estadistica.txt");
            pw_log.print("");
            pw_log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
