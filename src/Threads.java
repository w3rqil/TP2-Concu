import java.util.ArrayList;
import Jama.Matrix;
import java.util.concurrent.TimeUnit;

public class Threads extends Thread {
    
    private ArrayList<Matrix> transitions;
    private Matrix firingVector;
    private Monitor monitor;
    private int transitionCounter;
    private String name;


    public Threads(Matrix transitionsSequence, Monitor monitor, String name)
    {
        this.transitions = new ArrayList<Matrix>();
        this.name = name;
        transitionsSequence.print(2,0);

        for (int i = 0; i < transitionsSequence.getColumnDimension(); i++)
        {
            int index = (int) transitionsSequence.get(0, i);
            Matrix aux = new Matrix(1, monitor.getPetriNet().getIncidenceMatrix().getColumnDimension());
            aux.set(0, index, 1);
            this.transitions.add(aux);
        }
        this.monitor = monitor;
        this.transitionCounter = 0;
    }

    public void nextTransition()
    {
        this.transitionCounter++;
        if (transitionCounter >= transitions.size())
        {
            this.transitionCounter = 0;
        }
    }

    public String getThreadName() {
        return this.name;
    }

    @Override
    public void run()
    {
        System.out.println("Thread " + getThreadName() + ": started run()");
        while (this.monitor.getPetriNet().getCompletedInvariants() < 200)
        {
            this.firingVector = transitions.get(transitionCounter);
            if (monitor.fireTransition(firingVector))
            {
                nextTransition();
            } else
            {
                long sleepTime;
                try
                {
                    sleepTime = this.monitor.getTimeLeft(Thread.currentThread().getId());
                }
                catch (Exception e)
                {
                    sleepTime = 0;
                }
                if(!(this.monitor.getPetriNet().getCompletedInvariants() < 200))
                {
                    try
                    {
                        TimeUnit.MILLISECONDS.sleep(sleepTime);
                    } catch(Exception e)
                    {
                        System.err.println("❌  interrupted while sleeping  ❌");
                        System.exit(1);     // Stop the program with a non-zero exit code
                    }
                }
            }
        }
        this.monitor.addDeadThreads();
        System.out.println("Thread " + getThreadName() + ": finished run()");
    }
}

