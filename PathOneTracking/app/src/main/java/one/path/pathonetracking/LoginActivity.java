package one.path.pathonetracking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Calendar;

import one.path.pathonetracking.trackingservice.DatabaseCleanService;
import one.path.pathonetracking.trackingservice.SettingsManager;

/*
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
*/

public class LoginActivity extends AppCompatActivity {


    //
    private int MULTIPLE_RECEIVE_CODE = 100;
    /*
    private int ACCESS_COARSE_LOCATION_CODE = 101;
    private int ACCESS_FINE_LOCATION_CODE = 102;
    private int INTERNET_CODE = 103;
    private int ACCESS_WIFI_STATE_CODE = 104;
    private int ACCESS_NETWORK_STATE_CODE = 105;
    */

    SettingsManager settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = new SettingsManager(getApplicationContext());

        // handle permisssions
        Log.d("BUILD VERSION", String.valueOf(Build.VERSION.SDK_INT));

        if (Build.VERSION.SDK_INT >= 23) {
            //First checking if the app is already having the permission
            if(isPermissionsAllowed()){
                //If permission is already having then showing the toast
                // Toast.makeText(LoginActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
                //Existing the method with return
                // return;
            }else {

                //If the app has not the permission then asking for the permission
                requestPermission();
            }

        }

        // do we have a device id
        /*
        int deviceId = (getApplicationContext()
                .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                .getInt(Constants.DEVICE_ID,-1);
        */
        int deviceId = settings.getDeviceId();

        if(deviceId == -1){
            // this is first run. set defauls.
            settings.setLastReportTime(new Long(0));
            settings.setMaxReportTimeframeCel(0);
            settings.setMaxReportTimeframeWifi(0);
            settings.setMinReportTimeframeCel(0);
            settings.setMinReportTimeframeWifi(0);


            /*
            SharedPreferences pref = getApplicationContext()
                    .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            */

            String strDeviceId = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            deviceId = Math.abs((new BigInteger(strDeviceId, 16)).intValue());
            settings.setDeviceId(deviceId);

            /*
            editor.putInt(Constants.DEVICE_ID, deviceId);
            editor.commit(); // commit changes
            */
        }

        // are we logged in?
        String loggedInUser = (getApplicationContext()
                .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                .getString(Constants.LOGGED_IN_USERNAME,null);





        // Do cleanup and schedule
        Intent anIntent = new Intent(this, DatabaseCleanService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,  0, anIntent, 0);
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10); // first time in 10 seconds.
        long frequency= 1000 * 60 * 5; //TEST:5 minutes // 1000 * 60 * 60 * 24 * 7; // week in milliseconds
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);



        if(loggedInUser != null){
            // we are already in.
            Intent myIntent = new Intent(LoginActivity.this, RacePickerActivity.class);
            // myIntent.putExtra("key", value); //Optional parameters
            LoginActivity.this.startActivity(myIntent);
        }


    }

    // Login
    public void doLogin(View view) {

        String registeredUserName = "A Racer";

        String user = ((EditText)findViewById(R.id.username)).getText().toString();
        if (user.matches("")) {
            Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        if (password.matches("")) {
            Toast.makeText(this, "You did not enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        /*
        String path = "http://demo.path.one/api/device/" +
                (getApplicationContext()
                        .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                        .getInt(Constants.DEVICE_ID,0) + "/setuser";
        */

        // String path = "http://demo.path.one/api/device/" + settings.getDeviceId() + "/setuser";
        String path = settings.getServerBaseUrl() + "/api/device/" + settings.getDeviceId() + "/setuser";




        // we need to exec http request on main thred.
        // Doing this on login is kind of exception
        // these 2 lines allows us to do this ugly thing.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            //instantiates httpclient to make request
            DefaultHttpClient httpclient = new DefaultHttpClient();

            //url with the post data
            HttpPost httpost = new HttpPost(path);

            //passes the results to a string builder/entity
            JSONObject json = new JSONObject();

            try {
                json.put("username", user);
                json.put("password", password);

            } catch (JSONException e) {
                Log.e("ERROR",e.getMessage());
            }

            StringEntity se = new StringEntity(json.toString());

            //sets the post request as the resulting string
            httpost.setEntity(se);
            //sets a request header so the page receving the request
            //will know what to do with it
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");

            //Handles what is returned from the page
            // ResponseHandler responseHandler = new BasicResponseHandler();
            // httpclient.execute(httpost, responseHandler);
            HttpResponse httpResponse = httpclient.execute(httpost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            JSONObject response = new JSONObject(result.toString());
            registeredUserName = ((JSONObject)response.getJSONObject("data")).getString("name");

        }catch (Exception ex){

            Log.e("*** ERROR ***", ex.getMessage());
        }

        /*


        // POST USER
        class HttpPostTask implements Runnable {
            Context theContext;
            String username;
            String password;



            HttpPostTask(Context ctx, String username, String password) {
                theContext = ctx;
                this.username = username;
                this.password = password;
            }

            public void run() {

                String path = "http://demo.path.one/api/device/" +
                        (getApplicationContext()
                                .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                                .getInt(Constants.DEVICE_ID,0) + "/setuser";

                try {
                    //instantiates httpclient to make request
                    DefaultHttpClient httpclient = new DefaultHttpClient();

                    //url with the post data
                    HttpPost httpost = new HttpPost(path);

                    //passes the results to a string builder/entity
                    JSONObject json = new JSONObject();

                    try {
                        json.put("username", username);
                        json.put("password", password);

                    } catch (JSONException e) {
                        Log.e("ERROR",e.getMessage());
                    }

                    StringEntity se = new StringEntity(json.toString());

                    //sets the post request as the resulting string
                    httpost.setEntity(se);
                    //sets a request header so the page receving the request
                    //will know what to do with it
                    httpost.setHeader("Accept", "application/json");
                    httpost.setHeader("Content-type", "application/json");

                    //Handles what is returned from the page
                    // ResponseHandler responseHandler = new BasicResponseHandler();
                    // httpclient.execute(httpost, responseHandler);
                    HttpResponse httpResponse = httpclient.execute(httpost);
                    BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

                    StringBuffer result = new StringBuffer();
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject response = new JSONObject(result.toString());


                }catch (Exception ex){

                    Log.e("*** ERROR ***", ex.getMessage());
                }
            }
        }
        Thread httpPost = new Thread(new HttpPostTask(this, user,password));
        httpPost.start();
        */


        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.LOGGED_IN_USERNAME, user);
        editor.putString(Constants.LOGGED_IN_REGISTERED_USERNAME, registeredUserName);
        editor.commit(); // commit changes


        Intent myIntent = new Intent(LoginActivity.this, RacePickerActivity.class);
        LoginActivity.this.startActivity(myIntent);
    }


    /**
     *
     * Permisssions
     *
     *
     */

    //We are calling this method to check the permission status
    private boolean isPermissionsAllowed() {
        return false;
        /*
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.MAPS_RECEIVE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, INTERNET) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED){
            return false;
        } else {
            return false;
        }
        */
        /*
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.MAPS_RECEIVE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
        */
    }

    //Requesting permission
    private void requestPermission(){

        /*
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.MAPS_RECEIVE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        */

        //And finally ask for the permission
        /* <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> */

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.MAPS_RECEIVE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE},
                MULTIPLE_RECEIVE_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == MULTIPLE_RECEIVE_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                // Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }


}
