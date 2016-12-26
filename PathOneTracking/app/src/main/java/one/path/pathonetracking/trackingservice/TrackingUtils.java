package one.path.pathonetracking.trackingservice;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TrackingUtils {

    public static boolean shouldReport(Context context, SettingsManager settings){

        Log.d("TrackingUtils", "System.nanoTime(): " + System.currentTimeMillis());
        Log.d("TrackingUtils", "settings.getLastReportTime(): " + settings.getLastReportTime());

        long timeDiff = System.currentTimeMillis() - settings.getLastReportTime();
        long secondsTimeDifference = timeDiff / 1000;


        Log.d("TrackingUtils", "Time difference (s): " + secondsTimeDifference);
        Log.d("TrackingUtils", "Connectivity.isConnected(context): " + Connectivity.isConnected(context));
        Log.d("TrackingUtils", "Connectivity.isConnectedMobile(context): " + Connectivity.isConnectedMobile(context));
        Log.d("TrackingUtils", "Connectivity.isConnectedWifi(context): " + Connectivity.isConnectedWifi(context));

        Log.d("TrackingUtils", "settings.getMinReportTimeframeWifi(): " + settings.getMinReportTimeframeWifi());
        Log.d("TrackingUtils", "settings.getMaxReportTimeframeWifi(): " + settings.getMaxReportTimeframeWifi());

        if( Connectivity.isConnected(context) && ( (Connectivity.isConnectedMobile(context)
                && settings.getMinReportTimeframeCel() > secondsTimeDifference
                && settings.getMaxReportTimeframeCel() < secondsTimeDifference) ||
                (Connectivity.isConnectedWifi(context)
                        && settings.getMinReportTimeframeWifi() < secondsTimeDifference
                        && settings.getMaxReportTimeframeWifi() > secondsTimeDifference)) ){

            Log.d("TrackingUtils", "Will return true");
            return true;
        }else {

            // if max elapsed time has passed. reset it. otherwise we will never post again
            if(settings.getMaxReportTimeframeWifi() < secondsTimeDifference){
                Log.d("TrackingUtils", "reset settings.setLastReportTime(System.nanoTime()): " + System.currentTimeMillis());
                settings.setLastReportTime(System.currentTimeMillis());
            }



            Log.d("TrackingUtils", "Will return false");
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

            Log.d("TrackingUtils", "will send: "+ jsonData + " to: " + path);
            // response = httpclient.execute(httpost, responseHandler);
            HttpResponse httpResponse = httpclient.execute(httpost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            response = result.toString();

            // response = EntityUtils.toString(httpResponse.getEntity());
            Log.d("TrackingUtils", "returned: "+ response);


        }catch (Exception ex){
            Log.e("*** ERROR ***", ex.getMessage(),ex);
        }

        return response;
    }

    public static boolean isServiceRunning(Class<?> serviceClass, Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        // Loop through the running services
        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                // If the service is running then return true
                return true;
            }
        }
        return false;
    }

    public static String getAccuracyString(float accuracy){

        if(accuracy < 15){
            return "GOOD";
        }

        if((15 <= accuracy)&& (accuracy <= 100)){
            return "FAIR";
        }
        if(accuracy > 100){
            return "BAD";
        }

        return "--";
    }

    public static boolean loadImageFromURL(String fileUrl,
                                    ImageView iv){
        try {

            URL myFileUrl = new URL (fileUrl);
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            iv.setImageBitmap(BitmapFactory.decodeStream(is));

            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
