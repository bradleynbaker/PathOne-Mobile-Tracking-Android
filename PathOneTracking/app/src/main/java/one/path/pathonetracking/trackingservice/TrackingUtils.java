package one.path.pathonetracking.trackingservice;

import android.content.Context;
import android.util.Log;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

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

    public static String httpPostJsonData(String path, String jsonData){
        String response = "";

        try {
            //instantiates httpclient to make request
            DefaultHttpClient httpclient = new DefaultHttpClient();

            //url with the post data
            HttpPost httpost = new HttpPost(path);

            //passes the results to a string builder/entity
            StringEntity se = new StringEntity(jsonData);

            //sets the post request as the resulting string
            httpost.setEntity(se);
            //sets a request header so the page receving the request
            //will know what to do with it
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");

            //Handles what is returned from the page
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            Log.d("TrackingUtils httpPostJsonData", "will send: "+ jsonData);
            response = httpclient.execute(httpost, responseHandler);
            Log.d("TrackingUtils httpPostJsonData", "returned: "+ response);


        }catch (Exception ex){
            Log.e("*** ERROR ***", ex.getMessage(),ex);
        }

        return response;
    }



}
