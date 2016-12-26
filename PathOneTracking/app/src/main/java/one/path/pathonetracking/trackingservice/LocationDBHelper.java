package one.path.pathonetracking.trackingservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import one.path.pathonetracking.Constants;
import one.path.pathonetracking.HttpLogger;
import one.path.pathonetracking.trackingservice.model.Race;


public class LocationDBHelper {

    //objects
    private Context mContext;
    private static LocationDBHelper helper;


    private LocationDBHelper(Context pContxt) {
        mContext = pContxt;
    }

    public synchronized static LocationDBHelper getInstance(Context pcontxt) {
        if (helper != null) return helper;

        return helper = new LocationDBHelper(pcontxt);
    }


    /**
     * Count
     */
    public int countPositions(){
        int count = 0;
        SQLiteDatabase m_provider = null;
        try {
            m_provider = SQLiteDBProvider.getInstance(mContext).openToRead();
            m_provider.beginTransaction();
            Cursor cursor = m_provider.rawQuery(
                    "select count(*) from location_table",null);
            cursor.moveToFirst();
            count = cursor.getInt(0);
            m_provider.setTransactionSuccessful();
        }catch (Exception e) {
            HttpLogger.logDebug(String.valueOf((mContext
                            .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                            .getInt(Constants.DEVICE_ID,0)),
                    "LocationDBHelper countPositions failed with error: " +
                            e.getMessage(),mContext);
        } finally {
            if (m_provider != null)
                m_provider.endTransaction();
        }

        return count;
    }


    /**
     * Remove database records
     */
    public boolean removeOldPositions(){

        SQLiteDatabase m_provider = null;

        /*
        Delete any records that were transmitted (have a Batch Number) and are 2 or more weeks old.
        Autodelete all positions older than 2 months
         */

        long now = System.currentTimeMillis();
        long day = (1000 * 60 * 60 * 24); // 24 hours in milliseconds
        long twoWeeks = 14 * day; // two weeks
        long twoMonths = 30 * day; // two weeks
        long twoWeeksAgo = now - twoWeeks; // millis two weeks ago
        long twoMonthsAgo = now - twoMonths; // millis two months ago

        String query = "( report_batch_id is not null AND report_batch_id != '' " +
                "AND mloc_time < " + twoWeeksAgo + ") || (mloc_time < "+ twoMonthsAgo +")";

        Log.d("LocationDBHelper Query", query);

        try {
            m_provider = SQLiteDBProvider.getInstance(mContext).openToWrite();
            m_provider.beginTransaction();
            m_provider.delete(LocationMaster.getName(), query, null);
            m_provider.setTransactionSuccessful();
            return true;
        }catch (Exception e) {
            HttpLogger.logDebug(String.valueOf((mContext
                            .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                            .getInt(Constants.DEVICE_ID,0)),
                    "LocationDBHelper updateLocationBatchId failed with error: " +
                            e.getMessage(),mContext);
        } finally {
            if (m_provider != null)
                m_provider.endTransaction();
        }


        return true;
    }

    /**
     * Insert records into database.
     */
    public boolean insertLocationDetails(ArrayList<LocationVo> mContactsList) {
        if (mContactsList == null || mContactsList.size() < 0)
            return true;
        SQLiteDatabase m_provider = null;
        try {
            m_provider = SQLiteDBProvider.getInstance(mContext).openToWrite();
            m_provider.beginTransaction();

            for (final LocationVo mcontacts : mContactsList) {

                ContentValues m_conVal = new ContentValues();
                m_conVal.put(LocationMaster.mloc_latitude.name(), mcontacts.getmLatitude());
                m_conVal.put(LocationMaster.mloc_longitude.name(), mcontacts.getmLongitude());
                m_conVal.put(LocationMaster.mloc_address.name(), mcontacts.getmLocAddress());
                m_conVal.put(LocationMaster.mloc_quality.name(), mcontacts.getQuality());
                m_conVal.put(LocationMaster.mloc_accuracy.name(), mcontacts.getAccuracy());
                m_conVal.put(LocationMaster.mloc_time.name(), mcontacts.getTime());
                m_conVal.put(LocationMaster.mloc_heading.name(), mcontacts.getHeading());
                m_conVal.put(LocationMaster.mloc_speed.name(), mcontacts.getSpeed());
                m_conVal.put(LocationMaster.mloc_altitude.name(), mcontacts.getAltitude());
                m_conVal.put(LocationMaster.mloc_json_data.name(), mcontacts.buildJson().toString());

                m_provider.replace(LocationMaster.getName(), null, m_conVal);
                System.err.println("Records Inserted.");
            }

            m_provider.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            HttpLogger.logDebug(String.valueOf((mContext
                            .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                            .getInt(Constants.DEVICE_ID,0)),
                    "LocationDBHelper insertLocationDetails failed with error: " +
                            e.getMessage(),mContext);
        } finally {
            if (m_provider != null)
                m_provider.endTransaction();
        }


        new SettingsManager(mContext).setNumberCachedPositions("Number Cached DataLogging Positions: " + countPositions());

        return false;
    }


    public boolean updateLocationBatchId(List<LocationVo> locations, String batchId){
        if (locations == null || locations.size() < 0 || batchId == null || batchId.isEmpty())
            return false;

        SQLiteDatabase m_provider = null;

        StringBuffer sb = new StringBuffer();
        sb.append("mloc_id IN(-1");
        for (LocationVo location : locations){
            sb.append(",").append(location.getmLocId());
        }
        sb.append(")");

        Log.d("PATH_ONE_LOG: ", sb.toString());


        try {
            m_provider = SQLiteDBProvider.getInstance(mContext).openToWrite();
            m_provider.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(LocationMaster.report_batch_id.name(), batchId);
            m_provider.update(LocationMaster.getName(), contentValues, sb.toString(), null);
            m_provider.setTransactionSuccessful();
            return true;
        }catch (Exception e) {
            HttpLogger.logDebug(String.valueOf((mContext
                            .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                            .getInt(Constants.DEVICE_ID,0)),
                    "LocationDBHelper updateLocationBatchId failed with error: " +
                            e.getMessage(),mContext);
        } finally {
            if (m_provider != null)
                m_provider.endTransaction();
        }

        return false;
    }

    /**
     * Get all the contact list from the database.
     *
     * @return-Arraylist of contacts
     */
    public ArrayList<LocationVo> getAllLocationLatLongDetails() {

        ArrayList<LocationVo> m_arryContVo = new ArrayList<LocationVo>();
        SQLiteDatabase m_provider = SQLiteDBProvider.getInstance(mContext).openToRead();

        Cursor m_contCursor = m_provider.query(
                  LocationMaster.getName(),
                  null,
                  null,
                  null,
                  null,
                  null,
                  null, null);

        if (m_contCursor.getCount() > 0) {
            m_contCursor.moveToFirst();
            do {
                LocationVo m_conVo = new LocationVo();
                m_conVo.setmLatitude(m_contCursor.getDouble(m_contCursor
                          .getColumnIndex(LocationMaster.mloc_latitude.name())));
                m_conVo.setmLongitude(m_contCursor.getDouble(m_contCursor
                          .getColumnIndex(LocationMaster.mloc_longitude.name())));
                m_conVo.setmLocId(m_contCursor.getInt(m_contCursor
                          .getColumnIndex(LocationMaster.mloc_id.name())));

                m_conVo.setmLocAddress(m_contCursor.getString(m_contCursor
                          .getColumnIndex(LocationMaster.mloc_address.name())));

                m_arryContVo.add(m_conVo);

            } while (m_contCursor.moveToNext());
            m_contCursor.close();
        }

        return m_arryContVo;

    }

    public ArrayList<LocationVo> getUnsentLocations() {

        ArrayList<LocationVo> m_arryContVo = new ArrayList<LocationVo>();
        SQLiteDatabase m_provider = SQLiteDBProvider.getInstance(mContext).openToRead();

        /*
        *
    tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy
        * */
        Cursor m_contCursor = m_provider.query(
                LocationMaster.getName(),
                null,
                "report_batch_id IS NULL",
                null,
                null,
                null,
                null, null);

        if (m_contCursor.getCount() > 0) {
            m_contCursor.moveToFirst();
            do {
                LocationVo m_conVo = new LocationVo();


                m_conVo.setmLocId(m_contCursor.getInt(m_contCursor
                        .getColumnIndex(LocationMaster.mloc_id.name())));

                /*
                m_conVo.setmLatitude(m_contCursor.getDouble(m_contCursor
                        .getColumnIndex(LocationMaster.mloc_latitude.name())));

                m_conVo.setmLongitude(m_contCursor.getDouble(m_contCursor
                        .getColumnIndex(LocationMaster.mloc_longitude.name())));

                m_conVo.setQuality(m_contCursor.getString(m_contCursor
                        .getColumnIndex(LocationMaster.mloc_quality.name())));

                m_conVo.setAccuracy(m_contCursor.getFloat(m_contCursor
                        .getColumnIndex(LocationMaster.mloc_accuracy.name())));

                m_conVo.setTime(m_contCursor.getLong(m_contCursor
                        .getColumnIndex(LocationMaster.mloc_time.name())));

                m_conVo.setHeading(m_contCursor.getFloat(m_contCursor
                        .getColumnIndex(LocationMaster.mloc_heading.name())));

                m_conVo.setSpeed(m_contCursor.getFloat(m_contCursor
                        .getColumnIndex(LocationMaster.mloc_speed.name())));

                m_conVo.setAltitude(m_contCursor.getDouble(m_contCursor
                        .getColumnIndex(LocationMaster.mloc_altitude.name())));
                */

                m_conVo.setJson(m_contCursor.getString(m_contCursor
                        .getColumnIndex(LocationMaster.mloc_json_data.name())));

                m_arryContVo.add(m_conVo);

            } while (m_contCursor.moveToNext());
            m_contCursor.close();
        }

        return m_arryContVo;

    }


    public enum LocationMaster {
        mloc_id, mloc_latitude, mloc_longitude, mloc_address, mloc_quality,
        mloc_accuracy, mloc_time, mloc_heading, mloc_speed,mloc_altitude,
        report_batch_id, mloc_json_data;

        public static String getName() {
            return "location_table";
        }
    }

    public enum RaceMaster {
        race_id,
        race_name,
        race_image,
        race_image_url;

        public static String getName() {
            return "races_table";
        }
    }

    public ArrayList<Race> getAvailableRaces() {

        ArrayList<Race> array = new ArrayList<Race>();
        SQLiteDatabase m_provider = SQLiteDBProvider.getInstance(mContext).openToRead();

        Cursor cursor = m_provider.query(
                RaceMaster.getName(),
                null,
                null,
                null,
                null,
                null,
                null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Race race = new Race(cursor.getInt(cursor.getColumnIndex(RaceMaster.race_id.name())),
                        cursor.getString(cursor.getColumnIndex(RaceMaster.race_name.name())),
                        cursor.getString(cursor.getColumnIndex(RaceMaster.race_image_url.name())),
                        cursor.getBlob(cursor.getColumnIndex(RaceMaster.race_image.name())));


                array.add(race);

            } while (cursor.moveToNext());
            cursor.close();
        }

        return array;

    }

    public boolean insertRaces(ArrayList<Race> races) {
        if (races == null || races.size() < 0)
            return true;
        SQLiteDatabase m_provider = null;
        try {
            m_provider = SQLiteDBProvider.getInstance(mContext).openToWrite();
            m_provider.beginTransaction();

            for (final Race race : races) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(RaceMaster.race_name.name(), race.getName());
                contentValues.put(RaceMaster.race_id.name(), race.getId());
                contentValues.put(RaceMaster.race_image.name(), race.getImage());
                contentValues.put(RaceMaster.race_image_url.name(), race.getImageUrl());

                m_provider.insert(RaceMaster.getName(), null, contentValues);
                System.err.println("Records Inserted.");
            }

            m_provider.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            HttpLogger.logDebug(String.valueOf((mContext
                            .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                            .getInt(Constants.DEVICE_ID,0)),
                    "LocationDBHelper insertRaces failed with error: " +
                            e.getMessage(),mContext);
        } finally {
            if (m_provider != null)
                m_provider.endTransaction();
        }


        return false;
    }

    public boolean clearRacesTable(){

        SQLiteDatabase m_provider = null;


        try {
            m_provider = SQLiteDBProvider.getInstance(mContext).openToWrite();
            m_provider.beginTransaction();
            m_provider.execSQL("delete from "+ RaceMaster.getName());
            m_provider.setTransactionSuccessful();
            return true;
        }catch (Exception e) {
            HttpLogger.logDebug(String.valueOf((mContext
                            .getSharedPreferences(Constants.PATH_ONE_SHARED_PREFERENCES, 0))
                            .getInt(Constants.DEVICE_ID,0)),
                    "LocationDBHelper clearRacesTable failed with error: " +
                            e.getMessage(),mContext);
        } finally {
            if (m_provider != null)
                m_provider.endTransaction();
        }


        return true;
    }



}
