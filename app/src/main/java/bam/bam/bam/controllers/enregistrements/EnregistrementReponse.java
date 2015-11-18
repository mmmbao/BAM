package bam.bam.bam.controllers.enregistrements;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import bam.bam.R;
import bam.bam.bam.dataBDD.BamDAO;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.dataWS.RepJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.adaptater.BamsRecusAdaptater;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Internet;
import bam.bam.utilities.Utility;

/**
 * enregistrement de la réponse
 *
 * @author Marc
 */
public class EnregistrementReponse implements DialogInterface.OnClickListener {

    /**
     * le context
     */
    private Context context;

    /**
     * le bam
     */
    private  Bam bam;

    /**
     * adaptater de bam recu
     */
    private BamsRecusAdaptater brAdp;

    /**
     * l'icon
     */
    private int icon;

    /**
     * savoir si le bam a été annulé
     */
    private List<Boolean> codeAnnul;

    public EnregistrementReponse(Context context, Bam bam, BamsRecusAdaptater brAdp, int icon) {
        this.context = context;
        this.bam = bam;
        this.brAdp = brAdp;
        this.icon = icon;

        codeAnnul = new ArrayList<>();
        codeAnnul.add(false);
    }

    public void onClick(DialogInterface dialog, int which) {

        enregistrerReponse();
    }

    /**
     * enregistrer la validaton du bam
     */
    private void enregistrerReponse() {

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

                    serveurOk = true;

                    if (bam.getTime() == -1) { // si le bam a expiré
                        InfoToast.display(true, context.getString(R.string.bamExpire), context);
                    } else {
                        if (icon == R.mipmap.ok) {
                            User user = new UserDAO(context).getUserByDevice(Utility.getPhoneId(context));
                            serveurOk = new RepJSONParser(context).setBamRep(bam.getId(), user.getId(), codeAnnul); // enregistrer la réponse
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if (serveurOk != null && serveurOk) {
                    new BamDAO(context).deleteBam(bam);
                    brAdp.removeBam(bam); // enlever le bam de la liste

                    if (codeAnnul.get(0)) {
                        InfoToast.display(true, context.getString(R.string.bamAnnul), context);
                    }

                } else {
                    if (serveurOk != null) {
                        InfoToast.display(true, context.getString(R.string.pb_serveur), context);
                    } else {
                        Internet.infoInet(true, context);
                    }
                }

            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
