package one.path.pathonetracking.trackingservice;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    public static final String PATH_ONE_SHARED_PREFERENCES = "path_one_shared_preferences";
    public static final String DEVICE_ID = "device_id";
    // public static final String LOGGED_IN_USERNAME = "logged_in_username";

    public static final String PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL = "PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL";
    public static final String PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL = "PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL";
    public static final String PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI = "PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI";
    public static final String PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI = "PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI";
    public static final String PATH_ONE_PREFERENCE_LAST_REPORT_TIME = "PATH_ONE_PREFERENCE_LAST_REPORT_TIME";
    public static final String PATH_ONE_PREFERENCE_WIFI_LOCATION_UPLOAD_BATCH = "PATH_ONE_PREFERENCE_WIFI_LOCATION_UPLOAD_BATCH";
    public static final String UPDATE_INTERVAL_IN_MILLISECONDS = "UPDATE_INTERVAL_IN_MILLISECONDS";
    public static final String UPDATE_DISPLACEMENT_IN_METERS = "UPDATE_DISPLACEMENT_IN_METERS";
    public static final String UPLOAD_FREQUENCY = "UPLOAD_FREQUENCY";
    public static final String UPLOAD_WIFI_ONLY = "UPLOAD_WIFI_ONLY";
    public static final String PATH_ONE_SERVER_BASE_URL = "PATH_ONE_SERVER_BASE_URL";
    public static final String LOGGED_IN_USERNAME = "LOGGED_IN_USERNAME";

    public static final String PATH_ONE_PREFERENCE_POSITION = "PATH_ONE_PREFERENCE_POSITION";
    public static final String PATH_ONE_PREFERENCE_ACCURACY = "PATH_ONE_PREFERENCE_ACCURACY";
    public static final String PATH_ONE_PREFERENCE_LAST_LIVE_REPORT = "PATH_ONE_PREFERENCE_LAST_LIVE_REPORT";
    public static final String PATH_ONE_PREFERENCE_LAST_DATA_LOG_UPLOAD = "PATH_ONE_PREFERENCE_LAST_DATA_LOG_UPLOAD";
    public static final String PATH_ONE_PREFERENCE_CACHED_POSTITIONS = "PATH_ONE_PREFERENCE_CACHED_POSTITIONS";
    public static final String PATH_ONE_AVAILABLE_RACES = "PATH_ONE_AVAILABLE_RACES";
    public static final String PATH_ONE_SELECTED_RACE = "PATH_ONE_SELECTED_RACE";



    // defaults
    public static final int PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL_DEFAULT = 30;
    public static final int PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL_DEFAULT = 60;
    public static final int PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI_DEFAULT = 2;
    public static final int PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI_DEFAULT = 5;
    public static final long PATH_ONE_PREFERENCE_LAST_REPORT_TIME_DEFAULT = 0;
    public static final int PATH_ONE_PREFERENCE_WIFI_LOCATION_UPLOAD_BATCH_DEFAULT = 20;
    public static final String PATH_ONE_PREFERENCE_VALID_SSID_DEFAULT = "LINKSYS,LEVITE";
    public static final int UPDATE_INTERVAL_IN_MILLISECONDS_DEFAULT = 1000;
    public static final float UPDATE_DISPLACEMENT_IN_METERS_DEFAULT = 5;
    public static final long UPLOAD_FREQUENCY_DEFAULT = 5000;
    public static final boolean UPLOAD_WIFI_ONLY_DEFAULT = true;
    public static final String PATH_ONE_SERVER_BASE_URL_DEFAULT = "http://demo.path.one";
    public static final String LOGGED_IN_USERNAME_DEFAULT = "User";
    public static final String EMPTY_DEFAULT = "";



    private static int MODE_PRIVATE = 0;
    private Context context;
    private SharedPreferences appPreferences;

    public static final String PATH_ONE_PREFERENCE_VALID_SSID = "PATH_ONE_PREFERENCE_VALID_SSID";

    public SettingsManager(Context context){
        this.context = context;
        appPreferences = context.getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE);
    }


    public int getMinReportTimeframeCel(){
        return Integer.parseInt(context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL,
                        String.valueOf(PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL_DEFAULT)));
    }

    public int getMaxReportTimeframeCel(){
        return Integer.parseInt(context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL,
                        String.valueOf(PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL_DEFAULT)));
    }

    public int getMinReportTimeframeWifi(){
        return Integer.parseInt(context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI,
                        String.valueOf(PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI_DEFAULT)));
    }

    public int getMaxReportTimeframeWifi(){
        return Integer.parseInt(context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI,
                        String.valueOf(PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI_DEFAULT)));
    }

    public long getLastReportTime(){
        return Long.parseLong(context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_PREFERENCE_LAST_REPORT_TIME,
                        String.valueOf(PATH_ONE_PREFERENCE_LAST_REPORT_TIME_DEFAULT)));
    }

    public int getDeviceId(){
        return Integer.parseInt(context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(DEVICE_ID,String.valueOf(-1)));
    }

    public void setDeviceId(int deviceId){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(DEVICE_ID,
                String.valueOf(deviceId)).commit();
    }

    public void setMinReportTimeframeCel(int seconds){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL,
                String.valueOf(seconds)).commit();
    }

    public void setMaxReportTimeframeCel(int seconds){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL,
                String.valueOf(seconds)).commit();
    }

    public void setMinReportTimeframeWifi(int seconds){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI,
                String.valueOf(seconds)).commit();
    }

    public void setMaxReportTimeframeWifi(int seconds){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI,
                String.valueOf(seconds)).commit();
    }

    public void setLastReportTime(long seconds){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_LAST_REPORT_TIME,
                String.valueOf(seconds)).commit();
    }

    public String getSSID(){
        return (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .getString(PATH_ONE_PREFERENCE_VALID_SSID,
                        PATH_ONE_PREFERENCE_VALID_SSID_DEFAULT);
    }

    public String getServerBaseUrl(){
        return context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_SERVER_BASE_URL,
                        PATH_ONE_SERVER_BASE_URL_DEFAULT);
    }

    public String getLoggedInUsername(){
        return context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(LOGGED_IN_USERNAME,
                        LOGGED_IN_USERNAME_DEFAULT);
    }



    public void setServerBaseUrl(String aUrl){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_SERVER_BASE_URL,
                aUrl).commit();
    }

    public void setSSID(String ssid){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_VALID_SSID,
                ssid).commit();
    }

    public int getWifiLocationUploadBatchSize(){
        return Integer.valueOf((context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .getString(PATH_ONE_PREFERENCE_WIFI_LOCATION_UPLOAD_BATCH,
                        String.valueOf(PATH_ONE_PREFERENCE_WIFI_LOCATION_UPLOAD_BATCH_DEFAULT)));
    }

    public void setWifiLocationUploadBatchSize(int size){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_WIFI_LOCATION_UPLOAD_BATCH,
                String.valueOf(size)).commit();
    }

    public int getLocationServiceFixMeasureTime(){
        return Integer.valueOf((context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(UPDATE_INTERVAL_IN_MILLISECONDS,
                        String.valueOf(UPDATE_INTERVAL_IN_MILLISECONDS_DEFAULT)))) * 1000;
    }

    public float getLocationServiceFixMeasureDistance(){
        return Float.valueOf(context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(UPDATE_DISPLACEMENT_IN_METERS,
                        String.valueOf(UPDATE_DISPLACEMENT_IN_METERS_DEFAULT)));
    }

    public long getBatchUploadFrequency(){

        return Long.valueOf(context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(UPLOAD_FREQUENCY,
                        String.valueOf(UPLOAD_FREQUENCY_DEFAULT))) *1000;
    }

    public boolean getBatchUploadWifiOnly(){
        return (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .getBoolean(UPLOAD_WIFI_ONLY,
                        UPLOAD_WIFI_ONLY_DEFAULT);
    }



    public boolean ssidExists(String ssid){

        // lets make it allways work
        return true;

        /*
        String currentSSID = this.getSSID();

        if(currentSSID.contains(ssid)){
            return true;
        }else{
            return false;
        }
        */
    }

    public void setLastPostionString(String aString){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_POSITION,
                aString).commit();
    }

    public String getLastPostionString(){
        return context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_PREFERENCE_POSITION,
                        EMPTY_DEFAULT);
    }

    public void setAccuracy(String aString){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_ACCURACY,
                aString).commit();
    }

    public String getAccuracy(){
        return context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_PREFERENCE_ACCURACY,
                        EMPTY_DEFAULT);
    }

    public void setLastLiveReport(String aString){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_LAST_LIVE_REPORT,
                aString).commit();
    }

    public String getLastLiveReport(){
        return context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_PREFERENCE_LAST_LIVE_REPORT,
                        EMPTY_DEFAULT);
    }

    public void setLastDataLogUpload(String aString){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_LAST_DATA_LOG_UPLOAD,
                aString).commit();
    }

    public String getLastDataLogUpload(){
        return context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_PREFERENCE_LAST_DATA_LOG_UPLOAD,
                        EMPTY_DEFAULT);
    }

    public void setNumberCachedPositions(String aString){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_PREFERENCE_CACHED_POSTITIONS,
                aString).commit();
    }

    public String getNumberCachedPositions(){
        return context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_PREFERENCE_CACHED_POSTITIONS,
                        EMPTY_DEFAULT);
    }

    public void setAvailableRaces(String aString){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_AVAILABLE_RACES,
                aString).commit();
    }

    public String getAvailableRaces(){
        return context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_AVAILABLE_RACES,
                        EMPTY_DEFAULT);
    }

    public void setSelectedRace(String aString){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putString(PATH_ONE_SELECTED_RACE,
                aString).commit();
    }

    public String getSelectedRace(){
        return context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE)
                .getString(PATH_ONE_SELECTED_RACE,
                        EMPTY_DEFAULT);
    }
}
