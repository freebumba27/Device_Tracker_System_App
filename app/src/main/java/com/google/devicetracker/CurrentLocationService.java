package com.google.devicetracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class CurrentLocationService extends Service {

    double nlat = 0;
    double nlng = 0;
    double glat = 0;
    double glng = 0;

    LocationManager glocManager;
    LocationListener glocListener;
    LocationManager nlocManager;
    LocationListener nlocListener;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        gettingCurrentLoc();
        return START_STICKY;
    }

    private void gettingCurrentLoc() {
        try {
            Log.w("Service_location", "Inside Location Service");

            nlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            nlocListener = new MyLocationListenerNetWork();
            nlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0,
                    0,
                    nlocListener);

            glocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            glocListener = new MyLocationListenerGPS();
            glocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    glocListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (glocManager != null) {
                glocManager.removeUpdates(glocListener);
                Log.d("ServiceForLatLng", "GPS Update Released");
            }
            if (nlocManager != null) {
                nlocManager.removeUpdates(nlocListener);
                Log.d("ServiceForLatLng", "Network Update Released");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public class MyLocationListenerNetWork implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            nlat = loc.getLatitude();
            nlng = loc.getLongitude();

            ReuseableClass.saveInPreference("nlat", nlat + "", CurrentLocationService.this);
            ReuseableClass.saveInPreference("nlng", nlng + "", CurrentLocationService.this);

            Log.d("LAT & LNG Network:", nlat + " " + nlng);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("LOG", "Network is OFF!");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("LOG", "Thanks for enabling Network !");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    public class MyLocationListenerGPS implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            glat = loc.getLatitude();
            glng = loc.getLongitude();

            Log.d("LAT & LNG GPS:", glat + " " + glng);

            ReuseableClass.saveInPreference("glat", glat + "", CurrentLocationService.this);
            ReuseableClass.saveInPreference("glng", glng + "", CurrentLocationService.this);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("LOG", "GPS is OFF!");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("LOG", "Thanks for enabling GPS !");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
