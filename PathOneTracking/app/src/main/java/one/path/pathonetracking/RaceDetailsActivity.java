package one.path.pathonetracking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import one.path.pathonetracking.trackingservice.LocationTrackingService;

public class RaceDetailsActivity extends Activity{

    private boolean mIsServiceStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_details);
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
}
