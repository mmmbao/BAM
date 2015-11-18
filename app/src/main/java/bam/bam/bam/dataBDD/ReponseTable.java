package bam.bam.bam.dataBDD;

import android.database.sqlite.SQLiteDatabase;

/**
 * table réponse
 *
 * @author Marc
 */
public class ReponseTable {

    /**
     * Nom de la table des reponses.
     */
    public static final String TABLE_NAME = "Reponse";

    /**
     * id de l'utilisateur
     */
    public static final String ID_USER = "id_user";

    /**
     * id du bam
     */
    public static final String ID_BAM = "id_bam";

    /**
     * date de la réponse
     */
    public static final String RESPONCE_DATE = "responce_date";

    /**
     * Commande de creation de la table
     */
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME
            + "( " + ID_USER + " integer not null, "
            + ID_BAM + " integer not null, "
            + RESPONCE_DATE + " date not null "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
