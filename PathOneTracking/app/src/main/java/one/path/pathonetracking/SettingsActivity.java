package one.path.pathonetracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

import one.path.pathonetracking.trackingservice.LocationTrackingService;
import one.path.pathonetracking.trackingservice.LocationsBatchUpdateService;
import one.path.pathonetracking.trackingservice.SettingsManager;
import one.path.pathonetracking.trackingservice.TrackingUtils;

import static one.path.pathonetracking.trackingservice.SettingsManager.PATH_ONE_SHARED_PREFERENCES;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsManager settings = new SettingsManager(this);

        // Define the settings file to use by this settings fragment
        getPreferenceManager().setSharedPreferencesName(PATH_ONE_SHARED_PREFERENCES);
        addPreferencesFromResource(R.xml.appsettings);


        Preference pref = findPreference("udpate_now");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent serviceIntent = new Intent(SettingsActivity.super.getBaseContext(), LocationsBatchUpdateService.class);
                SettingsActivity.super.getBaseContext().startService(serviceIntent);
                // startService(new Intent(SettingsActivity.super.getBaseContext(), LocationsBatchUpdateService.class));
                return false;
            }
        });

        findPreference("username").setTitle("User: " + settings.getLoggedInUsername());
        findPreference("deviceid").setTitle("Device ID: " + settings.getDeviceId());
        findPreference("latlon").setTitle(settings.getLastPostionString());
        findPreference("accuracy").setTitle(settings.getAccuracy());
        findPreference("lastlivereport").setTitle(settings.getLastLiveReport());
        findPreference("lastdataloggingupload").setTitle(settings.getLastDataLogUpload());
        findPreference("numbercacheddataloggingpositions").setTitle(settings.getNumberCachedPositions());




        // Use instance field for listener
        // It will not be gc'd as long as this instance is kept referenced
        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                Log.d("Tracker Logging", "Setting changed");


                // shared prefs changed. Lets restart service if already running.
                if( TrackingUtils.isServiceRunning(LocationTrackingService.class, getApplicationContext()) ){

                    Log.d("Tracker Logging", "Setting changed. Service running Restarting Service");

                    stopService(new Intent(getApplicationContext(), LocationTrackingService.class));

                    startService(new Intent(getApplicationContext(), LocationTrackingService.class));

                }
            }
        };

        getApplicationContext().getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE).registerOnSharedPreferenceChangeListener(listener);

    }


}
