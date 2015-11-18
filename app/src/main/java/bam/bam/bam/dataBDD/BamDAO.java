package bam.bam.bam.dataBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bam.bam.bam.modeles.Bam;
import bam.bam.globalDisplay.database.DAO;
import bam.bam.utilities.Utility;

/**
 * Classe gérant les modules de la base de données
 *
 * @author Marc
 */
public class BamDAO extends DAO {

    /**
     * le context courant
     */
    private Context context;

    public BamDAO(Context context) {
        super(context);
        this.context = context;
    }


    /**
     * inserer des bam
     *
     * @param bams liste des bams à insérer
     * @return nombre d'insertion
     */
    public int insertBams(List<Bam> bams) {
        int nb_inserted = 0;

        this.open();

        for (Bam b : bams) {

            ContentValues values = new ContentValues();

            values.put(BamTable.ID, b.getId());
            values.put(BamTable.DESCRIPTION, b.getBam_description());
            values.put(BamTable.TITLE, b.getBam_title());
            values.put(BamTable.PRICE, b.getBam_price());
            values.put(BamTable.STATE, b.getBam_State());
            values.put(BamTable.LONGITUDE, b.getBam_longitude_position());
            values.put(BamTable.LATITUDE, b.getBam_latitude_position());
            values.put(BamTable.CREATION_DATE, b.getBam_creation_date());
            values.put(BamTable.END_DATE, b.getBam_end_date());
            values.put(BamTable.USER_ID, b.getBam_user_id());


            // On insère, sans vérifier que le bam est déjà présent
            if (getDatabase().insert(BamTable.TABLE_NAME, null, values) != -1) {
                nb_inserted++;
            }

        }

        this.close();

        return nb_inserted;
    }

    /**
     * liste des bams selon une position
     *
     * @param id_user id de l'utilisateur
     * @return liste des bams selon la position
     */
    public List<Bam> getBamsPos(int id_user) {

        this.open();

        Cursor curseur = getDatabase().rawQuery("SELECT * FROM " + BamTable.TABLE_NAME
                + " WHERE " + BamTable.USER_ID + " != '" + id_user + "'"
                + " AND " + BamTable.END_DATE + " > '" + Utility.dateToString(new Date())  + "'"
                + " ORDER BY " + BamTable.CREATION_DATE
                , null);
        List<Bam> bams = new ArrayList<>();

        for (curseur.moveToFirst(); !curseur.isAfterLast(); curseur.moveToNext()) {
            bams.add(cursorToBam(curseur));
        }

        curseur.close();

        this.close();

        return bams;
    }

    /**
     * obtenir liste des bams de l'utilisateur
     *
     * @param id_user id de l'utilisateur
     * @return liste des bams de l'utilisateur
     */
    public List<Bam> getLastBamsUser(int id_user) {

        this.open();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.roll(Calendar.DAY_OF_YEAR, -1);

        Cursor curseur = getDatabase().rawQuery("SELECT * FROM " + BamTable.TABLE_NAME
                + " WHERE " + BamTable.USER_ID + " = '" + id_user + "'"
                + " AND " + BamTable.END_DATE + " > '" + Utility.dateToString(cal.getTime())  + "'"
                + " ORDER BY " + BamTable.CREATION_DATE + " DESC "
                , null);
        List<Bam> bams = new ArrayList<>();

        for (curseur.moveToFirst(); !curseur.isAfterLast(); curseur.moveToNext()) {
            bams.add(cursorToBam(curseur));
        }

        curseur.close();

        this.close();

        return bams;
    }

    /**
     * obtenir tout les bams
     *
     * @return liste de tout les bams
     */
    public List<Bam> getBams() {

        this.open();

        Cursor curseur = getDatabase().rawQuery("SELECT * FROM " + BamTable.TABLE_NAME
                , null);

        List<Bam> bams = new ArrayList<>();

        for (curseur.moveToFirst(); !curseur.isAfterLast(); curseur.moveToNext()) {
            bams.add(cursorToBam(curseur));
        }

        curseur.close();
        this.close();

        return bams;
    }

    /**
     * supprimer un bam
     *
     * @param bam bam à supprimer
     */
    public void deleteBam(Bam bam) {
        this.open();
        getDatabase().delete(BamTable.TABLE_NAME, BamTable.ID + " = " + bam.getId(), null);
        this.close();
    }

    /**
     * insérer un bam
     *
     * @param bam bam
     * @return nombre d'insertions
     */
    public int insertBam(Bam bam) {
        List<Bam> bams = new ArrayList<>();
        bams.add(bam);
        return insertBams(bams);
    }

    /**
     * obtenir un bam à partir du curseur
     *
     * @param curseur le curseur
     * @return le bam
     */
    private Bam cursorToBam(Cursor curseur) {

        return new Bam(curseur.getInt(curseur.getColumnIndex(BamTable.ID)),
                curseur.getString(curseur.getColumnIndex(BamTable.DESCRIPTION)),
                curseur.getString(curseur.getColumnIndex(BamTable.TITLE)),
                curseur.getFloat(curseur.getColumnIndex(BamTable.PRICE)),
                curseur.getInt(curseur.getColumnIndex(BamTable.STATE)),
                curseur.getDouble(curseur.getColumnIndex(BamTable.LONGITUDE)),
                curseur.getDouble(curseur.getColumnIndex(BamTable.LATITUDE)),
                curseur.getString(curseur.getColumnIndex(BamTable.CREATION_DATE)),
                curseur.getString(curseur.getColumnIndex(BamTable.END_DATE)),
                curseur.getInt(curseur.getColumnIndex(BamTable.USER_ID)));
    }

    /**
     * nettoyer la BDD
     */
    public void clear()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.roll(Calendar.DAY_OF_YEAR, -1);

        this.open();
        getDatabase().delete(BamTable.TABLE_NAME,
                BamTable.END_DATE + " < '" + Utility.dateToString(cal.getTime())  + "'", null);
        this.close();
    }

}
