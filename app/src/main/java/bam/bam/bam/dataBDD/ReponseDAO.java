package bam.bam.bam.dataBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import bam.bam.bam.modeles.User;
import bam.bam.globalDisplay.database.DAO;

/**
 * Classe gérant les modules de la base de données
 *
 * @author Marc
 */
public class ReponseDAO extends DAO {

    /**
     * le context
     */
    private Context context;

    public ReponseDAO(Context context) {
        super(context);

        this.context = context;
    }

    /**
     * insérer des réponses
     *
     * @param users liste des réponses (utilisateurs)
     * @param datesRep id de l'utilisateur
     * @param id_bam id du bam
     * @return nombre d'insertion
     */
    public int insertReponses(List<User> users, List<String> datesRep,int id_bam) {
        int nb_inserted = 0;

        this.open();

        for (int i = 0; i < users.size(); i++)
        {

            ContentValues values = new ContentValues();

            values.put(ReponseTable.ID_BAM, id_bam);
            values.put(ReponseTable.ID_USER, users.get(i).getId());
            values.put(ReponseTable.RESPONCE_DATE,datesRep.get(i));



            // On insère, sans vérifier que le bam est déjà présent
            if (getDatabase().insert(ReponseTable.TABLE_NAME, null, values) != -1) {
                nb_inserted++;
            }

        }

        this.close();

        return nb_inserted;
    }


    /**
     * nettoyer la BDD
     */
    public void clear()
    {
        this.open();
        getDatabase().delete(ReponseTable.TABLE_NAME,
                ReponseTable.ID_BAM + " NOT IN (SELECT " +
                        BamTable.ID + " FROM " + BamTable.TABLE_NAME + ")", null);
        this.close();
    }

    /**
     * obtenir la liste des réponses
     *
     * @param id_bam id du bam
     * @return liste des réponses (utilisateurs)
     */
    public List<User> getUsersRep(int id_bam) {

        this.open();

        Cursor curseur = getDatabase().rawQuery("SELECT * FROM " + UserTable.TABLE_NAME
                + " u, " + ReponseTable.TABLE_NAME + " r"
                + " WHERE u." + UserTable.ID + " = r." + ReponseTable.ID_USER + ""
                + " AND r." + ReponseTable.ID_BAM + " = '" + id_bam + "'"
                + " ORDER BY r." + ReponseTable.RESPONCE_DATE
                , null);

        List<User> users = new ArrayList<>();

        for (curseur.moveToFirst(); !curseur.isAfterLast(); curseur.moveToNext()) {
            users.add(new UserDAO(context).cursorToUser(curseur));
        }

        curseur.close();
        this.close();

        return users;
    }
}
