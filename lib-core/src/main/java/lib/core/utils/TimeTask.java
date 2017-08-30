package lib.core.utils;

public class TimeTask {

    public String taskName;

    public long leftTime;

    public boolean isStop = true;

    private TimerListener timerListener;

    private ExTimerUtil exTimerUtil;

    public TimeTask(ExTimerUtil exTimerUtil) {
        this.exTimerUtil = exTimerUtil;
    }

    public void startTimer() {
        isStop = false;
        exTimerUtil.startTimer();
    }

    public void stopTimer() {
        isStop = true;

    }

    public void setLeftTime(long leftTime) {
        this.leftTime = leftTime;
        if (timerListener != null) {
            timerListener.onTimeProgress(leftTime);
        }
    }

    public void setTimerListener(TimerListener timerListener) {
        this.timerListener = timerListener;
    }

    public interface TimerListener {
        void onTimeProgress(long leftTime);
    }

}
