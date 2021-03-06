package one.path.pathonetracking.trackingservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import one.path.pathonetracking.Constants;
import one.path.pathonetracking.RaceDetailsActivity;

public class LocationTrackingService extends Service implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    SettingsManager settings;

    protected static final String TAG = "LocationTrackingService";


    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    // public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    // public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
    //         UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    public static Boolean mRequestingLocationUpdates;
    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;
    public static boolean isEnded = false;
    private ArrayList<LocationVo> mLocationData;

    @Override
    public void onCreate() {
        super.onCreate();
        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.

        // LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);

        /*
        HttpLogger.logDebug(String.valueOf((getApplicationContext()
                .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                .getInt(Constants.DEVICE_ID,0)),
                "LocationTrackingService  onCreate() called.");
        */

        this.settings = new SettingsManager(this);


        // this is for December race.
        // if we are on wifi network
        // try to execute batch updates every 10 seconds

        Intent myIntent = new Intent(this, LocationsBatchUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,  0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // calendar.add(Calendar.SECOND, 10); // first time
        // long frequency= 10 * 1000; // in ms
        long frequency = settings.getBatchUploadFrequency();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        // Log.d("LOC", "Service init...");
        isEnded = false;
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        buildGoogleApiClient();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        return Service.START_REDELIVER_INTENT;
    }


    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        // Log.i(TAG, "Connection suspended==");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("PATHONE_LOG", location.getTime() + " - Lattitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());

        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();

        /*
        Toast.makeText(this, getResources().getString(R.string.location_updated_message),
                Toast.LENGTH_SHORT).show();
        */

        // lets see how many records we have stored
        // ArrayList<LocationVo> locations = LocationDBHelper.getInstance(this).getAllLocationLatLongDetails();
        // Log.i(TAG, "*** RECORD COUNT: " + locations.size());


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        // Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        // Log.i(TAG, "Building GoogleApiClient===");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        createLocationRequest();
    }

    public void setLocationData() {

        mLocationData = new ArrayList<>();
        LocationVo mLocVo = new LocationVo();
        mLocVo.setmLongitude(mCurrentLocation.getLongitude());
        mLocVo.setmLatitude(mCurrentLocation.getLatitude());
        mLocVo.setQuality(mCurrentLocation.getProvider());
        mLocVo.setHasAccuracy(mCurrentLocation.hasAccuracy());
        mLocVo.setAccuracy(mCurrentLocation.getAccuracy());
        mLocVo.setTime(mCurrentLocation.getTime());
        mLocVo.setHasHeading(mCurrentLocation.hasBearing());
        mLocVo.setHeading(mCurrentLocation.getBearing());
        mLocVo.setHasSpeed(mCurrentLocation.hasSpeed());
        mLocVo.setSpeed(mCurrentLocation.getSpeed());
        mLocVo.setHasAltitude(mCurrentLocation.hasAltitude());
        mLocVo.setAltitude(mCurrentLocation.getAltitude());
        /*
        mLocVo.setmLocAddress(Const.getCompleteAddressString(this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        */
        mLocationData.add(mLocVo);
    }

    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {
        setLocationData();

        /*
        Toast.makeText(this, "Latitude: =" + mCurrentLocation.getLatitude() + " Longitude:=" + mCurrentLocation
                .getLongitude(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Latitude:==" + mCurrentLocation.getLatitude() + "\n Longitude:==" + mCurrentLocation.getLongitude
                ());
        */

        class DBHelperTask implements Runnable {
            Context theContext;
            DBHelperTask(Context ctx) { theContext = ctx; }
                public void run() {
                    LocationDBHelper helper = LocationDBHelper.getInstance(theContext);
                    // LocationDBHelper.getInstance(ctx).insertLocationDetails(mLocationData);
                    // ArrayList<LocationVo> locations = LocationDBHelper.getInstance(ctx).getAllLocationLatLongDetails();
                    helper.insertLocationDetails(mLocationData);
                }
        }
        Thread t = new Thread(new DBHelperTask(this));
        t.start();


        // LocationDBHelper.getInstance(this).insertLocationDetails(mLocationData);


        // broadcast location
        /* String locartion  = mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude();
        Intent RTReturn = new Intent(RaceDetailsActivity.PATHONE_TRACKING_SERVICE_CURRENT_POSITION_JSON);
        RTReturn.putExtra("location", locartion);
        LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn); */

        class BroadcastLocationTask implements Runnable {
            Context theContext;
            BroadcastLocationTask(Context ctx) { theContext = ctx; }
            public void run() {
                String locartion  = mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude();
                Intent RTReturn = new Intent(RaceDetailsActivity.PATHONE_TRACKING_SERVICE_CURRENT_POSITION_JSON);
                RTReturn.putExtra("location", locartion);
                RTReturn.putExtra(Constants.ACCURACY, String.valueOf(mCurrentLocation.getAccuracy()));

                // RTReturn.putExtra("locationCount", String.valueOf((LocationDBHelper.getInstance(theContext).getAllLocationLatLongDetails()).size()));
                LocalBroadcastManager.getInstance(theContext).sendBroadcast(RTReturn);

                // Log.d("LOCATION BROADCAST DATA", LocationVo.fromLocation(mCurrentLocation).getJson().toString());
            }
        }
        Thread broadcast = new Thread(new BroadcastLocationTask(this));
        broadcast.start();

        new Thread(new HttpPostLocationTask(this, mCurrentLocation,this.settings)).start();


        /*
         *  Set last data in preferences
         */

        settings.setLastPostionString("Lat-Lon: " + mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude());
        settings.setAccuracy("Accuracy: " + mCurrentLocation.getAccuracy());



        /*
         *  ~Set last data in preferences
         */






    }


    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mGoogleApiClient.connect();
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(settings.getLocationServiceFixMeasureTime());

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(settings.getLocationServiceFixMeasureTime()/2);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setSmallestDisplacement(settings.getLocationServiceFixMeasureDistance());

    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;

            // The final argument to {@code requestLocationUpdates()} is a LocationListener
            // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
            try {
                if(mGoogleApiClient.isConnected()) LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            }catch(SecurityException ex){
                Log.e(TAG, ex.toString());
            }

            // Log.i(TAG, " startLocationUpdates===");
            isEnded = true;
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            // It is a good practice to remove location requests when the activity is in a paused or
            // stopped state. Doing so helps battery performance and is especially
            // recommended in applications that request frequent location updates.

            // Log.d(TAG, "stopLocationUpdates();==");
            // The final argument to {@code requestLocationUpdates()} is a LocationListener
            // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onDestroy() {
        Log.d("LocationTrackingService","LocationTrackingService Destroyed.");
        super.onDestroy();
        stopLocationUpdates();
    }

}
