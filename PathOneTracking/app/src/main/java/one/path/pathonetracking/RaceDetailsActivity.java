package one.path.pathonetracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import one.path.pathonetracking.trackingservice.LocationTrackingService;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_details);

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

    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        bManager.unregisterReceiver(bReceiver);
    }


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

    // broadcast receiver
    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(PATHONE_TRACKING_SERVICE_CURRENT_POSITION_JSON)) {
                String location = intent.getStringExtra("location");

                // update location on screen
                EditText locationText = (EditText) findViewById(R.id.coords);
                locationText.setText("Coords: " + location);

                // update map
                String[] parts = location.split(",");
                double latitude = Double.parseDouble(parts[0].trim());
                double longitude = Double.parseDouble(parts[1].trim());
                if(now != null){ now.remove(); }
                LatLng latLng = new LatLng(latitude, longitude);
                now = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setTrafficEnabled(false);
        try {
            mMap.setMyLocationEnabled(false);
        }catch (SecurityException ex){

        }

        // Add a marker in Sydney and move the camera
        /*
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("You are Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
    }
}
