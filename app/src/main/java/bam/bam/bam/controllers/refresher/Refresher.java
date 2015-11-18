package bam.bam.bam.controllers.refresher;


import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import bam.bam.utilities.GPS;
import bam.bam.bam.dataBDD.BamDAO;
import bam.bam.bam.dataBDD.ReponseDAO;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.globalDisplay.views.MainActivity;

/**
 * le refresher
 *
 * @author Marc
 */
public class Refresher implements SwipeRefreshLayout.OnRefreshListener{

    /**
     * l'activity de l'appli
     */
    private MainActivity activity;

    /**
     * liste des refresher des listes
     */
    private List<SwipeRefreshLayout> swRLs;

    /**
     * savoir s'il y a un chargement
     */
    private boolean onLoad = false;

    /**
     * l'instance de la classe
     */
    private static Refresher INSTANCE;

    /**
     * si tout est OK
     */
    private boolean allOK = false;

    private Refresher() {
        swRLs = new ArrayList<>();
    }


    /**
     * obtenir une instance
     *
     * @return instance
     */
    public static Refresher getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new Refresher();

        return INSTANCE;
    }

    @Override
    public void onRefresh() {

        if(!onLoad && allOK) {
            onLoad = true;

            DateFormat df = DateFormat.getTimeInstance();
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            String gmtTime = df.format(new Date());

            clearBDD();

            for (SwipeRefreshLayout swRL : swRLs) {
                swRL.setRefreshing(true);
            }

            Location location = GPS.getLastBestLocation(true,activity,true);

            if (location != null) {

                LoadData loadD = new LoadData(activity,location);
                loadD.loadList();

            }
            else
            {
                endLoad();
            }
        }
    }

    /**
     * finir le chargement
     */
    public void endLoad()
    {
        for(SwipeRefreshLayout swRL : swRLs) {
            swRL.setRefreshing(false);
            onLoad = false;
        }
    }

    /**
     * mettre le context
     *
     * @param activity l'activity
     */
    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    /**
     * theme du rond de chargement
     *
     * @param swRL le refresher de la liste
     */
    public void addswRL(SwipeRefreshLayout swRL)
    {
        swRL.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swRLs.add(swRL);
    }

    /**
     * le premier chargement des listes
     */
    public void firstLoad()
    {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                allOK = true;
                onRefresh();
            }
        }, 1000);
    }


    /**
     * nettoyer la BDD
     */
    private void clearBDD() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                BamDAO bamDAO = new BamDAO(activity);
                bamDAO.clear();
                ReponseDAO reponseDAO = new ReponseDAO(activity);
                reponseDAO.clear();
                UserDAO userDAO = new UserDAO(activity);
                userDAO.clear();
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}