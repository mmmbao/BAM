package bam.bam.bam.controllers.verifications;

import android.content.Context;

import bam.bam.R;
import bam.bam.bam.dataWS.UserJSONParser;
import bam.bam.utilities.InfoToast;

/**
 * verifier les champs du profil
 *
 * @author Marc
 */
public class VerifChampsProfil {

    /**
     * le context
     */
    private Context context;

    /**
     * si on enregistre ou update
     */
    private boolean save;

    /**
     * le pseudo
     */
    private String pseudo;

    /**
     * le téléphone
     */
    private String tel;

    public VerifChampsProfil(Context context, boolean save, String pseudo, String tel) {
        this.context = context;
        this.save = save;
        this.pseudo = pseudo;
        this.tel = tel;
    }

    /**
     * savoir si tout est ok
     *
     * @return si tout est ok
     */
    public boolean isVerif()
    {

        Boolean checkPseudo = true;
        boolean checkPhone;
        String err = "";
        pseudo = pseudo.trim().replaceAll("\\s+", " ");

        UserJSONParser userJSONParser = new UserJSONParser(context);

        if(save) // test le pseudo si première connexion
        {
            if(pseudo.isEmpty())
            {
                err+= context.getString(R.string.peudo_empty);
                checkPseudo = false;
            }
            else {
                checkPseudo = userJSONParser.checkPseudo(pseudo);
            }
        }

        if(checkPseudo == null) // pb serveur
        {
            InfoToast.display(false, context.getString(R.string.pb_serveur), context);
        }
        else
        {
            if (!checkPseudo && !pseudo.isEmpty())
            {
                err += context.getString(R.string.pseudo_invalide);
            }

            if(tel.length() != 10) { // si le numéro à 10 chiffre
                checkPhone = false;

                if(!err.isEmpty())
                    err += "\n";

                err += context.getString(R.string.tel_invalide);
            }
            else
            {
                checkPhone = true;
            }



            if(checkPseudo && checkPhone) // si tout est ok
            {
                return true;
            }
            else
            {
                InfoToast.display(false, err,context);
                return false;
            }
        }

        return false;

    }
}
