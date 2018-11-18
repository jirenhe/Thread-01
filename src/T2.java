import java.util.concurrent.TimeUnit;

public class T2 {
    private static boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        new MyThread().start();
        TimeUnit.SECONDS.sleep(1);
        flag = false;
    }

    private static class MyThread extends Thread {
        @Override
        public void run() {
            while (flag) ;//线程可能永远都不会退出
        }
    }
}