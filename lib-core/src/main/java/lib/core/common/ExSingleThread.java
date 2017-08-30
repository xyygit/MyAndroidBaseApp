package lib.core.common;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import lib.core.definition.OperationTask;
import lib.core.definition.TaskCallback;

/**
 * 串行线程池
 */
public class ExSingleThread {

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor(new AppThreadFactory());
    private static final Handler mHandler = new Handler(Looper.getMainLooper()); // 创建 UI 线程对象

    private ExSingleThread() {}

    private static class ThreadHolder {
        private static final ExSingleThread est = new ExSingleThread();
    }

    public static final ExSingleThread getInstance() {
        return ThreadHolder.est;
    }

    public void execute(Runnable runnable) {
        mExecutor.execute(runnable);
    }

    public void submit(Callable task) {
        mExecutor.submit(task);
    }

    /**
     * Method_线程执行
     * @param task 要在线程中执行的任务
     */
    public void execute(final OperationTask task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    task.execute();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Method_线程执行并回调
     * @param task 要在线程中执行的任务
     */
    public void execute(final OperationTask task, final TaskCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Object obj = task.execute();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.callback(obj);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class AppThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("app_thread"+System.currentTimeMillis());
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        }
    }
}
