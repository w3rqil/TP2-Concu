import java.util.ArrayList;
import Jama.Matrix;

public class Threads extends Thread {
    
    private ArrayList<Matrix> transitions;
    private Matrix firingVector;
    private Monitor monitor;
    private int transitionCounter; 
    private String name;

    public Threads(Matrix transitionsSequence, Monitor monitor, String name) {   // Procesinhos cerra el orto vos
        this.transitions = new ArrayList<Matrix>();
        this.name = name;
        transitionsSequence.print(2,0);

        for (int i = 0; i < transitionsSequence.getColumnDimension(); i++) {
            int index = (int) transitionsSequence.get(0, i);
            Matrix aux = new Matrix(1, monitor.getPetriNet().getIncidenceMatrix().getColumnDimension());
            aux.set(0, index, 1);
            this.transitions.add(aux);
        }
        this.monitor = monitor;
        this.transitionCounter = 0;
    }

    public void nextTransition() {
        this.transitionCounter++;
        if (transitionCounter >= transitions.size()) {
            this.transitionCounter = 0;
        }
    }

    public String getThreadName() {
        return this.name;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getId() + ": started run()");
        while (!this.monitor.testCondition()) {
            System.out.println("ENTRO WACHO ENTRO EL HILO:"+ Thread.currentThread().getId());
            this.firingVector = transitions.get(transitionCounter);

            firingVector.print(2,0);
            if (monitor.fireTransition(firingVector)) {
                nextTransition();
            }
        }
        System.out.println(Thread.currentThread().getId() + ": finished run()");
    }
}