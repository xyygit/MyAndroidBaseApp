package lib.core.common;

import lib.core.bean.ActionListener;

/**
 * Created by lightning on 17/8/25.
 */

public class ExNetStatusListener extends ActionListener {

    public static final String SYSTEM_NET_STATUS = "SYSTEM_NET_STATUS";

    public ExNetStatusListener(String tag) {
        super(tag);
    }

    @Override
    public boolean isInterrupt() {
        return false;
    }

    @Override
    public void onMessage(Object msg) {

    }

    public void onNetConnect() {

    }

    public void onNetDisConnect() {

    }

}
