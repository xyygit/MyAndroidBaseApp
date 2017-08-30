package lib.core.common;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import lib.core.bean.ActionListener;
import lib.core.definition.MonitorCallback;
import lib.core.utils.ExAppUtil;
import lib.core.utils.ExCommonUtil;

/**
 * Created by lightning on 17/4/10.
 */

public class ExMonitor {

    public static final int HIGHT_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 2;
    public static final int LOW_PRIORITY = 3;

    private HashMap<String, ArrayList<ActionListener>> mListeners;

    private ExMonitor() {
        mListeners = new HashMap<>();
    }

    private static class MonitorHolder {
        private static final ExMonitor mgr = new ExMonitor();
    }

    public static ExMonitor getInstance() {
        return MonitorHolder.mgr;
    }

    public void add(ActionListener listener) {

        if (listener == null) {
            return;
        }

        ArrayList<ActionListener> listeners;

        if(mListeners.containsKey(listener.tag)) {
            listeners = mListeners.get(listener.tag);
        } else {
            listeners = new ArrayList<>();
            mListeners.put(listener.tag, listeners);
        }
        listeners.add(listener);

        if(mListeners.size() > 1) {
            Collections.sort(listeners);
        }

    }

    /**
     * Method_移除监听
     *
     * @param tag 标签
     */
    public void remove(String tag) {
        if (mListeners.containsKey(tag)) {
            mListeners.remove(tag);
        }
    }

    /**
     * Method_移除监听
     *
     * @param listener 监听器
     */
    public void remove(ActionListener listener) {

        if (listener == null) {
            return;
        }

        for (ArrayList<ActionListener> listeners : mListeners.values()) {
            if(listeners.contains(listener)) {
                listeners.remove(listener);
                break;
            }
        }
    }

    /**
     * Method_发送消息
     *
     * @param callback 回调函数
     */
    public void fire(MonitorCallback callback) {
        fire(ActionListener.TAG, callback);
    }

    /**
     * Method_发送消息
     *
     * @param tag      标签
     * @param callback 回调函数
     */
    public void fire(String tag, final MonitorCallback callback) {

        if (ExCommonUtil.isEmpty(tag)) {
            tag = ActionListener.TAG;
        }

        ArrayList<ActionListener> listeners = mListeners.get(tag);
        if(ExCommonUtil.isEmpty(listeners)) return;

        for (final ActionListener listener : listeners) {
            if (!ExAppUtil.getInstance().isMainThread()) {
                ExThread.getInstance().executeByUI(new Runnable() {
                    @Override
                    public void run() {
                        callback.execute(listener);
                    }
                });
            } else {
                callback.execute(listener);
            }
            if (listener.isInterrupt()) {
                break;
            }
        }
    }

    public void clear() {
        mListeners.clear();
    }

}
