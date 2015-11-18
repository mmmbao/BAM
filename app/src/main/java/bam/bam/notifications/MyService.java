package bam.bam.notifications;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.dataWS.BamJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.globalDisplay.database.ParametersDAO;
import bam.bam.utilities.GPS;
import bam.bam.utilities.Internet;
import bam.bam.utilities.Utility;


/**
 * classe qui gere les notifs
 *
 * @author Marc
 */
public class MyService extends Service {

    /**
     * le handler
     */
    private Handler handler;

    /**
     * le runnable
     */
    private Runnable runnable;

    /**
     * indique si la vérification est en cours ou non
     */
    private boolean occup = false;

    /**
     * temps entre chaque vérif des notifs
     */
    private int time = 5*1000;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        occup = false;

        //lancer le check pour les notifs
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                verifierNotification();
                handler.postDelayed(this,time);
            }


        };
        handler.postDelayed(runnable,time);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * verifie s'il y a de nouveaux bams
     */
    private void verifierNotification() {

        if(!occup) {

            occup = true;

            new AsyncTask<Void, Void, Void>() {

                private Context context;
                private Location location;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    context = getApplicationContext();
                    location = GPS.getLastBestLocation(true, context, false);
                }

                @Override
                protected Void doInBackground(Void... params) {

                    if (location != null) {
                        if (Internet.isConnected(context)) {

                            //recuperer les bams et en faire des notifs
                            User user = new UserDAO(context).getUserByDevice(Utility.getPhoneId(context));
                            if (user != null) { // si l'utilisateur est déjà inscrit

                                List<String> newMAJ = new ArrayList<>();
                                String oldMaj = new ParametersDAO(context).getLastUpdate(3);
                                List<Bam> bamsParser = new BamJSONParser(context).getBamsPos(location, user.getId(), oldMaj, newMAJ);

                                if (bamsParser != null) {
                                    new ParametersDAO(context).setLastUpdate(newMAJ.get(0), 3);
                                    for (Bam bam : bamsParser) {
                                        new NotificationRecus(context, bam.getBam_title(), bam.getBam_price()).createNotification();
                                    }
                                }
                            }
                        }
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    occup = false;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void onDestroy() {

        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
