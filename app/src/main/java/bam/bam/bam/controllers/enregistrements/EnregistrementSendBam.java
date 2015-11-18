package bam.bam.bam.controllers.enregistrements;

import android.location.Location;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import bam.bam.R;
import bam.bam.bam.controllers.verifications.VerifChampsSendBam;
import bam.bam.bam.dataBDD.BamDAO;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.dataWS.BamJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.controllers.refresher.Refresher;
import bam.bam.globalDisplay.FragmentParams;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.globalDisplay.views.tabs.TabsLayoutManager;
import bam.bam.utilities.Clavier;
import bam.bam.utilities.GPS;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Internet;
import bam.bam.utilities.Utility;

/**
 * enregistrement de l'envoi
 *
 * @author Marc
 */
public class EnregistrementSendBam implements View.OnClickListener {

    /**
     * l'activity main
     */
    private MainActivity activity;

    /**
     * le titre
     */
    private EditText titre;

    /**
     * la description
     */
    private EditText desc;

    /**
     * le prix
     */
    private EditText prix;

    /**
     * Parser des bams
     */
    private BamJSONParser bamJSONParser;

    /**
     * LayoutManager des tabs
     */
    private TabsLayoutManager tlm;

    /**
     * savoir s'il y a un enregistrement en cours
     */
    private boolean occup = false;

    /**
     * savoir si le nombre limite de bams à été dépassé
     */
    private List<Boolean> codeLimite;

    public EnregistrementSendBam(EditText titre, EditText desc, EditText prix, MainActivity activity) {
        this.titre = titre;
        this.desc = desc;
        this.prix = prix;
        this.activity = activity;

        codeLimite = new ArrayList<>();
        codeLimite.add(false);
        bamJSONParser = new BamJSONParser(activity);
        tlm = activity.getTabsLayoutManager();
    }


    @Override
    public void onClick(View v) {
        if (!occup)
            envoyerBam();
    }

    /**
     * envoyer un bam
     */
    public void envoyerBam ()
    {
        occup = true;

        boolean ok = new VerifChampsSendBam(activity,titre.getText().toString(),prix.getText().toString()).isVerif();
        if(ok) {

            new AsyncTask<Void, Void, Void>() {

                private Bam bam;
                private Bam bamRep = null;
                private Location location;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    InfoToast.display(false, activity.getString(R.string.enregistrement),activity);
                    location = GPS.getLastBestLocation(false,activity,true);
                }

                @Override
                protected Void doInBackground(Void... params) {

                    if (location != null) {
                        if(Internet.isConnected(activity)) {

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date());
                            cal.add(Calendar.MINUTE, 20);

                            bam = new Bam(desc.getText().toString(), titre.getText().toString(), Float.parseFloat(prix.getText().toString()),1,
                                    location.getLatitude(), location.getLongitude(), Utility.dateToString(new Date()), Utility.dateToString(cal.getTime()),
                                    new UserDAO(activity).getUserByDevice(Utility.getPhoneId(activity)).getId());

                            bamRep = bamJSONParser.setBam(bam,codeLimite);

                        }
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                    if(location != null) {
                        if (bamRep == null) {
                            Internet.infoInet(false, activity);
                        } else {

                            if (bamRep.getId() != -1 || codeLimite.get(0)) {

                                if (!codeLimite.get(0)) {
                                    BamDAO bamDAO = new BamDAO(activity);
                                    bamDAO.insertBam(bamRep);


                                    Refresher.getInstance().onRefresh();

                                    tlm.setPager(1); // mettre sur la page liste envoyé

                                    FragmentParams fParams = FragmentParams.TABS;
                                    activity.loadFragment(fParams.ordinal(), true, activity.getString(fParams.getPageTitle()));
                                    Clavier.ferrmerClavier(activity); // enlever le clavier

                                    InfoToast.display(true, activity.getString(R.string.messBamEnv), activity);
                                } else {
                                    InfoToast.display(false, activity.getString(R.string.limiteBams), activity);
                                }

                            } else // pb de connexion au serveur
                            {
                                InfoToast.display(false, activity.getString(R.string.pb_serveur), activity);
                            }

                        }
                    }

                    occup = false;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            occup = false;
        }

    }
}
