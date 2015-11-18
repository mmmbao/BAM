package bam.bam.bam.dataWS;

import android.content.Context;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
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
import bam.bam.bam.modeles.Bam;
import bam.bam.utilities.Utility;

/**
 * paseur bams
 *
 * @author Marc
 */
public class BamJSONParser {

    /**
     * URL pour obtenir les bams d'un user
     */
    private String URL_GET_BAMS_USER;

    /**
     * URL pour obtenir les bams selon une position
     */
    private String URL_GET_BAMS_POS;

    /**
     * créer un bam
     */
    private String URL_POST_BAM;

    /**
     * modifier un bam
     */
    private String URL_PUT_BAM_ANNULATION;

    public BamJSONParser(Context context) {
        String URL = context.getResources().getString(R.string.URL);
        this.URL_PUT_BAM_ANNULATION = URL + "bams/";
        this.URL_GET_BAMS_USER = URL + "bams/user/";
        this.URL_GET_BAMS_POS = URL + "bams?";
        this.URL_POST_BAM = URL + "bams";
    }

    /**
     * creer un bam
     *
     * @param bam le bam à insérer
     * @param codeLimite savoir si la limite de bam est atteinte
     * @return si la requête à marchée
     */
    public Bam setBam(Bam bam,List<Boolean> codeLimite)
    {
        Bam bamRep = new Bam();

        List<String> urlNom  = new ArrayList<>();
        urlNom.add("bam_user_id");
        urlNom.add("bam_title");
        urlNom.add("bam_description");
        urlNom.add("bam_price");
        urlNom.add("bam_state");
        urlNom.add("bam_latitude_position");
        urlNom.add("bam_longitude_position");
        urlNom.add("bam_creation_date");
        urlNom.add("bam_end_date");

        List<String> urlData = new ArrayList<>();
        urlData.add(String.valueOf(bam.getBam_user_id()));
        urlData.add(bam.getBam_title());
        urlData.add(bam.getBam_description());
        urlData.add(String.valueOf(bam.getBam_price()));
        urlData.add("1");
        urlData.add(String.valueOf(bam.getBam_latitude_position()));
        urlData.add(String.valueOf(bam.getBam_longitude_position()));
        urlData.add(bam.getBam_creation_date());
        urlData.add(bam.getBam_end_date());

        try {
            PostPutData ppd = new PostPutData(URL_POST_BAM,"POST",urlNom,urlData);
            boolean ok = ppd.lancerEnregistrement();

            if(ok) {
                if(ppd.getResponseCode() == 429) {
                    codeLimite.set(0, true);
                }
                else {
                    String rep = ppd.getResponse();
                    JSONObject jObj = new JSONObject(rep);

                    Gson gson = new Gson();
                    Type type = new TypeToken<Bam>() {}.getType();
                    bamRep = gson.fromJson(jObj.toString(), type);

                }
            }
        } catch (JSONException e) {}


        return bamRep;
    }

    /**
     * obtenir les bams selon une position
     *
     * @param location la localisation
     * @param idUser id de l'utilisateur
     * @param lastUpdate dernière modification de la BDD
     * @param newMAJ nouvelle date de MAJ
     * @return si la requête à marchée
     */
    public List<Bam> getBamsPos(Location location,int idUser,String lastUpdate,List<String> newMAJ) {

        List<Bam> bams = new ArrayList<>();

        try {
            URL obj = new URL(URL_GET_BAMS_POS
                    + "bam_latitude_position=" + location.getLatitude()
                    + "&bam_longitude_position=" + location.getLongitude()
                    + "&bam_user_id=" + idUser
                    + "&last_update_date=" + lastUpdate.replace(" ","%20"));

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // success

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
                Type type = new TypeToken<Bam>() {
                }.getType();

                JSONObject jObj = new JSONObject(response.toString());
                JSONArray jBams = (JSONArray) jObj.get("bams");


                for (int i = 0; i < jBams.length(); i++) {
                    JSONObject jBam = (JSONObject) jBams.get(i);
                    Bam bam = gson.fromJson(jBam.toString(), type);
                    bams.add(bam);
                }

                return bams;
            }

            return null;

        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * obtenir les bams d'un user
     *
     * @param idUser id de l'utilisateur
     * @return si la requête à marchée
     */
    public List<Bam> getLastBamsUser(int idUser) {

        List<Bam> bams = new ArrayList<>();

        try {
            URL obj = new URL(URL_GET_BAMS_USER + idUser);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                Type type = new TypeToken<Bam>() {
                }.getType();

                JSONObject jObj = new JSONObject(response.toString());
                JSONArray jBams = (JSONArray) jObj.get("bams");


                for (int i = 0; i < jBams.length(); i++) {
                    JSONObject jBam = (JSONObject) jBams.get(i);
                    Bam bam = gson.fromJson(jBam.toString(), type);
                    bams.add(bam);
                }

                return bams;
            }

            return null;

        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * annuler un bam
     *
     * @param idBam le bam à annuler
     * @return si la requête à marchée
     */
    public boolean annulerBam(int idBam)
    {
        List<String> urlNom  = new ArrayList<>();
        List<String> urlData = new ArrayList<>();

        PostPutData ppd = new PostPutData(URL_PUT_BAM_ANNULATION + idBam + "/cancel","PUT",urlNom,urlData);
        return ppd.lancerEnregistrement();
    }
}