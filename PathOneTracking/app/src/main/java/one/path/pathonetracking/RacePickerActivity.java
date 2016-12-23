package one.path.pathonetracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import one.path.pathonetracking.trackingservice.LocationTrackingService;

public class RacePickerActivity extends AppCompatActivity {

    ListView list;
    String[] web = {
            "Baja 1000",
            "San Felipe 250",
            "Baja 500"
    } ;
    Integer[] imageId = {
            R.drawable.baja1000,
            R.drawable.logo100,
            R.drawable.baja500
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_picker);


        CustomList adapter = new
                CustomList(RacePickerActivity.this, web, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Toast.makeText(RacePickerActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                showRaceDetails(view);
            }
        });
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
