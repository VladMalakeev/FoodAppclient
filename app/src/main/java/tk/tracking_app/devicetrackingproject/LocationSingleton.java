package tk.tracking_app.devicetrackingproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by asus on 04.11.2017.
 */

public class LocationSingleton implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {
    private static Context mContext;
    private static LocationSingleton instance;
    private List<MyLocationListener> listeners;
    private GoogleApiClient mGoogleApiClient;
    private Location lastLocation;

    private long interval=10000;
    private long fastestInterval=5000;
    private int priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
    public Location getLocation(LocationSingleton singleton)
    {
        return lastLocation;
    }
    public static LocationSingleton getInstance(Context context)
    {
        mContext = context;
        if (instance == null)
        {
            instance = new LocationSingleton();
        }
        return instance;
    }
    private LocationSingleton()
    {
        listeners = new ArrayList<>();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }
    public void updateLocationRequest(LocationRequest locationrequest) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.e("app_errors","Недостаточно разрешений");
            return;
        }
        interval = locationrequest.getInterval();
        fastestInterval = locationrequest.getFastestInterval();
        priority = locationrequest.getPriority();

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationrequest, this);
        Log.d("app_debug","Выполнен запрос на обновление параметров");
    }

    public void registerListener(MyLocationListener ... listeners)
    {
        Log.d("app_debug","Слушатели зарегистрированы");
        if(lastLocation != null)
        {
            for (MyLocationListener listener:listeners)
            {
                listener.updateLocation(lastLocation);
            }
        }
        this.listeners.addAll(Arrays.asList(listeners));
    }
    public void unregisterListener(MyLocationListener ... listeners)
    {
        Log.d("app_debug","Слушатели удалены");
        this.listeners.removeAll(Arrays.asList(listeners));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("app_debug","Соединение установлено");
        if (ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("app_error","Недостаточно разрешений");
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null)
        {
            for (MyLocationListener listener: listeners)
            {
                listener.updateLocation(lastLocation);
            }
        }
        else
        {
            Log.e("app_error", "Нет данных о последней локации");
        }

        LocationRequest mLastRequest = new LocationRequest();
        mLastRequest.setInterval(interval);
        mLastRequest.setFastestInterval(fastestInterval);
        mLastRequest.setPriority(priority);

        updateLocationRequest(mLastRequest);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("app_debug","Новое местоположение");
        lastLocation = location;
        if (lastLocation != null)
        {
            for(MyLocationListener listener:listeners)
            {
                listener.updateLocation(lastLocation);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("app_errors","Соединение приостановлено");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("app_errors","Соединение потеряно");
    }
}
