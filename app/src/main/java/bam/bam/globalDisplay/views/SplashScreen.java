package bam.bam.globalDisplay.views;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import bam.bam.R;
import bam.bam.bam.dataBDD.BamDAO;
import bam.bam.bam.dataWS.BamJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.utilities.GPS;
import bam.bam.utilities.Internet;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.dataWS.UserJSONParser;
import bam.bam.bam.modeles.User;
import bam.bam.globalDisplay.database.ParametersDAO;
import bam.bam.utilities.Utility;

/**
 * slashScreen au lancement de l'application
 *
 * @author Marc
 */
public class SplashScreen extends Activity {

    /**
     * gestion des paramètres de la bdd
     */
    private ParametersDAO parametersDAO;

    /**
     * durée de l'attente
     */
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);



        // attendre 1s, sinon il y aura un écran noir avant le splashscreen
        Handler handler = new Handler();
        Runnable delay = new Runnable() {
            @Override
            public void run() {
                verifiation();
            }
        };

        handler.postDelayed(delay, 1000);


    }

    /**
     * verifier si la BDD est rempli et avant de lancer l'appli
     */
    public void verifiation()
    {
        parametersDAO = new ParametersDAO(this);

        if(!parametersDAO.isBDDFill()) // si BDD vide
        {

            if(Internet.isConnected(SplashScreen.this))
            {
                verificationUser();
            }
            else // arrêter l'appli
            {
                endApp();
            }
        }
        else
        {
            chargerApp(false);
        }
    }

    /**
     * checker si le user existe si la bdd a été vidé
     */
    public void verificationUser()
    {
        final List<Boolean> connexion = new ArrayList<>();
        connexion.add(false);

        try {

            new AsyncTask<Void, Void, User>() {

                @Override
                protected User doInBackground(Void... params) {

                    UserJSONParser userJSONParser = new UserJSONParser(SplashScreen.this);

                    return userJSONParser.getUser(Utility.getPhoneId(SplashScreen.this),true,connexion);
                }

                @Override
                protected void onPostExecute(User user) {
                    super.onPostExecute(user);

                    if(connexion.get(0))
                    {

                        if (user == null) // première inscription
                        {
                            // load la page profil (première connexion)
                            chargerApp(true);
                        }
                        else // recharger l'utilisateur
                        {
                            chargerBams(user);
                        }
                    }
                    else // arrêter l'appli
                    {
                        endApp();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e)
        {
            endApp();
        }
    }

    /**
     * charger les bams envoyés si la bdd a été vidé
     */
    public void chargerBams(final User user)
    {
        try {// charger les bams envoyés
            new AsyncTask<Void, Void, List<Bam>>() {
                @Override
                protected List<Bam> doInBackground(Void... params) {

                    BamJSONParser bamJSONParser = new BamJSONParser(SplashScreen.this);
                    List<Bam> bams = bamJSONParser.getLastBamsUser(user.getId());


                    return bams;
                }

                @Override
                protected void onPostExecute(List<Bam> bams) {
                    super.onPostExecute(bams);

                    if(bams != null) { // on a récupéré les bams
                        enregistrerBDD(user,bams);
                    }
                    else
                    {
                        endApp();
                    }


                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (Exception e)
        {
            endApp();
        }
    }

    /**
     * enregistrer dans la bdd si la bdd a été vidé
     */
    public void enregistrerBDD(User user,List<Bam> bams)
    {
        BamDAO bamDAO = new BamDAO(this);
        bamDAO.insertBams(bams);

        UserDAO userDAO = new UserDAO(this);
        userDAO.insertUser(user);
        parametersDAO.setBDDFill(true);

        chargerApp(false);
    }


    /**
     * arrêter l'application
     */
    public void endApp()
    {
        Internet.infoInet(false,this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
                System.exit(0);
            }
        }, 5*1000);
    }

    /**
     * lancer l'application
     *
     * @param first si c'est la première connexion
     */
    public void chargerApp(final boolean first)
    {
        GPS.lancerGPS(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // après l'attente, charge la première page de l'application
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("notification", false);
                b.putBoolean("first", first);
                mainIntent.putExtras(b);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
