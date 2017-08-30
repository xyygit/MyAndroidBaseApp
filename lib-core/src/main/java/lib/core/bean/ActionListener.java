package lib.core.bean;

import lib.core.common.ExMonitor;
import lib.core.definition.MessageListener;

/**
 * Created by lightning on 17/4/10.
 */

public abstract class ActionListener implements Comparable, MessageListener {

    public static final String TAG = ActionListener.class.getName();

    private Integer mPriority; // 优先级
    public String tag; // 标签

    protected ActionListener() {
        this(ExMonitor.NORMAL_PRIORITY);
    }

    protected ActionListener(Integer priority) {
        this(priority, TAG);
    }

    protected ActionListener(String tag) {
        this(ExMonitor.NORMAL_PRIORITY, tag);
    }

    protected ActionListener(Integer priority, String tag) {
        this.mPriority = priority;
        this.tag = tag;
    }

    @Override
    public int compareTo(Object another) {
        if (another instanceof ActionListener) {
            ActionListener other = (ActionListener) another;
            return mPriority.compareTo(other.mPriority);
        }
        return 0;
    }
}
