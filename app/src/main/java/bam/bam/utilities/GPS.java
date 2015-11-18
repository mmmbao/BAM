package bam.bam.utilities;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import bam.bam.R;

/**
 * classe du GPS
 *
 * @author Marc
 */
public class GPS {

    /**
     * localisation
     */
    private static Location locationGPS;

    /**
     * listener pour le gps
     */
    private static MyLocationListener locationListener;

    public static void lancerGPS(Context context)
    {
        LocationManager manager = (LocationManager) context.getSystemService(Activity.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    /**
     * obtenir la localisation
     *
     * @param list si on est sur les tabs
     * @param context le context
     * @return la localisation
     */
    public static Location getLastBestLocation(boolean list,Context context,boolean toast) {

        if(isGPSActivated(context)) {

            if(locationGPS == null && toast)
            {
                InfoToast.display(list, context.getString(R.string.waitGPS),context);
            }

            return locationGPS;
        }
        else
        {
            if(toast)
                infoGPS(list,context);
            return null;
        }
    }

    /**
     * savoir si le GPS est activé
     * @param context le context
     * @return si le GPS est activé
     */
    public static boolean isGPSActivated(Context context)
    {
        LocationManager manager = (LocationManager) context.getSystemService(Activity.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * message d'erreur du GPS
     *
     * @param list si on est sur es tabs
     * @param context le context
     */
    public static void infoGPS(boolean list, Context context)
    {
        InfoToast.display(list, context.getString(R.string.warnGPS),context);
    }

    /**
     * classe pour la localisation
     */
    private static class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            locationGPS = location;

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
