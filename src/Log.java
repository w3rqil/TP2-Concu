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
    public Log() {
        try {
            file = new FileWriter("./effect/log.txt");
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
    public void closeFile() {
        try {
            pw.write(rdp.transitionsCounterInfo());  //      rdp.transitionsCounterInfo() devuelve string
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
