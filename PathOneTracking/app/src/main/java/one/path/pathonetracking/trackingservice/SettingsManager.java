package one.path.pathonetracking.trackingservice;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    public static final String PATH_ONE_SHARED_PREFERENCES = "path_one_shared_preferences";
    public static final String DEVICE_ID = "device_id";
    public static final String LOGGED_IN_USERNAME = "logged_in_username";

    public static final String PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL = "PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL";
    public static final String PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL = "PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL";
    public static final String PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI = "PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI";
    public static final String PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI = "PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI";
    public static final String PATH_ONE_PREFERENCE_LAST_REPORT_TIME = "PATH_ONE_PREFERENCE_LAST_REPORT_TIME";

    // defaults
    public static final int PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL_DEFAULT = 30;
    public static final int PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL_DEFAULT = 60;
    public static final int PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI_DEFAULT = 2;
    public static final int PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI_DEFAULT = 5;
    public static final int PATH_ONE_PREFERENCE_LAST_REPORT_TIME_DEFAULT = 0;

    private static int MODE_PRIVATE = 0;
    private Context context;
    private SharedPreferences appPreferences;

    public SettingsManager(Context context){
        this.context = context;
        appPreferences = context.getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE);
    }


    public int getMinReportTimeframeCel(){
        return (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .getInt(PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL,
                        PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL_DEFAULT);
    }

    public int getMaxReportTimeframeCel(){
        return (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .getInt(PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL,
                        PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL_DEFAULT);
    }

    public int getMinReportTimeframeWifi(){
        return (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .getInt(PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI,
                        PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI_DEFAULT);
    }

    public int getMaxReportTimeframeWifi(){
        return (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .getInt(PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI,
                        PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI_DEFAULT);
    }

    public long getLastReportTime(){
        return (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .getLong(PATH_ONE_PREFERENCE_LAST_REPORT_TIME,
                        PATH_ONE_PREFERENCE_LAST_REPORT_TIME_DEFAULT);
    }

    public int getDeviceId(){
        return (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .getInt(DEVICE_ID,0);
    }

    public void setMinReportTimeframeCel(int seconds){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putInt(PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_CEL,
                seconds).commit();
    }

    public void setMaxReportTimeframeCel(int seconds){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putInt(PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_CEL,
                seconds).commit();
    }

    public void setMinReportTimeframeWifi(int seconds){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putInt(PATH_ONE_PREFERENCE_MIN_REPORT_TIMEFRAME_WIFI,
                seconds).commit();
    }

    public void setMaxReportTimeframeWifi(int seconds){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putInt(PATH_ONE_PREFERENCE_MAX_REPORT_TIMEFRAME_WIFI,
                seconds).commit();
    }

    public void setLastReportTime(long seconds){
        (context.getApplicationContext()
                .getSharedPreferences(PATH_ONE_SHARED_PREFERENCES, MODE_PRIVATE))
                .edit().putLong(PATH_ONE_PREFERENCE_LAST_REPORT_TIME,
                seconds).commit();
    }


}
