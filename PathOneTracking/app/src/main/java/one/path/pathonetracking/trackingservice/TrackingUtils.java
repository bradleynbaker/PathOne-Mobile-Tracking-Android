package one.path.pathonetracking.trackingservice;

import android.content.Context;
import android.util.Log;

public class TrackingUtils {

    public static boolean shouldReport(Context context, SettingsManager settings){

        Log.d("TrackingUtils.shouldReport", "System.nanoTime(): " + System.currentTimeMillis());
        Log.d("TrackingUtils.shouldReport", "settings.getLastReportTime(): " + settings.getLastReportTime());

        long timeDiff = System.currentTimeMillis() - settings.getLastReportTime();
        long secondsTimeDifference = timeDiff / 1000;


        Log.d("TrackingUtils.shouldReport", "Time difference (s): " + secondsTimeDifference);
        Log.d("TrackingUtils.shouldReport", "Connectivity.isConnected(context): " + Connectivity.isConnected(context));
        Log.d("TrackingUtils.shouldReport", "Connectivity.isConnectedMobile(context): " + Connectivity.isConnectedMobile(context));
        Log.d("TrackingUtils.shouldReport", "Connectivity.isConnectedWifi(context): " + Connectivity.isConnectedWifi(context));

        Log.d("TrackingUtils.shouldReport", "settings.getMinReportTimeframeWifi(): " + settings.getMinReportTimeframeWifi());
        Log.d("TrackingUtils.shouldReport", "settings.getMaxReportTimeframeWifi(): " + settings.getMaxReportTimeframeWifi());

        if( Connectivity.isConnected(context) && ( (Connectivity.isConnectedMobile(context)
                && settings.getMinReportTimeframeCel() > secondsTimeDifference
                && settings.getMaxReportTimeframeCel() < secondsTimeDifference) ||
                (Connectivity.isConnectedWifi(context)
                        && settings.getMinReportTimeframeWifi() < secondsTimeDifference
                        && settings.getMaxReportTimeframeWifi() > secondsTimeDifference)) ){

            Log.d("TrackingUtils.shouldReport", "Will return true");
            return true;
        }else {

            // if max elapsed time has passed. reset it. otherwise we will never post again
            if(settings.getMaxReportTimeframeWifi() < secondsTimeDifference){
                Log.d("TrackingUtils.shouldReport", "reset settings.setLastReportTime(System.nanoTime()): " + System.currentTimeMillis());
                settings.setLastReportTime(System.currentTimeMillis());
            }



            Log.d("TrackingUtils.shouldReport", "Will return false");
            return false;
        }
    }



}
