package com.example.jun.mycapstone;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class LocationUtils {
    public static Criteria createCoarseCriteria() {
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;
    }

    public static Criteria createFineCriteria() {
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;
    }

    public static void init(Context ctx, LocationManager locMgr){
        LocationProvider low = locMgr.getProvider(locMgr.getBestProvider(createCoarseCriteria(),true));

        LocationProvider high = locMgr.getProvider(locMgr.getBestProvider(createFineCriteria(), true));

        ///try catch문 삽입해서 빨간줄 없앤 부분//
        try{
            locMgr.requestLocationUpdates(low.getName(), 10, 1,
                    new LocationListener() {
                        public void onLocationChanged(Location location) {
                        }
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }
                        public void onProviderEnabled(String s) {
                        }
                        public void onProviderDisabled(String s) {
                        }
                    });

            locMgr.requestLocationUpdates(high.getName(), 10, 1,
                    new LocationListener() {
                        public void onLocationChanged(Location location) {
                        }
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }
                        public void onProviderEnabled(String s) {
                        }
                        public void onProviderDisabled(String s) {
                        }
                    });
        }
        catch(SecurityException e){
        }
    }
}