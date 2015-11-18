package bam.bam.bam.views.alerts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import bam.bam.R;
import bam.bam.bam.controllers.enregistrements.EnregistrementAnnulation;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.views.adaptater.BamsEnvoyesAdaptater;
import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;

/**
 * alert pour annuler un bam
 *
 * @author Marc
 */
public class AlertBamEnvSuppr extends AlertDialog.Builder {

    public AlertBamEnvSuppr(Context context, Bam bam, BamsEnvoyesReponsesFragment berf,boolean rep) {
        super(context);

        setCancelable(false);
        setTitle(context.getString(R.string.annuler));
        setMessage(R.string.annulerBam);

        setPositiveButton(android.R.string.yes, new EnregistrementAnnulation(context,bam,berf,rep));

        setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() { // bouton annuler
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        show();
    }
}
