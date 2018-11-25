import java.util.concurrent.*;

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
        System.out.println(futureTask.get(2, TimeUnit.SECONDS));
    }

    public static class MyFutureTask<V> implements RunnableFuture<V> {


        private Callable<V> callable;

        private volatile boolean isFinish = false;

        private volatile boolean isCancel = false;

        private volatile Exception exception;

        private volatile V result;

        private Thread runner;

        public MyFutureTask(Callable<V> callable) {
            this.callable = callable;
        }

        @Override
        public void run() {
            runner = Thread.currentThread();
            try {
                result = callable.call();
            } catch (Exception e) {
                exception = e;
            } finally {
                finish();
            }
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            try {
                if (mayInterruptIfRunning) {
                    runner.interrupt();
                }
            } finally {
                isCancel = true;
                finish();
            }
            return true;
        }

        @Override
        public boolean isCancelled() {
            return isCancel;
        }

        @Override
        public boolean isDone() {
            return isFinish;
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
            synchronized (this) {
                while (!isFinish) {
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
                while (!isFinish && remaining > 0) {
                    TimeUnit.NANOSECONDS.timedWait(this, timeout);
                    remaining = future - System.nanoTime();
                }
                if (isFinish) {
                    if (exception == null) {
                        return result;
                    } else {
                        throw new ExecutionException(exception);
                    }
                } else {
                    throw new TimeoutException("time out!");
                }
            }
        }

        private void finish() {
            isFinish = true;
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

}
