import java.util.concurrent.TimeUnit;

public class T8 {

    public static void main(String[] args) throws InterruptedException {

        CDPlayer cdPlayer = new CDPlayer();

        cdPlayer.start();
        TimeUnit.MICROSECONDS.sleep(10);
        cdPlayer.suspend();
        TimeUnit.MICROSECONDS.sleep(10);
        cdPlayer.resume();
        TimeUnit.MICROSECONDS.sleep(10);
        cdPlayer.suspend();
        cdPlayer.stop();
    }

    public static class CDPlayer implements Runnable {

        private volatile int flag = 1;

        private static final Object lock = new Object();

        Thread thread;

        public CDPlayer() {
            thread = new Thread(this);
        }

        @Override
        public void run() {
            while (!thread.isInterrupted()) {
                synchronized (lock) {
                    while (flag == 2) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    singing();
                }
            }
        }

        public void start(){
            thread.start();
        }

        public void stop() {
            thread.interrupt();
            System.out.println("cdPlayer.stop");
        }

        public void resume() {
            synchronized (lock) {
                flag = 1;
                lock.notifyAll();
            }
            System.out.println("cdPlayer.resume");
        }

        public void suspend() {
            flag = 2;
            System.out.println("cdPlayer.suspend");
        }

        private void singing() {
            System.out.println("singing.....");
        }
    }

}
