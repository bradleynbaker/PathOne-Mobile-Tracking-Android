package one.path.pathonetracking.trackingservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class LocationsBatchUpdateService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SettingsManager settings = new SettingsManager(getApplicationContext());
        LocationDBHelper dbHelper = LocationDBHelper.getInstance(getApplicationContext());
        String path = settings.getServerBaseUrl() + "/api/device/" + String.valueOf(settings.getDeviceId()) + "/report";

        // are any unsent location
        List<LocationVo> unsentLocations = dbHelper.getUnsentLocations();

        Log.d("unsent locations", String.valueOf(unsentLocations.size()) );

        if(unsentLocations.size() > 0 && Connectivity.isConnected(this)){

            Log.d("LocationsBatchUpdate","Will start new thread");
            Runnable r = new MyThread(unsentLocations, this, settings, path);
            new Thread(r).start();
        }


        // send batch

        // continue loop

        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        Log.d("LocationsBatchUpdate","LocationsBatchUpdateService Destroyed.");
    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyThread implements Runnable {

        List<LocationVo> unsentLocations;
        SettingsManager settings;
        String path;
        Context context;

        public MyThread(List<LocationVo> unsentLocations, Context context, SettingsManager settings, String path) {
            this.unsentLocations = unsentLocations;
            this.settings = settings;
            this.path = path;
            this.context = context;
        }

        public void run() {


            Log.d("PATH_ONE_LOG","settings.getBatchUploadWifiOnly(): " + settings.getBatchUploadWifiOnly());


            // should we run? ;)
            if (Connectivity.isConnected(context) && Connectivity.isConnectedWifi(context) == settings.getBatchUploadWifiOnly()){


                // get first 20
                List<List<LocationVo>> subSets = Lists.partition(unsentLocations, settings.getWifiLocationUploadBatchSize());
            LocationDBHelper dbHelper = LocationDBHelper.getInstance(getApplicationContext());

            Iterator listsIterator = subSets.iterator();
            while (listsIterator.hasNext()) {


                List<LocationVo> locationsList = (List<LocationVo>) listsIterator.next();
                String batchId = null;
                // build batch
                // JSONArray locationsJsonArray = LocationVo.buildJsonArray(locationsList);

                try {


                    JSONArray locationsJsonArray = new JSONArray();

                    for (LocationVo location : locationsList) {
                        locationsJsonArray.put(new JSONObject(location.getJson()));
                    }

                    // send
                    String strResp = TrackingUtils.httpPostJsonData(path, locationsJsonArray.toString(), settings.getJwtToken());


                    batchId = (String) new JSONObject(strResp).getJSONObject("data").get("bath_id");


                    settings.setLastDataLogUpload("Last Data Log Upload: " + new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(new Date()));
                } catch (JSONException e) {
                    Log.e("PATHONE_LOG:", e.getMessage(), e);
                }

                // lets update db is we have a batchid
                if (batchId != null && !batchId.isEmpty()) {
                    dbHelper.updateLocationBatchId((List<LocationVo>) locationsList, batchId);
                }

            }

        }

        }
    }


}
