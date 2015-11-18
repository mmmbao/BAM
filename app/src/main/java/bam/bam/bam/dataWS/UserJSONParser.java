package bam.bam.bam.dataWS;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bam.bam.R;
import bam.bam.bam.modeles.User;

/**
 * paseur utilisateurs
 *
 * @author Marc
 */
public class UserJSONParser {

    /**
     * URL pour obtenir un utilisateur (id de l'utilisateur)
     */
    private String URL_GET_USER_IDUSEUR;

    /**
     * URL pour obtenir un utilisateur (id de l'appareil)
     */
    private String URL_GET_USER_IDDEVICE;

    /**
     * URL pour créer un utilisateur
     */
    private String URL_POST_USER;

    /**
     * URL pour modifier un utilisateur
     */
    private String URL_PUT_USER;

    /**
     * URL pour vérifier un pseudo
     */
    private String URL_GET_PSEUDO;

    public UserJSONParser(Context context) {
        String URL = context.getResources().getString(R.string.URL);

        this.URL_GET_USER_IDUSEUR = URL + "users/";
        this.URL_GET_USER_IDDEVICE =  URL + "users/device/";
        this.URL_POST_USER = URL + "users";
        this.URL_PUT_USER = URL + "users/";
        this.URL_GET_PSEUDO = URL + "users/pseudo/";
    }

    /**
     * créer un utilisateur
     *
     * @param user l'utilisateur
     * @return si la requête à marchée
     */
    public int setUser(User user)
    {

        int idUser = -1;

        List<String> urlNom  = new ArrayList<>();
        urlNom.add("user_pseudo");
        urlNom.add("user_device_id");
        urlNom.add("user_phone_number");
        urlNom.add("photo_data");

        List<String> urlData = new ArrayList<>();
        urlData.add(user.getUser_pseudo());
        urlData.add(user.getUser_device_id());
        urlData.add(user.getUser_phone_number());
        urlData.add(user.getPhoto_data());

        try {
            PostPutData ppd = new PostPutData(URL_POST_USER,"POST",urlNom,urlData);
            boolean ok = ppd.lancerEnregistrement();
            if(ok) {
                String rep = ppd.getResponse();
                JSONObject jObj = new JSONObject(rep);
                idUser = (int) jObj.get("id");
            }
        } catch (JSONException e) {
        }

        return idUser;
    }

    /**
     * modifier un utilisateur
     *
     * @param user l'utilisateur
     * @return si la requête à marchée
     */
    public boolean updateUser(User user)
    {
        List<String> urlNom  = new ArrayList<>();
        urlNom.add("user_pseudo");
        urlNom.add("user_device_id");
        urlNom.add("user_phone_number");
        urlNom.add("photo_data");

        List<String> urlData = new ArrayList<>();
        urlData.add(user.getUser_pseudo());
        urlData.add(user.getUser_device_id());
        urlData.add(user.getUser_phone_number());
        urlData.add(user.getPhoto_data());

        PostPutData ppd = new PostPutData(URL_PUT_USER + user.getId(), "PUT", urlNom, urlData);
        return ppd.lancerEnregistrement();
    }

    /**
     * obtenir un utilisateur à partir de l'id
     *
     * @param id id à utiliser
     * @param device si c'est l'id du device ou non
     * @return l'utilisateur
     */
    public User getUser(String id,boolean device,List<Boolean> connexion) {

        String URL_GET;

        if(device)
            URL_GET = URL_GET_USER_IDDEVICE;
        else
            URL_GET = URL_GET_USER_IDUSEUR;

        try {
            URL obj = new URL(URL_GET + id);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 400)
                connexion.set(0, true);

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
                connexion.set(0, true);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jObj = new JSONObject(response.toString());
                JSONObject userPhoto = (JSONObject) jObj.get("user_photo");
                String photoData = (String) userPhoto.get("photo_data");

                Gson gson = new Gson();
                Type type = new TypeToken<User>() {}.getType();
                User user = gson.fromJson(response.toString(), type);
                user.setPhoto_data(photoData);

                return user;
            }

            return null;

        } catch (Exception e)
        {
            return null;
        }
    }


    /**
     * savoir si le pseudo exist ou non
     *
     * @param pseudo le pseudo à checker
     * @return si le pseudo exist ou non
     */
    public Boolean checkPseudo(final String pseudo) {

        try {
            return new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    try {
                        URL obj = new URL(URL_GET_PSEUDO + pseudo.replace(" ","%20") + "/unique");
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                        con.setRequestMethod("GET");

                        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            StringBuffer response = new StringBuffer();

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();

                            JSONObject jObj = new JSONObject(response.toString());
                            return  (boolean) jObj.get("isAvailable");
                        }

                        return null;

                    } catch (Exception e)
                    {
                        return null;
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (Exception e)
        {
            return null;
        }
    }
}