import java.util.concurrent.TimeUnit;

public class T1 {

    private static volatile boolean stopFlag = false;

    public static void main(String[] args) throws InterruptedException {
        MyThread1 myThread1 = new MyThread1();
        MyThread2 myThread2 = new MyThread2();
        myThread1.start();
        myThread2.start();
        TimeUnit.SECONDS.sleep(1); //确保myThread运行一段时间
        myThread1.stop(); //显示通信，直接停止线程
        stopFlag = true; //通过改变内存中某个值进行隐式通信
    }

    public static class MyThread1 extends Thread {
        @Override
        public void run() {
            while (true) {
                System.out.println("t1 hello!");
            }
        }
    }

    public static class MyThread2 extends Thread {
        @Override
        public void run() {
            while (!stopFlag) {
                System.out.println("t2 hello!");
            }
            System.out.println("out!");
        }
    }

}
