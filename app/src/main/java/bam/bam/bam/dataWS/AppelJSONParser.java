package bam.bam.bam.dataWS;


import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bam.bam.R;
import bam.bam.utilities.Utility;

/**
 * paseur appels
 *
 * @author Marc
 */
public class AppelJSONParser {

    /**
     * URL pour creer un appel
     */
    private String URL_POST_USER = R.string.URL + "calls";

    public AppelJSONParser(Context context) {
        String URL = context.getResources().getString(R.string.URL);

        this.URL_POST_USER = URL + "calls";
    }

    /**
     * creer un appel
     *
     * @param calledUerId id de l'appelant
     * @param callerUserId id de l'applé
     * @param bamId id du bam
     * @return si la requête à marchée
     */
    public boolean setAppel(int calledUerId, int callerUserId, int bamId)
    {
        List<String> urlNom  = new ArrayList<>();
        urlNom.add("call_caller_user_id");
        urlNom.add("call_called_user_id");
        urlNom.add("call_bam_id");
        urlNom.add("call_date");

        List<String> urlData = new ArrayList<>();
        urlData.add(String.valueOf(callerUserId));
        urlData.add(String.valueOf(calledUerId));
        urlData.add(String.valueOf(bamId));
        urlData.add(Utility.dateToString(new Date()));



        return new PostPutData(URL_POST_USER,"POST",urlNom,urlData).lancerEnregistrement();
    }
}
