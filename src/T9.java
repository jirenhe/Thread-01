import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class T9 {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        Callable<Integer> callable = () -> {
            System.out.println("callable start!");
            TimeUnit.SECONDS.sleep(5);
            return 1;
        };
        MyFutureTask<Integer> futureTask = new MyFutureTask<>(callable);

        new Thread(futureTask).start();

//        System.out.println(futureTask.get());
//        System.out.println(futureTask.get(2, TimeUnit.SECONDS));
//        futureTask.cancel(true);
//        System.out.println(futureTask.get());
    }

    public static class MyFutureTask<V> implements RunnableFuture<V> {


        private Callable<V> callable;

        private volatile String state = "NEW";

        private volatile Exception exception;

        private volatile V result;

        private AtomicReference<Thread> runner = new AtomicReference<>();

        public MyFutureTask(Callable<V> callable) {
            this.callable = callable;
        }

        @Override
        public void run() {
            if (!"NEW".equals(state) || !runner.compareAndSet(null, Thread.currentThread())) {
                return;
            }
            try {
                result = callable.call();
                state = "FINISH";
            } catch (Exception e) {
                state = "FAIL";
                exception = e;
            } finally {
                wakeupWaiters();
            }
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            try {
                Thread t;
                if (mayInterruptIfRunning && (t = runner.get()) != null) {
                    t.interrupt();
                }
            } finally {
                state = "CANCEL";
                wakeupWaiters();
            }
            return true;
        }

        @Override
        public boolean isCancelled() {
            return "CANCEL".equals(state);
        }

        @Override
        public boolean isDone() {
            return "FINISH".equals(state);
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
            synchronized (this) {
                while ("NEW".equals(state)) {
                    this.wait();
                }
                if (exception == null) {
                    return result;
                } else {
                    throw new ExecutionException(exception);
                }
            }
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            long nanos = unit.toNanos(timeout);
            long future = System.nanoTime() + nanos;
            long remaining = nanos;
            synchronized (this) {
                while ("NEW".equals(state) && remaining > 0) {
                    TimeUnit.NANOSECONDS.timedWait(this, timeout);
                    remaining = future - System.nanoTime();
                }
                if (!"NEW".equals(state)) {
                    if ("FAIL".equals(state)) {
                        return result;
                    } else {
                        throw new ExecutionException(exception);
                    }
                } else {
                    throw new TimeoutException("time out!");
                }
            }
        }

        private void wakeupWaiters() {
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

}
