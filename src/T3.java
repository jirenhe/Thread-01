import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class T3 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        new MyThread().start();

        Runnable runnable = () -> System.out.println("runnable start!" + Thread.currentThread().getId());
        new Thread(runnable).start();

        Callable<Integer> callable = () -> {
            System.out.println("callable start!" + Thread.currentThread().getId());
            return 1;
        };
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).run();
        System.out.println("futureTask return:" + futureTask.get());

    }

    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("my thread start!" + this.getId());
        }
    }
}
