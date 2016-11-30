package one.path.pathonetracking.trackingservice;

import android.content.Context;

public class TrackingUtils {

    public static boolean shouldReport(Context context, SettingsManager settings){

        long nanosecondsTimeDifference = System.nanoTime() - settings.getLastReportTime();
        double secondsTimeDifference = (double)nanosecondsTimeDifference / 1000000000.0;

        if( Connectivity.isConnected(context) && ( (Connectivity.isConnectedMobile(context)
                && settings.getMinReportTimeframeCel() > secondsTimeDifference
                && settings.getMaxReportTimeframeCel() < secondsTimeDifference) ||
                (Connectivity.isConnectedWifi(context)
                        && settings.getMinReportTimeframeWifi() > secondsTimeDifference
                        && settings.getMaxReportTimeframeWifi() < secondsTimeDifference)) ){
            return true;
        }else {
            return false;
        }
    }



}
