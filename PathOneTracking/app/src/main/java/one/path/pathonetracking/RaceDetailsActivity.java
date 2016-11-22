package one.path.pathonetracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    MapView mapView;
    GoogleMap map;
    LocationManager locationManager;
    LatLng myPosition;
    private boolean zoomed = false;
    boolean firstPass = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_details);

        // Getting reference to the SupportMapFragment of activity_main.xml
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        // Getting GoogleMap object from the fragment
        mapFragment.getMapAsync(this);
        int a = 1;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean locationEnabled = prefs.getBoolean("location_enabled_switch", false);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        // Enabling MyLocation Layer of Google Map
        map.setMyLocationEnabled(true);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            // Getting latitude of the current location
            double latitude = location.getLatitude();

            // Getting longitude of the current location
            double longitude = location.getLongitude();

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            myPosition = new LatLng(latitude, longitude);

            map.addMarker(new MarkerOptions().position(myPosition).title("Start"));
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            // ---Get current location latitude, longitude---

            Log.d("LOCATION CHANGED", location.getLatitude() + "");
            Log.d("LOCATION CHANGED", location.getLongitude() + "");
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            Marker currentLocationMarker = map.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
            // Move the camera instantly to hamburg with a zoom of 15.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
            // Zoom in, animating the camera.
            if (!zoomed) {
                map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
                zoomed = true;
            }
            if (!firstPass){
                currentLocationMarker.remove();
            }
            firstPass = false;
            Toast.makeText(RaceDetailsActivity.this,"Latitude = "+
                            location.getLatitude() + "" +"Longitude = "+ location.getLongitude(),
                    Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    /** BEGIN MENU **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_showSettings) {
            Intent settingsIntent=new  Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        /*else if(id==R.id.nav_gallery){ // }else if(id==R.id.action_logout){
            finish();
        }*/


        // return true;
        return super.onOptionsItemSelected(item);
    }
    /** END MENU **/
}
