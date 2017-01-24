package one.path.pathonetracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import one.path.pathonetracking.trackingservice.LocationTrackingService;
import one.path.pathonetracking.trackingservice.SettingsManager;

public class RacePickerActivity extends AppCompatActivity {

    SettingsManager settings;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = new SettingsManager(getApplicationContext());



        // wont allow to go further is not logged in or not have a token
        String loggedInUser = (getApplicationContext()
                .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                .getString(Constants.LOGGED_IN_USERNAME,null);

        if(loggedInUser == null || settings.getJwtToken().isEmpty()){

            // stop logging
            stopService(new Intent(getBaseContext(), LocationTrackingService.class));

            // remove user from settings
            (getApplicationContext()
                    .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                    .edit().remove(Constants.LOGGED_IN_USERNAME).apply();

            // forward to login page
            Intent anIntent = new Intent(RacePickerActivity.this, LoginActivity.class);
            RacePickerActivity.this.startActivity(anIntent);
        }else {


            JSONArray races = null;
            String[] raceNames = new String[0];
            String[] raceImages = new String[0];

            try {
                JSONObject json = new JSONObject(settings.getAvailableRaces());


                String strRaces = json.getString("data");
                races = new JSONArray(strRaces);

                raceNames = new String[races.length()];
                raceImages = new String[races.length()];

                for (int i = 0; i < races.length(); i++) {
                    raceNames[i] = races.getJSONObject(i).getString("name");
                    raceImages[i] = races.getJSONObject(i).getString("logo");
                }
            } catch (Exception ex) {
                Log.e("PATH_ONE", ex.getMessage(), ex);
            }


            setContentView(R.layout.activity_race_picker);


            // CustomList adapter = new
            //         CustomList(RacePickerActivity.this, web, imageId);
            RaceList adapter = new
                    RaceList(RacePickerActivity.this, raceNames, raceImages);

            list = (ListView) findViewById(R.id.list);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    settings.setSelectedRace(String.valueOf(position));
                    showRaceDetails(view);
                }
            });
        }
    }


    // Login
    public void showRaceDetails(View view) {
        Intent myIntent = new Intent(RacePickerActivity.this, RaceDetailsActivity.class);
        // myIntent.putExtra("key", value); //Optional parameters
        RacePickerActivity.this.startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logOut:

                // stop logging
                stopService(new Intent(getBaseContext(), LocationTrackingService.class));

                // remove user from settings
                (getApplicationContext()
                        .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                        .edit().remove(Constants.LOGGED_IN_USERNAME).apply();

                // forward to login page
                Intent anIntent = new Intent(RacePickerActivity.this, LoginActivity.class);
                RacePickerActivity.this.startActivity(anIntent);
                return true;

            case R.id.action_showSettings:

                anIntent = new Intent(RacePickerActivity.this, SettingsActivity.class);
                RacePickerActivity.this.startActivity(anIntent);
            return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
