package one.path.pathonetracking.trackingservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class WifiStatusBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = WifiStatusBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final SettingsManager settings = new SettingsManager(context);

        final int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);

        // Need to wait a bit for the SSID to get picked up;
        // if done immediately all we'll get is null
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())
                        && WifiManager.WIFI_STATE_ENABLED == wifiState
                        && settings.ssidExists(Connectivity.getSSID(context))) {

                    context.startService(new Intent(context, LocationsBatchUpdateService.class));
                }
            }

            }, 10000);
    }
}
