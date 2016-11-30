package one.path.pathonetracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // do we have a device id
        int deviceId = (getApplicationContext()
                .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                .getInt(Constants.DEVICE_ID,-1);

        if(deviceId == -1){
            SharedPreferences pref = getApplicationContext()
                    .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            String strDeviceId = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            deviceId = Math.abs((new BigInteger(strDeviceId, 16)).intValue());

            editor.putInt(Constants.DEVICE_ID, deviceId);
            editor.commit(); // commit changes
        }

        // are we logged in?
        String loggedInUser = (getApplicationContext()
                .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                .getString(Constants.LOGGED_IN_USERNAME,null);

        if(loggedInUser != null){
            // we are already in.
            Intent myIntent = new Intent(LoginActivity.this, RacePickerActivity.class);
            // myIntent.putExtra("key", value); //Optional parameters
            LoginActivity.this.startActivity(myIntent);
        }

    }

    // Login
    public void doLogin(View view) {

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
                    ResponseHandler responseHandler = new BasicResponseHandler();
                    httpclient.execute(httpost, responseHandler);
                }catch (Exception ex){

                    Log.e("*** ERROR ***", ex.getMessage());
                }
            }
        }
        Thread httpPost = new Thread(new HttpPostTask(this, user,password));
        httpPost.start();

        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.LOGGED_IN_USERNAME, user);
        editor.commit(); // commit changes


        Intent myIntent = new Intent(LoginActivity.this, RacePickerActivity.class);
        // myIntent.putExtra("key", value); //Optional parameters
        LoginActivity.this.startActivity(myIntent);
    }
}
