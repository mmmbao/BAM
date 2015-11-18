package bam.bam.bam.controllers.verifications;

import android.content.Context;

import bam.bam.R;
import bam.bam.utilities.InfoToast;

/**
 * verifier les champs pour envoyer un bam
 *
 * @author Marc
 */
public class VerifChampsSendBam {

    /**
     * le context
     */
    private Context context;

    /**
     * le titre
     */
    private String titre;

    /**
     * le prix
     */
    private String prix;

    public VerifChampsSendBam(Context context, String  titre, String prix) {
        this.context = context;
        this.titre = titre;
        this.prix = prix;
    }

    /**
     * savoir si tout est ok
     *
     * @return si tout est ok
     */
    public boolean isVerif()
    {
        boolean checkTitle = true;
        boolean checkPrix = true;
        String err = "";

        if(titre.isEmpty())
        {
            err += context.getString(R.string.titre_empty);
            checkTitle = false;
        }

        if(prix.isEmpty())
        {
            if(!err.isEmpty())
                err+="\n";

            err += context.getString(R.string.prix_empty);

            checkPrix = false;
        }

        InfoToast.display(false, err,context);

        if(checkPrix && checkTitle)
            return true;

        return false;
    }
}
