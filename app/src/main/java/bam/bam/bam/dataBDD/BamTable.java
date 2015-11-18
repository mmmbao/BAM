package bam.bam.bam.dataBDD;

import android.database.sqlite.SQLiteDatabase;

/**
 * table bam
 *
 * @author Marc
 */
public class BamTable {

    /**
     * Nom de la table des bams.
     */
    public static final String TABLE_NAME = "Bam";

    /**
     * id
     */
    public static final String ID = "id";

    /**
     * description
     */
    public static final String DESCRIPTION = "description";

    /**
     * titre
     */
    public static final String TITLE = "title";

    /**
     * prix
     */
    public static final String PRICE = "price";

    /**
     * state
     */
    public static final String STATE = "state";

    /**
     * latitude
     */
    public static final String LATITUDE = "latitude";

    /**
     * longitude
     */
    public static final String LONGITUDE = "longitude";

    /**
     * date de création
     */
    public static final String CREATION_DATE = "creation_date";

    /**
     * date de fin
     */
    public static final String END_DATE = "end_date";

    /**
     * id de l'utilisateur
     */
    public static final String USER_ID = "user_id";


    /**
     * Commande de creation de la table
     */
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME
            + "( " + ID + " integer not null, "
            + DESCRIPTION + " text not null, "
            + TITLE + " text not null, "
            + PRICE + " real not null, "
            + STATE + " real not null, "
            + LATITUDE + " double not null, "
            + LONGITUDE + " double not null, "
            + CREATION_DATE + " date not null, "
            + END_DATE + " date not null, "
            + USER_ID + " integer not null "
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
