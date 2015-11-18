package bam.bam.bam.controllers.verifications;

import android.content.Context;
import android.view.View;

import bam.bam.bam.modeles.Bam;
import bam.bam.bam.views.adaptater.BamsRecusAdaptater;
import bam.bam.bam.views.alerts.AlertBamRecu;
import bam.bam.utilities.Internet;

/**
 * verification pour la validation d'un bam
 *
 * @author Marc
 */
public class VerifValidation implements View.OnClickListener {

    /**
     * le context
     */
    private Context context;

    /**
     * le bam à vérifier
     */
    private Bam bam;

    /**
     * le text à afficher
     */
    private String text;

    /**
     * l'icon
     */
    private int icon;

    /**
     * adaptateur de la liste de bams recus
     */
    private BamsRecusAdaptater brAdp;


    public VerifValidation(Context context, Bam bam, String text, int icon, BamsRecusAdaptater brAdp) {
        this.context = context;
        this.bam = bam;
        this.text = text;
        this.icon = icon;
        this.brAdp = brAdp;
    }

    @Override
    public void onClick(View v) {
        new AlertBamRecu(context,text,icon,bam,brAdp);

    }
}
