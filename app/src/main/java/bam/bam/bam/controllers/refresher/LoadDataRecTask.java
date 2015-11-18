package bam.bam.bam.controllers.refresher;

import android.location.Location;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.dataBDD.BamDAO;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.dataWS.BamJSONParser;
import bam.bam.bam.dataWS.UserJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.fragment.BamsRecusFragment;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.globalDisplay.database.ParametersDAO;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Internet;
import bam.bam.utilities.Utility;

/**
 * chargeur de la liste des bams recus
 *
 * @author Marc
 */
public class LoadDataRecTask extends AsyncTask<Void,Void,Void> {

    /**
     * parser des bams
     */
    private BamJSONParser bamJSONParser;

    /**
     * parser des users
     */
    private UserJSONParser userJSONParser;

    /**
     * liste des bams recus
     */
    private List<Bam> bams;

    /**
     * Map du bam et de son utilisateur
     */
    private Map<Bam, User> bamUsers;

    /**
     * fragment de la liste
     */
    private BamsRecusFragment fragment;

    /**
     * l'utilisateur
     */
    private User user;

    /**
     * gestionnaire des bams
     */
    private BamDAO bamDAO;

    /**
     * gestionnaire des users
     */
    private UserDAO userDAO;

    /**
     * le context
     */
    private MainActivity activity;

    /**
     * gestionnaire des paramètres
     */
    private ParametersDAO parametersDAO;

    /**
     * chargeur de listes
     */
    private LoadData loadD;

    /**
     * la localisation
     */
    private Location location;

    /**
     * savoir si le serveur est ok
     */
    private boolean serveurOk = true;

    public LoadDataRecTask(MainActivity activity, LoadData loadD, Location location, BamsRecusFragment fragment) {
        this.activity = activity;
        this.loadD = loadD;
        this.location = location;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        bams = new ArrayList<>();
        parametersDAO = new ParametersDAO(activity);
        bamJSONParser = new BamJSONParser(activity);
        userJSONParser = new UserJSONParser(activity);
        bamDAO = new BamDAO(activity);
        userDAO = new UserDAO(activity);
        bamUsers = new HashMap<>();
    }


    @Override
    protected Void doInBackground(Void... params) {

        user = userDAO.getUserByDevice(Utility.getPhoneId(fragment.getActivity()));

        if (Internet.isConnected(activity)) {

            // récupéré les bams de la bdd interne
            List<Bam> bamsBDD = loadFromBDD();

            // récupéré les bams depuis le Web Service
            loadFromWS(bamsBDD);
        }
        else // si pas internet
        {
            pbIternet(); // avertir de la non connexion

            // load BDD interne
            List<Bam> bamsBDD = loadFromBDD();
            bams.addAll(bamsBDD);
        }

        return null;
    }

    /**
     * charger depuis le web service
     *
     * @param bamsBDD liste des bams de la BDD
     */
    private void loadFromWS(List<Bam> bamsBDD)
    {
        List<String> newMAJ = new ArrayList<>();
        String oldMaj = parametersDAO.getLastUpdate(1);
        List<Bam> bamsParser = bamJSONParser.getBamsPos(location, user.getId(), oldMaj, newMAJ);
        List<User> usersParser = new ArrayList<>();

        if(bamsParser != null) {
            for (Bam b : bamsParser) {

                List<Boolean> connextion =  new ArrayList<Boolean>();
                connextion.add(false);

                User user = userJSONParser.getUser(String.valueOf(b.getBam_user_id()), false,connextion);
                usersParser.add(user);
            }

            if(bamsParser.size() == usersParser.size()) // tout est ok
            {
                bamDAO.insertBams(bamsParser);
                userDAO.insertUsers(usersParser);
                parametersDAO.setLastUpdate(newMAJ.get(0), 1);
                parametersDAO.setLastUpdate(newMAJ.get(0), 3);
                bams.addAll(bamsBDD);
                bams.addAll(bamsParser);

                for(int i=0;i<bamsParser.size();i++)
                    bamUsers.put(bamsParser.get(i),usersParser.get(i));
            }
            else // pb de connexion au serveur
            {
                bams.addAll(bamsBDD);
                serveurOk = false;
            }
        }
        else // pb de connexion au serveur
        {
            bams.addAll(bamsBDD);
            serveurOk = false;
        }
    }

    /**
     * si probleme internet
     */
    private void pbIternet()
    {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Internet.infoInet(true, activity);
            }
        });
    }

    /**
     * charger depuis la BDD interne
     *
     * @return liste des bams de la BDD
     */
    private List<Bam> loadFromBDD()
    {
        List<Bam> bamsBDD = bamDAO.getBamsPos(user.getId());
        for (Bam b : bamsBDD) {
            User user = userDAO.getUser(b.getBam_user_id());
            bamUsers.put(b,user);
        }

        return bamsBDD;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        fragment.loadAdpRec(bams, bamUsers);

        if(!serveurOk) {
            InfoToast.display(true, activity.getString(R.string.pb_serveur), activity);
        }

        loadD.endTask();
    }
}
