import java.util.concurrent.TimeUnit;

public class T7 {

    public static void main(String[] args) throws InterruptedException {

        MyThread1 myThread1 = new MyThread1();
        MyThread2 myThread2 = new MyThread2();
        MyThread3 myThread3 = new MyThread3();
        MyThread4 myThread4 = new MyThread4();
        myThread1.start();
        myThread2.start();
        myThread3.start();
        myThread4.start();
        TimeUnit.SECONDS.sleep(1);
        myThread1.interrupt();
        myThread2.interrupt();
        myThread3.interrupt();
        myThread4.interrupt();

    }

    public static class MyThread1 extends Thread {

        @Override
        public void run() {
            while (!this.isInterrupted()) ;
            System.out.println("MyThread1 is isInterrupted : " + this.isInterrupted());
            Thread.interrupted();
            System.out.println("MyThread1 is isInterrupted : " + this.isInterrupted());

        }
    }

    public static class MyThread2 extends Thread {

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(10000000);
            } catch (InterruptedException e) {
                System.out.println("MyThread2 InterruptedException happend!");
                System.out.println("MyThread2 is isInterrupted : " + this.isInterrupted());
            }

        }
    }

    public static class MyThread3 extends Thread {

        @Override
        public void run() {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("MyThread3 InterruptedException happend!");
                    System.out.println("MyThread4 is isInterrupted : " + this.isInterrupted());
                }
            }

        }
    }

    public static class MyThread4 extends Thread {

        @Override
        public void run() {
            synchronized (this) {
                try {
                    this.wait(5000);
                } catch (InterruptedException e) {
                    System.out.println("MyThread4 InterruptedException happend!");
                    System.out.println("MyThread4 is isInterrupted : " + this.isInterrupted());
                }
            }

        }
    }

}
