package bam.bam.bam.controllers.enregistrements;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import bam.bam.R;
import bam.bam.bam.controllers.refresher.Refresher;
import bam.bam.bam.controllers.verifications.VerifChampsProfil;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.dataWS.UserJSONParser;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.fragment.ProfilFragment;
import bam.bam.globalDisplay.FragmentParams;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.utilities.Clavier;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Internet;
import bam.bam.utilities.Utility;

/**
 * enregistrement du profil
 *
 * @author Marc
 */
public class EnregistrementProfil implements View.OnClickListener {

    /**
     * savoir s'il y a un enregistrement en cours
     */
    private boolean occup = false;

    /**
     * l'activity main
     */
    private MainActivity activity;

    /**
     * la photo
     */
    private ImageView image;

    /**
     * le téléphone
     */
    private EditText tel;

    /**
     * le pseudo
     */
    private EditText pseudoET;

    /**
     * parseur des users
     */
    private UserJSONParser userJSONParser;

    /**
     * la fragment
     */
    private ProfilFragment pf;

    public EnregistrementProfil(ProfilFragment pf, MainActivity activity, ImageView image, EditText tel, EditText pseudoET) {
        this.pf = pf;
        this.activity = activity;
        this.image = image;
        this.tel = tel;
        this.pseudoET = pseudoET;
        userJSONParser = new UserJSONParser(activity);
    }

    @Override
    public void onClick(View v) {
        if (!occup)
            saveProfil();
    }

    /**
     * enregistrement du profil
     */
    public void saveProfil()
    {
        occup = true;

        if(activity.isFirst()) // premier enregistrement
        {
            firstSave();
        }
        else { // modification du profil
            modifierProfil();
        }
    }


    /**
     * sauvegarde du premier profil
     */
    public void firstSave()
    {
        final UserDAO userDAO = new UserDAO(activity);
        boolean verifOk = new VerifChampsProfil(activity, true, pseudoET.getText().toString().trim().replaceAll("\\s+", " "), tel.getText().toString()).isVerif();
        if(verifOk) {

            new AsyncTask<Void, Void, Void>() {

                private int idUser = -2;
                private User user;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    InfoToast.display(false, activity.getString(R.string.enregistrement), activity);
                }

                @Override
                protected Void doInBackground(Void... params) {


                    Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();

                    if(Internet.isConnected(activity)) {

                        user = new User(pseudoET.getText().toString().trim().replaceAll("\\s+", " "), tel.getText().toString(),
                                Utility.encodeTobase64(bitmap),Utility.getPhoneId(activity));

                        idUser = userJSONParser.setUser(user);

                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                    if (idUser >= 0) {
                        user.setId(idUser);
                        userDAO.insertUser(user);
                        activity.setFirst(false);
                        activity.chargerListes();
                        goListsApp();
                    }

                    if (idUser == -1) {
                            InfoToast.display(true, activity.getString(R.string.pb_serveur) + "1", activity);
                    }

                    if (idUser == -2) {
                        Internet.infoInet(false, activity);
                    }

                    occup = false;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
        else
        {
            occup = false;
        }
    }

    /**
     * modification du profil
     */
    public void modifierProfil()
    {
        final UserDAO userDAO = new UserDAO(activity);
        boolean verifOk = new VerifChampsProfil(activity,false,pseudoET.getText().toString(),tel.getText().toString()).isVerif();
        if(verifOk) {

            new AsyncTask<Void, Void, Void>() {

                private Boolean serveurOk = null;
                private User user;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    InfoToast.display(false, activity.getString(R.string.enregistrement),activity);
                }

                @Override
                protected Void doInBackground(Void... params) {

                    Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();

                    if(Internet.isConnected(activity)) {


                        user = userDAO.getUserByDevice(Utility.getPhoneId(activity));

                        if(pf.isPhotoChange()) {
                            user.setPhoto_data(Utility.encodeTobase64(bitmap));
                        }

                        user.setUser_phone_number(tel.getText().toString());
                        serveurOk = userJSONParser.updateUser(user);

                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    if (serveurOk != null && serveurOk) {
                        userDAO.updateUser(user);
                        Refresher.getInstance().onRefresh();
                        goListsApp();
                    }
                    else
                    {
                        if (serveurOk != null) {
                            InfoToast.display(false, activity.getString(R.string.pb_serveur), activity);
                        }
                        else {
                            Internet.infoInet(false, activity);
                        }
                    }

                    occup = false;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
        else
        {
            occup = false;
        }
    }

    /**
     * aller sur les listes
     */
    public void goListsApp()
    {
        // si tout est ok, auvegarde

        FragmentParams fParams = FragmentParams.TABS;
        activity.loadFragment(fParams.ordinal(), true, activity.getString(fParams.getPageTitle()));
        Clavier.ferrmerClavier(activity);
        InfoToast.display(true, activity.getString(R.string.messSaveProfil),activity);
    }
}
