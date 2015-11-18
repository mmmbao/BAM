package bam.bam.bam.controllers.refresher;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.dataBDD.BamDAO;
import bam.bam.bam.dataBDD.ReponseDAO;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.globalDisplay.database.ParametersDAO;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Utility;

/**
 * chargeur de la liste des bams envoyés
 *
 * @author Marc
 */
public class LoadDataEnvTask extends AsyncTask<Void,Void,Void>{



    /**
     * gestionnaire des bams
     */
    private BamDAO bamDAO;

    /**
     * gestionnaire des users
     */
    private UserDAO userDAO;

    /**
     * liste des bams envoyés
     */
    private List<Bam> bams;

    /**
     * l'utilisateur
     */
    private User user;

    /**
     * nombre de bams actifs
     */
    private int nbActifs = 0;

    /**
     * fragment de la liste
     */
    private BamsEnvoyesReponsesFragment fragment;

    /**
     * le context
     */
    private MainActivity activity;

    /**
     * chargeur de listes
     */
    private LoadData loadD;

    /**
     * nombre de task Réponse
     */
    private int nbTaskRep;

    /**
     * gestionnaire des réponses
     */
    private ReponseDAO reponseDAO;

    /**
     * gestionnaire des paramètres
     */
    private ParametersDAO parametersDAO;

    public LoadDataEnvTask(MainActivity activity, LoadData loadD, BamsEnvoyesReponsesFragment fragment) {
        this.activity = activity;
        this.loadD = loadD;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        LoadDataRepTask.clear();
        bams = new ArrayList<>();
        bamDAO = new BamDAO(activity);
        userDAO = new UserDAO(activity);
        parametersDAO = new ParametersDAO(activity);
        reponseDAO = new ReponseDAO(activity);
    }

    @Override
    protected Void doInBackground(Void... params) {

        user = userDAO.getUserByDevice(Utility.getPhoneId(fragment.getActivity()));

        // load BDD interne
        bams = bamDAO.getLastBamsUser(user.getId());

        // compter le nombre de bams actifs
        for (Bam bam : bams) {
            if (bam.getTime() != -1)
                nbActifs++;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        nbTaskRep = bams.size();

        for(Bam b : bams)
        {
            LoadDataRepTask loadRep = new LoadDataRepTask(activity,fragment.getLastBamRep(), b,this);
            loadRep.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        if(bams.isEmpty())
        {
            Map<Integer, Integer> nbRepsByItBam = new HashMap<>();
            fragment.loadAdpEnv(user, bams, nbActifs, nbRepsByItBam);
            loadD.endTask();
        }

        loadD.endTask();
    }

    public void endLoad()
    {
        nbTaskRep --;

        if(nbTaskRep == 0) {

            List<User> users = LoadDataRepTask.getUsers();
            List<String> newMAJ = LoadDataRepTask.getNewMAJ();
            Map<Integer, List<User>> usersByIdBam = LoadDataRepTask.getUsersByIdBam();
            Map<Integer, List<String>> datesByIdBam = LoadDataRepTask.getDatesByIdBam();
            Map<Integer, Integer> nbRepsByItBam = LoadDataRepTask.getNbRepsByItBam();

            for (Bam b : bams) { // pour chaque bam envoyé on charge les réponses

                int idBam = b.getId();
                List<User> us = usersByIdBam.get(idBam);

                if(us != null) {
                    userDAO.insertUsers(us);
                    reponseDAO.insertReponses(us, datesByIdBam.get(idBam), idBam);
                }
            }


            if(!newMAJ.isEmpty())
                parametersDAO.setLastUpdate(newMAJ.get(0),2);

            if (!fragment.isEnvVisible()) {
                fragment.loadAdpRep(users);// charger la liste des réponses
            }

            fragment.loadAdpEnv(user, bams, nbActifs, nbRepsByItBam); // charger la liste des bams envoyés

            if(!LoadDataRepTask.isServeurOk())
            {
                InfoToast.display(true, activity.getString(R.string.pb_serveur), activity);
            }

            loadD.endTask();
        }
    }
}