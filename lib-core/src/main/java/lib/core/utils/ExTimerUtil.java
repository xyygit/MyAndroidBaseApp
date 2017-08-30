package lib.core.utils;

import android.os.Handler;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * 使用说明
 * ExTimerUtil exTimerUtil = ExTimerUtil.getInstance();
 * TimeTask timeTask = exTimerUtil.newTimer("timer1", 5000);
 * timeTask.setTimerListener(new TimeTask.TimerListener() {
 *  @Override
 *  public void onTimeProgress(long leftTime) {
 *
 *  }
 *  });
 *  timeTask.startTimer(); 启动计时
 *  timeTask.stopTimer(); 暂停计时
 *  Activity 销毁时取消计时
 *  @Override
 * protected void onDestroy() {
 *  super.onDestroy();
 *  ExTimerUtil exTimerUtil = ExTimerUtil.getInstance();
 *  exTimerUtil.cancelTask(timeTask);
 * }
 */

public class ExTimerUtil {

    private static final long frequency = 1000;

    private LinkedList<TimeTask> timeTasks = new LinkedList<TimeTask>();
    private ArrayList<String> taskNames = new ArrayList<String>();

    private boolean isRunning;

    private ExTimerUtil() {
    }

    private static class ExTimerHolder {
        private static final ExTimerUtil etu = new ExTimerUtil();
    }

    public static final ExTimerUtil getInstance() {
        return ExTimerHolder.etu;
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int stopNum = 0;
            for (int i = 0; i < timeTasks.size(); i++) {
                TimeTask timeTask = timeTasks.get(i);
                if (!timeTask.isStop) {
                    timeTask.leftTime -= frequency;
                    if (timeTask.leftTime <= 0) {
                        timeTask.setLeftTime(0);
                        timeTasks.remove(timeTask);
                        taskNames.remove(timeTask.taskName);
                    } else {
                        isRunning = true;
                        timeTask.setLeftTime(timeTask.leftTime);
                    }
                } else {
                    stopNum++;
                }
            }
            int size = timeTasks.size();
            if (size == 0 || stopNum == size) {
                isRunning = false;
                return;
            }
            if (isRunning) {
                handler.postDelayed(this, frequency);
            }
        }
    };

    public void startTimer() {
        if (!isRunning) {
            isRunning = true;
            handler.postDelayed(runnable, frequency);
        }
    }

    public final TimeTask newTimer(String taskName, long millisecond) {
        if(taskNames.contains(taskName)) {
            ExLogUtil.e("task name repeat");
            return null;
        }
        TimeTask timeTask = new TimeTask(ExTimerHolder.etu);
        timeTask.taskName = taskName;
        timeTask.leftTime = millisecond;
        timeTasks.add(timeTask);
        taskNames.add(taskName);
        return timeTask;
    }

    public final void cancelTask(TimeTask timeTask) {
        timeTasks.remove(timeTask);
        taskNames.remove(timeTask.taskName);
    }

}
