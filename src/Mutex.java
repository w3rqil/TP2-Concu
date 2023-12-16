import java.util.concurrent.Semaphore;

public class Mutex {            ///CLASE NO SE USA BORRAR

    private static Mutex instance;
    private Semaphore mutexInstance;

    private Mutex() {
        this.mutexInstance = new Semaphore(1, true);
    }

    public static Mutex getInstance() {
        if (instance == null) {
            synchronized (Mutex.class) {
                if (instance == null) {
                    instance = new Mutex();
                }
            }
        }
        return instance;
    }


    public void catchMonitor() throws InterruptedException {
        mutexInstance.acquire();
    }


    public void exitMonitor() {
        mutexInstance.release();
    }










}
