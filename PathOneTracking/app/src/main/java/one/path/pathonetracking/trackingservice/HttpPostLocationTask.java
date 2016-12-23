package one.path.pathonetracking.trackingservice;


import android.content.Context;
import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpPostLocationTask implements Runnable {
    Context theContext;
    Location location;
    SettingsManager settings;

    HttpPostLocationTask(Context ctx, Location location, SettingsManager settings) {
        theContext = ctx;
        this.location = location;
        this.settings = settings;
    }

    public void run() {



        // are we connected?
        if(TrackingUtils.shouldReport(theContext,settings)){

            String path = settings.getServerBaseUrl() + "/api/device/" + settings.getDeviceId() + "/report";

            TrackingUtils.httpPostJsonData(path, LocationVo.fromLocation(location).buildJson().toString());

            settings.setLastLiveReport("Last Live Report: " + new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(new Date()));


            /*
            try {
                //instantiates httpclient to make request
                DefaultHttpClient httpclient = new DefaultHttpClient();

                //url with the post data
                HttpPost httpost = new HttpPost(path);

                //passes the results to a string builder/entity
                StringEntity se = new StringEntity(LocationVo.fromLocation(location).buildJson().toString());

                //sets the post request as the resulting string
                httpost.setEntity(se);
                //sets a request header so the page receving the request
                //will know what to do with it
                httpost.setHeader("Accept", "application/json");
                httpost.setHeader("Content-type", "application/json");

                //Handles what is returned from the page
                ResponseHandler responseHandler = new BasicResponseHandler();

                Log.d("HttpPostLocationTask.run", "willl send: "+ se.toString());
                httpclient.execute(httpost, responseHandler);

                // we reported. Lets save timestamp
                settings.setLastReportTime(System.currentTimeMillis());


            }catch (Exception ex){
                Log.e("*** ERROR ***", ex.getMessage(),ex);
            }
            */

        }


    }
}
