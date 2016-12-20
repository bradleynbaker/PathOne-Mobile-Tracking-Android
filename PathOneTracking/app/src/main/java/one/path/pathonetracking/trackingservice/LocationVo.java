package one.path.pathonetracking.trackingservice;


import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LocationVo {

    private double mLatitude, mLongitude;
    private int mLocId;
    private String mLocAddress;
    private String quality; // In android this would be the provider. From android documentation: Returns the name of the provider that generated this fix.
    private float accuracy;
    private long time;
    private float heading; // bearing
    private float speed;
    private double altitude;
    private String report_batch_id;
    private String json;

    private boolean hasLatitude;
    private boolean hasLongitude;
    private boolean hasQuality;
    private boolean hasAccuracy;
    private boolean hasHeading;
    private boolean hasSpeed;
    private boolean hasAltitude;
    private boolean hasTime;
    private boolean hasLocId;
    private boolean hasLocAddress;


    public static LocationVo fromLocation(Location mCurrentLocation){

        LocationVo locationVo = new LocationVo();

        locationVo.setmLongitude(mCurrentLocation.getLongitude());
        locationVo.setmLatitude(mCurrentLocation.getLatitude());
        locationVo.setQuality(mCurrentLocation.getProvider());
        locationVo.setHasAccuracy(mCurrentLocation.hasAccuracy());
        locationVo.setAccuracy(mCurrentLocation.getAccuracy());
        locationVo.setTime(mCurrentLocation.getTime());
        locationVo.setHasHeading(mCurrentLocation.hasBearing());
        locationVo.setHeading(mCurrentLocation.getBearing());
        locationVo.setHasSpeed(mCurrentLocation.hasSpeed());
        locationVo.setSpeed(mCurrentLocation.getSpeed());
        locationVo.setHasAltitude(mCurrentLocation.hasAltitude());
        locationVo.setAltitude(mCurrentLocation.getAltitude());

        return locationVo;

    }

    public int getmLocId() {
        return mLocId;
    }

    public void setmLocId(int mLocId) {
        this.mLocId = mLocId;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmLocAddress() {
        return mLocAddress;
    }

    public void setmLocAddress(String mLocAddress) {
        this.mLocAddress = mLocAddress;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getHeading() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading = heading;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getReport_batch_id() { return report_batch_id; }

    public void setReport_batch_id(String report_batch_id) { this.report_batch_id = report_batch_id; }

    public boolean hasLatitude() {
        return hasLatitude;
    }

    public void setHasLatitude(boolean hasLatitude) {
        this.hasLatitude = hasLatitude;
    }

    public boolean hasLongitude() {
        return hasLongitude;
    }

    public void setHasLongitude(boolean hasLongitude) {
        this.hasLongitude = hasLongitude;
    }

    public boolean hasQuality() {
        return hasQuality;
    }

    public void setHasQuality(boolean hasQuality) {
        this.hasQuality = hasQuality;
    }

    public boolean hasAccuracy() {
        return hasAccuracy;
    }

    public void setHasAccuracy(boolean hasAccuracy) {
        this.hasAccuracy = hasAccuracy;
    }

    public boolean hasHeading() {
        return hasHeading;
    }

    public void setHasHeading(boolean hasHeading) {
        this.hasHeading = hasHeading;
    }

    public boolean hasSpeed() {
        return hasSpeed;
    }

    public void setHasSpeed(boolean hasSpeed) {
        this.hasSpeed = hasSpeed;
    }

    public boolean hasAltitude() {
        return hasAltitude;
    }

    public void setHasAltitude(boolean hasAltitude) {
        this.hasAltitude = hasAltitude;
    }

    public boolean hasTime() {
        return hasTime;
    }

    public void setHasTime(boolean hasTime) {
        this.hasTime = hasTime;
    }

    public boolean hasLocId() {
        return hasLocId;
    }

    public void setHasLocId(boolean hasLocId) {
        this.hasLocId = hasLocId;
    }

    public boolean hasLocAddress() {
        return hasLocAddress;
    }

    public void setHasLocAddress(boolean hasLocAddress) {
        this.hasLocAddress = hasLocAddress;
    }

    public JSONObject buildJson(){

        JSONObject json = new JSONObject();

        try {
            json.put("quality", this.getQuality());
            json.put("time", this.getTime());
            json.put("latitude", this.getmLatitude());
            json.put("longitude", this.getmLongitude());
            if(this.hasAccuracy()) json.put("accuracy", this.getAccuracy());
            if(this.hasSpeed()) json.put("speed", this.getSpeed());
            if(this.hasAltitude()) json.put("altitude", this.getAltitude());
            if(this.hasHeading()) json.put("heading", this.getHeading());

        } catch (JSONException e) {
            return null;
        }

        return json;
    }

    public static JSONArray buildJsonArray(List<LocationVo> list){
        JSONArray array = new JSONArray();
        for (LocationVo location : list){
            array.put(location.buildJson());
        }
        return array;
    }
}
