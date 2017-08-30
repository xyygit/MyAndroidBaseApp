package lib.core.common;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import lib.core.definition.OperationTask;
import lib.core.definition.TaskCallback;

/**
 * 并行线程池-http
 */
public class ExHttpThread {

    private final Executor mExecutor = Executors.newFixedThreadPool(3, new AppThreadFactory());
    private static final Handler mHandler = new Handler(Looper.getMainLooper()); // 创建 UI 线程对象

    private ExHttpThread() {}

    private static class ThreadHolder {
        private static final ExHttpThread et = new ExHttpThread();
    }

    public static final ExHttpThread getInstance() {
        return ThreadHolder.et;
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
     * @param callback 线程任务执行完的回调处理
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
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private class AppThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("app_thread"+System.currentTimeMillis());
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }

    /**
     * Method_在主线程中执行
     *
     * @param runnable 线程
     */
    public void executeByUI(Runnable runnable) {

        mHandler.post(runnable);
    }

    /**
     * Method_在主线程中执行通过时间
     *
     * @param runnable    线程
     * @param delayMillis 时间
     */
    public void executeByUIADelay(Runnable runnable, long delayMillis) {

        mHandler.postDelayed(runnable, delayMillis);
    }

    /**
     * Method_取消从主线程运行的线程
     *
     * @param runnable 线程
     */
    public void cancelByUI(Runnable runnable) {

        mHandler.removeCallbacks(runnable);
    }

}
