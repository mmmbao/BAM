package bam.bam.bam.dataBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import bam.bam.bam.modeles.User;
import bam.bam.globalDisplay.database.DAO;
import bam.bam.utilities.Utility;

/**
 * Classe gérant les modules de la base de données
 *
 * @author Marc
 */
public class UserDAO extends DAO {

    /**
     * le context courant
     */
    private Context context;

    public UserDAO(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * insérer des utilisateurs
     *
     * @param users liste des utilisateurs
     * @return nombre d'insertions
     */
    public int insertUsers(List<User> users) {
        int nb_inserted = 0;

        this.open();


        for (User u : users) {

            User check = getUser(u.getId());
            if (check != null) {

                deleteUser(check);
            }

            ContentValues values = new ContentValues();

            values.put(UserTable.ID, u.getId());
            values.put(UserTable.PSEUDO, u.getUser_pseudo());
            values.put(UserTable.PHOTO, u.getPhoto_data());
            values.put(UserTable.PHONE, u.getUser_phone_number());
            values.put(UserTable.DEVICE_ID, u.getUser_device_id());

            // On insère, sans vérifier que le user est déjà présent
            if (getDatabase().insert(UserTable.TABLE_NAME, null, values) != -1) {
                nb_inserted++;
            }
        }

        this.close();

        return nb_inserted;
    }

    /**
     * modifier un utilisateur
     *
     * @param user utilisateur à modifier
     */
    public void updateUser(User user) {

        this.open();

        ContentValues values = new ContentValues();
        values.put(UserTable.PHOTO, user.getPhoto_data());
        values.put(UserTable.PHONE, user.getUser_phone_number());
        values.put(UserTable.DEVICE_ID, user.getUser_device_id());

        getDatabase().update(UserTable.TABLE_NAME, values, UserTable.ID + " =  '" + user.getId() + "'", null);

        this.close();
    }

    /**
     * supprimer un utilisateur
     *
     * @param user utilisateur à supprimer
     */
    public void deleteUser(User user) {
        this.open();
        int res = getDatabase().delete(UserTable.TABLE_NAME,
                UserTable.ID + " = " + user.getId(), null);
        this.close();
    }



    /**
     * obtenir tout les utilisateurs
     *
     * @return liste de tout les utilisateurs
     */
    public List<User> getUsers() {

        this.open();

        Cursor curseur = getDatabase().rawQuery("SELECT * FROM " + UserTable.TABLE_NAME
                , null);

        List<User> users = new ArrayList<>();

        for (curseur.moveToFirst(); !curseur.isAfterLast(); curseur.moveToNext()) {
            users.add(cursorToUser(curseur));
        }

        curseur.close();
        this.close();

        return users;
    }


    /**
     * insérer un utilisateur
     *
     * @param user utilisateur
     * @return nombre d'insertions
     */
    public int insertUser(User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
        return insertUsers(users);
    }

    /**
     * obtenir un utilisateur à partir de son id d'appareil
     *
     * @param idDevice id de l'utilisateur
     * @return l'utilisateur
     */
    public User getUserByDevice(String idDevice) {
        for (User u : getUsers()) {
            if (u.getUser_device_id().equals(idDevice))
                return u;
        }
        return null;
    }

    /**
     * obtenir un utilisateur à partir de son id
     *
     * @param id_user id de l'utilisateur
     * @return l'utilisateur
     */
    public User getUser(int id_user) {
        for (User u : getUsers()) {
            if (u.getId() == id_user)
                return u;
        }
        return null;
    }

    /**
     * obtenir un utilisateur à partir du curseur
     *
     * @param curseur le curseur
     * @return l'utilisateur
     */
    public User cursorToUser(Cursor curseur) {

        return new User(curseur.getInt(curseur.getColumnIndex(UserTable.ID)),
                curseur.getString(curseur.getColumnIndex(UserTable.PSEUDO)),
                curseur.getString(curseur.getColumnIndex(UserTable.PHONE)),
                curseur.getString(curseur.getColumnIndex(UserTable.PHOTO)),
                curseur.getString(curseur.getColumnIndex(UserTable.DEVICE_ID)));
    }

    /**
     * nettoyer la BDD
     */
    public void clear()
    {
        this.open();
        getDatabase().delete(UserTable.TABLE_NAME,
                UserTable.DEVICE_ID + " != '" + Utility.getPhoneId(context) + "'" + " AND " +
                        UserTable.ID + " NOT IN (SELECT DISTINCT " +
                        BamTable.USER_ID + " FROM " + BamTable.TABLE_NAME + ") AND " +
                        UserTable.ID + " NOT IN (SELECT DISTINCT " +
                        ReponseTable.ID_USER + " FROM " + ReponseTable.TABLE_NAME + ")", null);
        this.close();
    }

}
