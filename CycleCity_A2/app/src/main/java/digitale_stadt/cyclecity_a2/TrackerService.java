package digitale_stadt.cyclecity_a2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by alexutza_a on 29.04.2016.
 */
public class TrackerService extends Service {

    private LocationManager manager;
    private LocationListener locationListener;
    DBHelper dbhelper;

    public TrackerService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TrackerService", "TrackerService started");
        //manager = (LocationManager) AppContextProvider.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        manager = (LocationManager) AppContextProvider.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        dbhelper = new DBHelper(AppContextProvider.getAppContext());
        Log.d("TrackerService", "DBHelper wurde erzeugt");

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try
                {
                    Position pos = new Position(1,"2",location);
                    dbhelper.insertPosition(pos);
                    Log.i("TrackerService", "Datenpunkt gespeichert" + pos.getTime());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String paddressrovider) {

            }
        };

        try {
            Log.d("!?GPSTracker", "Manager RequestLocationUpdate wir angemacht mit 3 Sekunden abstand");
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000L, 0, locationListener);
            Log.i("TrackerService", "up nd running");
        }catch (SecurityException e){

        }

        return 0;
    }
}
