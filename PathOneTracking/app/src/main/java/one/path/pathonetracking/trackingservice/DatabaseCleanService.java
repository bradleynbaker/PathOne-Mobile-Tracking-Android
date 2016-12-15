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
        // This describes what will happen when service is triggered

        LocationDBHelper dbHelper = LocationDBHelper.getInstance(context);
        Log.e("DatabaseCleanService", "Count before cleaning: " + dbHelper.countPositions());
        dbHelper.removeOldPositions();
        Log.e("DatabaseCleanService", "Count after cleaning: " + dbHelper.countPositions());

    }


}
