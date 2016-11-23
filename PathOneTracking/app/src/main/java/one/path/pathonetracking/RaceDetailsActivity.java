package one.path.pathonetracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import one.path.pathonetracking.trackingservice.LocationTrackingService;

public class RaceDetailsActivity extends AppCompatActivity {

    private boolean mIsServiceStarted = false;

    //activity will respond to this action String
    public static final String PATHONE_TRACKING_SERVICE_CURRENT_POSITION_JSON = "one.path.pathonetracking.PATHONE_TRACKING_SERVICE_CURRENT_POSITION_JSON";

    // local broadcast manager
    LocalBroadcastManager bManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_details);

        // setup broadcast manager
        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PATHONE_TRACKING_SERVICE_CURRENT_POSITION_JSON);
        bManager.registerReceiver(bReceiver, intentFilter);
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

    private void updateUI(Intent intent) {
        String latitude = intent.getStringExtra("lat");
        String longitude = intent.getStringExtra("lng");


        /*
        TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
        txtDateTime.setText(time);
        txtCounter.setText(counter);

        */
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
            }
        }
    };



}
