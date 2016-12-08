package one.path.pathonetracking.trackingservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.common.collect.Lists;

import org.json.JSONArray;

import java.util.Iterator;
import java.util.List;

public class LocationsBatchUpdateService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SettingsManager settings = new SettingsManager(getApplicationContext());
        LocationDBHelper dbHelper = LocationDBHelper.getInstance(getApplicationContext());
        String path = "http://demo.path.one/api/device/" + settings.getDeviceId() + "/report";

        // are any unsent location
        List<LocationVo> unsentLocations = dbHelper.getUnsentLocations();


        if(unsentLocations.size() > 0 ){
            Runnable r = new MyThread(unsentLocations, settings, path);
            new Thread(r).start();
        }


        // send batch

        // continue loop

        stopSelf();
        return START_NOT_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyThread implements Runnable {

        List<LocationVo> unsentLocations;
        SettingsManager settings;
        String path;

        public MyThread(List<LocationVo> unsentLocations, SettingsManager settings, String path) {
            this.unsentLocations = unsentLocations;
            this.settings = settings;
            this.path = path;
        }

        public void run() {
            // get first 20
            List<List<LocationVo>> subSets = Lists.partition(unsentLocations, settings.getWifiLocationUploadBatchSize());

            Iterator listsIterator = subSets.iterator();
            while(listsIterator.hasNext()){
                List<LocationVo> locationsList = (List<LocationVo>)listsIterator.next();
                // build batch
                // JSONArray locationsJsonArray = LocationVo.buildJsonArray(locationsList);
                JSONArray locationsJsonArray = new JSONArray();
                for (LocationVo location : locationsList){
                    locationsJsonArray.put(location.getJson());
                }

                // send
                String batchId = TrackingUtils.httpPostJsonData(path,locationsJsonArray.toString());

                Log.d("LocationsBatchUpdateService.onStartCommand BATCH ID:", batchId);
            }

        }
    }


}
