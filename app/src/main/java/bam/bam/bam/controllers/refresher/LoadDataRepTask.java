package bam.bam.bam.controllers.refresher;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.dataBDD.ReponseDAO;
import bam.bam.bam.dataWS.RepJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.globalDisplay.database.ParametersDAO;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Internet;

/**
 * chargeur de la liste des réponses
 *
 * @author Marc
 */
public class LoadDataRepTask extends AsyncTask<Void,Void,Void> {

    /**
     * chargeur de liste des bams envoyés
     */
    private LoadDataEnvTask loadEnv;

    /**
     * parseur pour les réponses
     */
    private RepJSONParser repJSONParser;

    /**
     * le dernier bam checké pour les reps
     */
    private Bam lastBam;


    /**
     * gestionnaire des réponses
     */
    private ReponseDAO reponseDAO;

    /**
     * gestionnaire des paramètres
     */
    private ParametersDAO parametersDAO;

    /**
     * le context
     */
    private MainActivity activity;

    /**
     * list des bams correspondant pour les réponses
     */
    private Bam bam;

    /**
     * savoir si le serveur est ok
     */
    private static boolean serveurOk;

    /**
     * liste des users
     */
    private static List<User> users;

    /**
     * nouvelle date de MAJ
     */
    private static List<String> newMAJ;

    /**
     * liste a insérer dans la BDD (users)
     */
    private static Map<Integer,List<User>> usersByIdBam;

    /**
     * liste a insérer dans la BDD (dates)
     */
    private static Map<Integer,List<String>> datesByIdBam;

    /**
     * nombre de réponses pour chaque bam
     */
    private static Map<Integer,Integer> nbRepsByItBam;

    public LoadDataRepTask(MainActivity activity, Bam lastBam,Bam bam, LoadDataEnvTask loadEnv) {
        this.activity = activity;
        this.lastBam = lastBam;
        this.bam = bam;
        this.loadEnv = loadEnv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        serveurOk = true;
        nbRepsByItBam = new HashMap<>();
        usersByIdBam = new HashMap<>();
        datesByIdBam = new HashMap<>();
        newMAJ = new ArrayList<>();
        users = new ArrayList<>();

        parametersDAO = new ParametersDAO(activity);
        reponseDAO = new ReponseDAO(activity);
        repJSONParser = new RepJSONParser(activity);

    }

    public static void clear()
    {
        serveurOk = true;
        nbRepsByItBam = new HashMap<>();
        usersByIdBam = new HashMap<>();
        datesByIdBam = new HashMap<>();
        newMAJ = new ArrayList<>();
        users = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... params) {

        if (Internet.isConnected(activity)) {

            // récupéré les bams de la bdd interne
            List<User> usersBDD = loadFromBDD();

            // récupéré les bams depuis le Web Service
            loadFromWS(usersBDD);

        }
        else // si pas internet
        {
            // load BDD interne
            List<User> usersBDD = loadFromBDD();
            if(lastBam != null && lastBam.equals(bam)) {
                users.addAll(usersBDD);
            }
            nbRepsByItBam.put(bam.getId(), usersBDD.size()); // nbReps
        }

        return null;
    }

    /**
     * charger depuis le web service
     *
     * @param usersBDD liste des réponses de la BDD
     */
    private void loadFromWS(List<User> usersBDD)
    {
        List<String> datesRep = new ArrayList<>();
        String oldMAJ = parametersDAO.getLastUpdate(2);

        // load web service
        List<User> usersParser = repJSONParser.getBamRep(bam.getId(),datesRep,oldMAJ,newMAJ);
        if (usersParser != null) {

            usersByIdBam.put(bam.getId(), new ArrayList<>(usersParser)); //users a inserer
            datesByIdBam.put(bam.getId(),new ArrayList<>(datesRep)); // dates à inserer
            nbRepsByItBam.put(bam.getId(), usersBDD.size() + usersParser.size()); // nbReps

            if(lastBam != null && lastBam.equals(bam)) {
                users.addAll(usersBDD);
                users.addAll(usersParser);
            }
        }
        else // pb de connexion au serveur
        {
            pbServeur();
        }
    }

    /**
     * si probleme de serveur
     */
    private void pbServeur()
    {
        serveurOk = false;

        // load BDD interne
        List<User> usersBDD = loadFromBDD();
        if(lastBam != null && lastBam.equals(bam)) {
            users.addAll(usersBDD);
        }
        nbRepsByItBam.put(bam.getId(), usersBDD.size()); // nbReps
    }

    /**
     * charger depuis la BDD interne
     *
     * @return liste des réponses de la BDD
     */
    private List<User> loadFromBDD()
    {
        return reponseDAO.getUsersRep(bam.getId());
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        loadEnv.endLoad();
    }

    public static Map<Integer, Integer> getNbRepsByItBam() {
        return nbRepsByItBam;
    }

    public static Map<Integer, List<String>> getDatesByIdBam() {
        return datesByIdBam;
    }


    public static Map<Integer, List<User>> getUsersByIdBam() {
        return usersByIdBam;
    }

    public static List<String> getNewMAJ() {
        return newMAJ;
    }

    public static boolean isServeurOk() {
        return serveurOk;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void setUsers(List<User> users) {
        LoadDataRepTask.users = users;
    }
}