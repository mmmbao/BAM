package bam.bam.bam.dataWS;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bam.bam.R;
import bam.bam.bam.modeles.User;
import bam.bam.utilities.Utility;

/**
 * paseur reponses
 *
 * @author Marc
 */
public class RepJSONParser {

    /**
     * URL pour obtenir une réponse
     */
    private String URL_GET_BAM_REP;

    /**
     * URL pour créer une réponse
     */
    private String URL_POST_BAM_REP;

    public RepJSONParser(Context context) {
        String URL = context.getResources().getString(R.string.URL);

        this.URL_GET_BAM_REP = URL + "responses/bam/";
        this.URL_POST_BAM_REP = URL + "responses";
    }

    /**
     * créer une réponse
     *
     * @param idBam id du bam
     * @param idUser id de l'utilisateur
     * @return si la requête à marchée
     */
    public boolean setBamRep(final int idBam, final int idUser,List<Boolean> codeAnnul) {

        List<String> urlNom  = new ArrayList<>();
        urlNom.add("response_user_id");
        urlNom.add("response_bam_id");
        urlNom.add("response_date");

        List<String> urlData = new ArrayList<>();
        urlData.add(String.valueOf(idUser));
        urlData.add(String.valueOf(idBam));
        urlData.add(Utility.dateToString(new Date()));

        PostPutData ppd = new PostPutData(URL_POST_BAM_REP,"POST",urlNom,urlData);
        boolean ok = ppd.lancerEnregistrement();

        if(ok) {
            if (ppd.getResponseCode() == 403) {
                codeAnnul.set(0, true);
            }
        }

        return ok;
    }

    /**
     * onbtenir les réponses d'un bam
     *
     * @param idBam id du bam
     * @param datesRep la date des réponses
     * @param lastUpdate dernière modification de la BDD
     * @param newMAJ nouvelle date de MAJ
     * @return si la requête à marchée
     */
    public List<User> getBamRep(int idBam,List<String> datesRep,String lastUpdate,List<String> newMAJ) {

        List<User> users = new ArrayList<>();

        try {
            URL obj = new URL(URL_GET_BAM_REP + idBam + "?last_update_date=" + lastUpdate.replace(" ","%20"));

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // success

                newMAJ.clear();
                newMAJ.add(Utility.dateToString(new Date()));

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                Type type = new TypeToken<User>() {}.getType();

                JSONObject jObj = new JSONObject(response.toString());
                JSONArray jReps = (JSONArray)jObj.get("responses");

                for(int i=0 ; i<jReps.length() ; i++) {
                    JSONObject jRep = (JSONObject) jReps.get(i);
                    JSONObject jUser = (JSONObject) jRep.get("response_user");
                    User user = gson.fromJson(jUser.toString(), type);

                    JSONObject userPhoto = (JSONObject) jUser.get("user_photo");
                    String photoData = (String) userPhoto.get("photo_data");
                    user.setPhoto_data(photoData);

                    String date = (String) jRep.get("response_date");
                    datesRep.add(date);

                    users.add(user);
                }

                return users;
            }

            return null;

        } catch (Exception e)
        {
            return null;
        }
    }
}
