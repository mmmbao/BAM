package bam.bam.globalDisplay.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Factorise les méthodes communes aux DAO
 */
public abstract class DAO {

    /**
     * Nombre de connection ouvertes à la BDD. Est utilisé afin de savoir quand
     * il est réellement utile d'ouvrir ou de fermer la BDD. Par exemple si
     * avant d'insérer j'ai une fonction qui supprime un élément de la BDD on
     * n'ouvre pas la BDD 2 fois (supprimer et insérer) mais on la garde ouverte
     * tant que toutes les opérations n'ont pas été effectuée. Attention: il
     * faut IMPERATIVEMENT faire appel aux méthode open() et close() pour
     * maintenir la cohérence du système.
     */
    int nb_open = 0;
    /**
     * Database utilisée par le DAO
     */
    private SQLiteDatabase database;
    /**
     * DatabaseHelper utilisé par le DAO
     */
    private DatabaseHelper dbHelper;
    /**
     * Context par lequel le DAO a été construit
     */
    private Context context;

    /**
     * Constructeur
     *
     * @param context la context ayant créé le DAO
     */
    public DAO(Context context) {
        this.context = context;
        dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Ouvre la connexion à la base de donnée. Doit être appelée avant toute
     * interaction avec le DAO.
     *
     * @throws SQLException
     */
    protected void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Ferme la connexion avec la BDD. Doit être appelé lorsque la base de
     * données n'est plus utilisée. NB: penser à fermer la base dans onPause()
     * et à la rouvrir dans le onResume().
     */
    protected void close() {
        dbHelper.close();
    }

    /**
     * Retourne la base de données sur laquelle effectuer des opérations.
     *
     * @return la base de données sur laquelle effectuer des opérations.
     */
    protected SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * Retourne le context utilisé pour créer le DAO
     *
     * @return le context utilisé pour créer le DAO
     */
    protected Context getContext() {
        return context;
    }
}
