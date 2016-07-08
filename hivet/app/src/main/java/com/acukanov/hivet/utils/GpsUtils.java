package com.acukanov.hivet.utils;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class GpsUtils {

    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static Location getLastKnownLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return myLocation;
    }
}
