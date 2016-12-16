package one.path.pathonetracking.trackingservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DatabaseCleanService extends IntentService {

    Context context;

    public DatabaseCleanService() {
        super("DatabaseCleanService");
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
        this.context = getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e("DatabaseCleanService", "onHandleIntent will run: " + !TrackingUtils.isServiceRunning(LocationTrackingService.class, this));

        // we do cleanup only if no location tracking running
        if(!TrackingUtils.isServiceRunning(LocationTrackingService.class, this)) {
            LocationDBHelper dbHelper = LocationDBHelper.getInstance(context);
            Log.e("DatabaseCleanService", "Count before cleaning: " + dbHelper.countPositions());
            dbHelper.removeOldPositions();
            Log.e("DatabaseCleanService", "Count after cleaning: " + dbHelper.countPositions());
        }

    }


}
