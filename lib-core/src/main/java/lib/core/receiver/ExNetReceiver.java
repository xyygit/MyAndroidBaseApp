package lib.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import lib.core.bean.ActionListener;
import lib.core.common.ExMonitor;
import lib.core.common.ExNetStatusListener;
import lib.core.definition.MonitorCallback;
import lib.core.utils.ExAppUtil;
import lib.core.utils.ExDeviceUtil;


/**
 * Created by lightning on 17/8/24.
 */

public class ExNetReceiver extends BroadcastReceiver {

    private int isLastConnect = 0; //0初始态 1上次为连网状态 2上一次为断网状态

    private ExNetReceiver() {
    }

    private static class ExNetReceiverHolder {
        private static final ExNetReceiver eau = new ExNetReceiver();
    }

    public static final ExNetReceiver getInstance() {
        return ExNetReceiverHolder.eau;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ExDeviceUtil.isNetworkConnect()) {
            if(isLastConnect == 2) {
                ExMonitor.getInstance().fire(ExNetStatusListener.SYSTEM_NET_STATUS, new MonitorCallback() {
                    @Override
                    public void execute(ActionListener listener) {
                        ExNetStatusListener netStatusListener = (ExNetStatusListener)listener;
                        netStatusListener.onNetConnect();
                    }
                });
            }
            isLastConnect = 1;
        } else {
            if(isLastConnect == 1) {
                ExMonitor.getInstance().fire(ExNetStatusListener.SYSTEM_NET_STATUS, new MonitorCallback() {
                    @Override
                    public void execute(ActionListener listener) {
                        ExNetStatusListener netStatusListener = (ExNetStatusListener)listener;
                        netStatusListener.onNetDisConnect();
                    }
                });
            }
            isLastConnect = 2;
        }

    }

    public void register() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        ExAppUtil.getApplicationContext().registerReceiver(this, intentFilter);
    }

    public void unregisterReceiver() {
        ExAppUtil.getApplicationContext().unregisterReceiver(this);
    }

}
