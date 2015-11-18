package bam.bam.globalDisplay.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import bam.bam.bam.dataBDD.BamTable;
import bam.bam.bam.dataBDD.ReponseTable;
import bam.bam.bam.dataBDD.UserTable;

/**
 * Classe gérant la création d'une base de données. Elle contient le nom de la
 * base, le nom des tables et de chaque colonne.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Nom de la base.
     */
    private static final String DATABASE_NAME = "isidroid";
    /**
     * Version de la base.
     */
    private static final int VERSION = 7;

    /**
     * Instance du singleton
     */
    private static DatabaseHelper instance;

    /**
     * Nombre de connection ouvertes à la BDD. Est utilisé afin de savoir quand
     * il est réellement utile d'ouvrir ou de fermer la BDD. Par exemple si
     * avant d'insérer j'ai une fonction qui supprime un élément de la BDD on
     * n'ouvre pas la BDD 2 fois (supprimer et insérer) mais on la garde ouverte
     * tant que toutes les opérations n'ont pas été effectuée.
     */
    private int nb_open = 0;


    /**
     * Constructeur privé (pattenr singleton)
     *
     * @param context contexte courant
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

    }

    /**
     * Accès à l'instance singleton
     *
     * @param context contexte courant
     * @return instance unique de DatabaseHelper
     */
    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        nb_open++;
        return super.getWritableDatabase();
    }


    @Override
    public synchronized void close() {
        nb_open--;
        if (nb_open == 0) {
            super.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Parametre - Creation de la table qui gère les paramètres
        ParametersTable.onCreate(database);

        // User - Création de table User
        UserTable.onCreate(database);

        // Bam - Création de la table Bam
        BamTable.onCreate(database);

        // Reponse - Création de la table Reponse
        ReponseTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
    /*
     * Mise à jour de la base vers une nouvelle version (Pour le moment, on
	 * ne fait que supprimer la base et la recréer)
	 */
        Log.w("DatabaseHelper", "Suppression et recréation de la base");

        // User
        UserTable.onUpgrade(database, oldVersion, newVersion);

        // Bam
        BamTable.onUpgrade(database, oldVersion, newVersion);

        // Response
        ReponseTable.onUpgrade(database, oldVersion, newVersion);

        // Paramètre
        ParametersTable.onUpgrade(database, oldVersion, newVersion);
    }

}
