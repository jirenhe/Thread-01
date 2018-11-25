public class T5 {

    public static void main(String[] args) throws InterruptedException {

        MyThread myThread = new MyThread();
        myThread.start();
        myThread.await();
        System.exit(-1);

        Thread t = new Thread(() -> {
            while (true) ;
        });

        synchronized (t) {
            t.wait();
            System.out.println(11);
            t.notify();

        }

    }

    public static class MyThread extends Thread {
        @Override
        public void run() {
            while (true) {
                System.out.println("hello!");
            }
        }

        public void await() throws InterruptedException {
            synchronized (this) {
                this.wait();
            }
        }
    }


}
