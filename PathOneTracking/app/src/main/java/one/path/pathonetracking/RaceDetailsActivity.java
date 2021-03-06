package one.path.pathonetracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import one.path.pathonetracking.trackingservice.LocationTrackingService;
import one.path.pathonetracking.trackingservice.SettingsManager;
import one.path.pathonetracking.trackingservice.TrackingUtils;

public class RaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private boolean mIsServiceStarted = false;

    //activity will respond to this action String
    public static final String PATHONE_TRACKING_SERVICE_CURRENT_POSITION_JSON = "one.path.pathonetracking.PATHONE_TRACKING_SERVICE_CURRENT_POSITION_JSON";

    // local broadcast manager
    LocalBroadcastManager bManager;

    // the map
    private GoogleMap mMap;

    // the marker
    Marker now;

    // device id
    // public static int DEVICE_ID = 0;

    TextView textViewRacerName;
    TextView textViewSignal;
    TextView textViewExtraLine1;
    String currentAccuracy;
    String driver;


    SettingsManager settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_details);

        settings = new SettingsManager(getApplicationContext());

        // setup broadcast manager
        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PATHONE_TRACKING_SERVICE_CURRENT_POSITION_JSON);
        bManager.registerReceiver(bReceiver, intentFilter);

        // get the map
        // Getting reference to the SupportMapFragment of activity_main.xml
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        // Getting GoogleMap object from the fragment
        mapFragment.getMapAsync(this);

        // display device id
        /*
        String strDeviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        DEVICE_ID = Math.abs((new BigInteger(strDeviceId, 16)).intValue());
        */

        // SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0); // 0 - for private mode
        // lets clean time
        // Log.d("RaceDetailsActivity", "System.currentTimeMillis(): " + System.currentTimeMillis());
        (new SettingsManager(this)).setLastReportTime(System.currentTimeMillis());


        // EditText deviceIdText = (EditText) findViewById(R.id.deviceId);
        // deviceIdText.setText("ID " + DEVICE_ID);
        // deviceIdText.setText("ID " + (getApplicationContext().getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0)).getInt(Constants.DEVICE_ID,0));

        // start service
        if (!mIsServiceStarted) {
            mIsServiceStarted = true;
            // setButtonsEnabledState();
            // OnGoingLocationNotification(this);
            startService(new Intent(this, LocationTrackingService.class));
        }



        textViewRacerName=(TextView)findViewById(R.id.textView7);
        textViewSignal=(TextView)findViewById(R.id.textView8);
        textViewExtraLine1=(TextView)findViewById(R.id.textView9);

        // lets set some defaults
        /*
        textViewRacerName.setText("Device ID: " + String.valueOf((getApplicationContext()
                .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                .getInt(Constants.DEVICE_ID,0)));
        */
        textViewRacerName.setText("Racer: " + String.valueOf((getApplicationContext()
                .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                .getString(Constants.LOGGED_IN_REGISTERED_USERNAME,"A RACER")));

        textViewSignal.setText("Accuracy: ");
        // textViewExtraLine1.setText("Extra Information");


        // Update Scroling text
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(30000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(currentAccuracy != null){
                                    textViewSignal.setText("Accuracy: " + TrackingUtils.getAccuracyString(Float.valueOf(currentAccuracy)) );
                                }else{
                                    textViewSignal.setText("Accuracy: --");
                                }



                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
        // ~Update Scroling text


        // Set race image
        int selectedRacePosition = Integer.parseInt(settings.getSelectedRace());
        JSONArray races = null;
        String[] raceNames = new String[0];
        String[] raceImages = new String[0];

        try {
            JSONObject json = new JSONObject(settings.getAvailableRaces());


            String strRaces = json.getString("data");
            races = new JSONArray(strRaces);

            raceNames= new String[races.length()];
            raceImages= new String[races.length()];

            for (int i = 0; i < races.length(); i++) {
                raceNames[i] = races.getJSONObject(i).getString("name");
                raceImages[i] = races.getJSONObject(i).getString("logo");
            }
        }catch (Exception ex){
            Log.e("PATH_ONE", ex.getMessage(),ex);
        }

        ImageView iv = (ImageView)findViewById(R.id.imageView);
        TrackingUtils.loadImageFromURL(raceImages[selectedRacePosition],iv);

        // ~Set race image



    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        bManager.unregisterReceiver(bReceiver);
    }

    /*
    public void onToggleClicked(View view) {
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
            // start service
            if (!mIsServiceStarted) {
                mIsServiceStarted = true;
                // setButtonsEnabledState();
                // OnGoingLocationNotification(this);
                startService(new Intent(this, LocationTrackingService.class));
            }
        } else {
            // stop service
            if (mIsServiceStarted) {
                mIsServiceStarted = false;
                // setButtonsEnabledState();
                // cancelNotification(this, notifID);
                stopService(new Intent(this, LocationTrackingService.class));
            }
        }
    }
    */

    // broadcast receiver
    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(PATHONE_TRACKING_SERVICE_CURRENT_POSITION_JSON)) {
                String location = intent.getStringExtra("location");
                currentAccuracy = intent.getStringExtra(Constants.ACCURACY);
                String locationCount = intent.getStringExtra("locationCount");

                // update location on screen
                // EditText locationText = (EditText) findViewById(R.id.coords);
                // locationText.setText("Coords: " + location);

                // update record count
                // EditText locationCountText = (EditText) findViewById(R.id.editText3);
                // locationCountText.setText("Saved Locations " + locationCount);

                // update map
                String[] parts = location.split(",");
                double latitude = Double.parseDouble(parts[0].trim());
                double longitude = Double.parseDouble(parts[1].trim());
                if(now != null){ now.remove(); }
                LatLng latLng = new LatLng(latitude, longitude);
                now = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.reddot)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            }
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setTrafficEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        try {
            mMap.setMyLocationEnabled(false);
        }catch (SecurityException ex){

            HttpLogger.logDebug(String.valueOf((getApplicationContext()
                            .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                            .getInt(Constants.DEVICE_ID,0)),
                    "RaceDetailsActivity onMapReady failed with error: " +
                            ex.getMessage(),getApplicationContext());

        }

        // Add a marker in Sydney and move the camera
        /*
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("You are Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
    }

    public void requestHelp(View view) {
        Intent myIntent = new Intent(RaceDetailsActivity.this, HelpRequestActivity.class);
        // myIntent.putExtra("key", value); //Optional parameters
        RaceDetailsActivity.this.startActivity(myIntent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.race_details, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent anIntent = null;
        switch (item.getItemId()) {
            case R.id.action_logOut:

                // stop logging
                stopService(new Intent(getBaseContext(), LocationTrackingService.class));

                // remove user from settings
                (getApplicationContext()
                        .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                        .edit().remove(Constants.LOGGED_IN_USERNAME).apply();

                // forward to login page
                anIntent = new Intent(RaceDetailsActivity.this, LoginActivity.class);
                RaceDetailsActivity.this.startActivity(anIntent);
                return true;

            case R.id.action_leaveRace:

                // stop logging
                stopService(new Intent(getBaseContext(), LocationTrackingService.class));

                // forward to login page
                anIntent = new Intent(RaceDetailsActivity.this, RacePickerActivity.class);
                RaceDetailsActivity.this.startActivity(anIntent);
                return true;

            case R.id.action_showSettings:

                // forward to login page
                anIntent = new Intent(RaceDetailsActivity.this, SettingsActivity.class);
                RaceDetailsActivity.this.startActivity(anIntent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
