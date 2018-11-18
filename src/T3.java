public class T3 {

    public static void main(String[] args) {

        new MyThread().start();

        Runnable runnable = () -> System.out.println("runnable start!" + Thread.currentThread().getId());
        new MyThread().start();


    }

    public static class MyThread extends Thread {
        @Override
        public void run() {
            MyThread.dumpStack();
            System.out.println("my thread start()!" + this.getId());
        }
    }
}
