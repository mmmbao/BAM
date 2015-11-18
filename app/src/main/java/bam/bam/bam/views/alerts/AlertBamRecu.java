package bam.bam.bam.views.alerts;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import bam.bam.R;
import bam.bam.bam.controllers.enregistrements.EnregistrementReponse;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.views.adaptater.BamsRecusAdaptater;

/**
 * class alert pour la validation d'un bam
 *
 * @author Marc
 */
public class AlertBamRecu extends AlertDialog.Builder {
    

    public AlertBamRecu(Context context,String message,int icon,Bam bam,BamsRecusAdaptater brAdp) {
        super(context);

        setCancelable(false);
        setTitle(context.getString(R.string.confirmation));
        setMessage(message);

        setPositiveButton(android.R.string.yes, new EnregistrementReponse(context,bam,brAdp,icon));

        setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() { // bouton annuler
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        setIcon(icon);

        show();
    }
}
