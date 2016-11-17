package one.path.pathonetracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

}
