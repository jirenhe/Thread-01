import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class T4 {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread newThread = new Thread(() -> {
        });

        Thread runThread = new Thread(() -> {
            while (true) ;
        });

        Thread blockThread = new Thread(() -> {
            synchronized (lock) {

            }
        });

        Thread waitThread = new Thread(LockSupport::park);

        Thread timeWaitThread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread terminatedThread = new Thread(() -> {
        });

        synchronized (lock) {
            runThread.start();
            blockThread.start();
            waitThread.start();
            timeWaitThread.start();
            terminatedThread.start();

            TimeUnit.SECONDS.sleep(1);

            System.out.println("newThread sate is " + newThread.getState());
            System.out.println("runThread sate is " + runThread.getState());
            System.out.println("blockThread sate is " + blockThread.getState());
            System.out.println("waitThread sate is " + waitThread.getState());
            System.out.println("timeWaitThread sate is " + timeWaitThread.getState());
            System.out.println("terminatedThread sate is " + terminatedThread.getState());
        }
    }

}
