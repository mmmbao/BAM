package bam.bam.globalDisplay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * DataAcessObject pour la table des paramètres
 */
public class ParametersDAO extends DAO {

    /**
     * Constructeur
     *
     * @param context contexte courant
     */
    public ParametersDAO(Context context) {
        super(context);
    }


    /**
     * savoir si la BDD possède des informations
     *
     * @return si la BDD possède des informations
     */
    public boolean isBDDFill() {

        this.open();

        Cursor curseur = getDatabase().rawQuery("SELECT " + ParametersTable.BDD_FILL + " FROM " + ParametersTable.TABLE_NAME, null);

        if (curseur.moveToFirst()) {
            int fill = curseur.getInt(curseur.getColumnIndex(ParametersTable.BDD_FILL));
            if(fill == 0)
                return false;

            return true;
        }

        curseur.close();
        this.close();

        return false;
    }

    /**
     * mettre si la BDD possède des informations
     *
     * @param fill si la BDD possède des informations
     */
    public void setBDDFill(boolean fill) {
        ContentValues values = new ContentValues();
        if (fill)
            values.put(ParametersTable.BDD_FILL, 1);
        else
            values.put(ParametersTable.BDD_FILL, 0);
        this.open();
        getDatabase().update(ParametersTable.TABLE_NAME, values, null, null);
        this.close();
    }

    /**
     * obtenir la dernière date de MAJ
     *
     * @return la dernière date de MAJ
     */
    public String getLastUpdate(int type) {
        String date = "";
        String colonne = "";

        if(type == 1)
            colonne = ParametersTable.LAST_UPDATE_REC;
        if(type == 2)
            colonne = ParametersTable.LAST_UPDATE_REP;
        if(type == 3)
            colonne = ParametersTable.LAST_UPDATE_REC_NOTIF;

        this.open();
        Cursor cursor = getDatabase().rawQuery("SELECT " + colonne + " FROM " + ParametersTable.TABLE_NAME, null);
        if (cursor.moveToFirst())
            date = cursor.getString(cursor.getColumnIndex(colonne));
        cursor.close();
        this.close();
        return date;
    }


    /**
     * mettre la dernière date de MAJ
     *
     * @param date la dernière date de MAJ
     */
    public void setLastUpdate(String date,int type) {
        ContentValues values = new ContentValues();
        String colonne="";

        if(type == 1)
            colonne = ParametersTable.LAST_UPDATE_REC;
        if(type == 2)
            colonne = ParametersTable.LAST_UPDATE_REP;
        if(type == 3)
            colonne = ParametersTable.LAST_UPDATE_REC_NOTIF;

        values.put(colonne,date);

        this.open();
        getDatabase().update(ParametersTable.TABLE_NAME, values, null, null);
        this.close();
    }


}
