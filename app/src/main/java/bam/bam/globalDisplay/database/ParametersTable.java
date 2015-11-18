package bam.bam.globalDisplay.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Date;

import bam.bam.utilities.Utility;

/**
 * Définit les constantes (nom et colonnes) de la table stockant les divers
 * paramètres (dernière mise à jour, etc)
 */
public class ParametersTable {

    /**
     * Nom de la table gérant les updates.
     */
    public static final String TABLE_NAME = "parametre";

    /**
     * date de la dernière MAJ pour les bams recus
     */
    public static final String LAST_UPDATE_REC = "last_update_rec";

    /**
     * date de la dernière MAJ pour les réponses
     */
    public static final String LAST_UPDATE_REP = "last_update_rep";

    /**
     * date de la dernière MAJ pour les bams recus (notifications)
     */
    public static final String LAST_UPDATE_REC_NOTIF = "last_update_rec_notif";

    /**
     * si la BDD possède des informations
     */
    public static final String BDD_FILL = "bdd_fill";

    /**
     * Commande de création de la table
     */
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "( "
            + LAST_UPDATE_REC + " date not null, "
            + LAST_UPDATE_REP + " date not null, "
            + LAST_UPDATE_REC_NOTIF + " date not null, "
            + BDD_FILL + " integer not null "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.roll(Calendar.DAY_OF_YEAR, -4);

        // On insère les valeurs de départ pour les paramètres
        ContentValues values = new ContentValues();
        values.put(BDD_FILL, 0);
        values.put(LAST_UPDATE_REC, Utility.dateToString(cal.getTime()));
        values.put(LAST_UPDATE_REP, Utility.dateToString(cal.getTime()));
        values.put(LAST_UPDATE_REC_NOTIF, Utility.dateToString(cal.getTime()));
        database.insert(TABLE_NAME, null, values);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
