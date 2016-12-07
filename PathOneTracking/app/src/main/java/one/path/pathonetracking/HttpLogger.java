package one.path.pathonetracking;

import android.util.Log;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpLogger {

    public static void logDebug(String device, String message){
        (new Thread(new HttpPostTask(device, "DEBUG", message))).start();
    }

    static class HttpPostTask implements Runnable {

        String device;
        String message;
        String tag;

        HttpPostTask(String device, String tag, String message) {
            this.device = device;
            this.message = message;
            this.tag = tag;
        }

        public void run() {

            String path = "http://demo.path.one/api/device/" + this.device + "/report";

            try {
                //instantiates httpclient to make request
                DefaultHttpClient httpclient = new DefaultHttpClient();

                //url with the post data
                HttpPost httpost = new HttpPost(path);

                //passes the results to a string builder/entity
                JSONObject json = new JSONObject();

                try {
                    json.put("time", System.currentTimeMillis());
                    // json.put("latitude", "-1");
                    // json.put("longitude", "-1");
                    json.put("tag", this.tag);
                    json.put("message", this.message);


                } catch (JSONException e) {
                    Log.e("*** ERROR ***", e.getMessage(),e);
                }

                StringEntity se = new StringEntity(json.toString());

                //sets the post request as the resulting string
                httpost.setEntity(se);
                //sets a request header so the page receving the request
                //will know what to do with it
                httpost.setHeader("Accept", "application/json");
                httpost.setHeader("Content-type", "application/json");

                //Handles what is returned from the page
                ResponseHandler responseHandler = new BasicResponseHandler();
                httpclient.execute(httpost, responseHandler);
            }catch (Exception ex){
                Log.e("*** ERROR ***", ex.getMessage(),ex);
            }
        }
    }

}
