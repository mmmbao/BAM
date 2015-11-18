package bam.bam.bam.controllers.enregistrements;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.security.BasicPermission;

import bam.bam.R;
import bam.bam.bam.dataBDD.BamDAO;
import bam.bam.bam.dataWS.BamJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.views.adaptater.BamsEnvoyesAdaptater;
import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Internet;

/**
 * enregistrement de l'annulation
 *
 * @author Marc
 */
public class EnregistrementAnnulation implements DialogInterface.OnClickListener {

    /**
     * le context
     */
    private Context context;

    /**
     * le bam
     */
    private  Bam bam;

    /**
     * si on est dans les reponses
     */
    private boolean rep;

    /**
     * fragment de bam env / rep
     */
    private BamsEnvoyesReponsesFragment berf;

    public EnregistrementAnnulation(Context context, Bam bam, BamsEnvoyesReponsesFragment berf,boolean rep) {
        this.context = context;
        this.bam = bam;
        this.berf = berf;
        this.rep = rep;
    }

    public void onClick(DialogInterface dialog, int which) {
        enregistrerAnnulation();
    }

    /**
     * enregistrer l'annulation du bam
     */
    private void enregistrerAnnulation() {

        new AsyncTask<Void, Void, Void>() {

            private Boolean serveurOk = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                InfoToast.display(false, context.getString(R.string.enregistrement), context);
            }

            @Override
            protected Void doInBackground(Void... params) {

                if(Internet.isConnected(context)) {
                    serveurOk = new BamJSONParser(context).annulerBam(bam.getId()); // enregistrer l'annulation
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (serveurOk != null && serveurOk) {
                    new BamDAO(context).deleteBam(bam);
                    berf.getAdpEnv().annulerBam(bam); // enlever le bam de la liste
                    bam.setBam_State(2);
                    new BamDAO(context).insertBam(bam);
                    if(rep)
                        berf.frameBack();
                } else {
                    if (serveurOk != null) {
                        InfoToast.display(true, context.getString(R.string.pb_serveur), context);
                    }
                    else
                    {
                        Internet.infoInet(true,context);
                    }
                }
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
