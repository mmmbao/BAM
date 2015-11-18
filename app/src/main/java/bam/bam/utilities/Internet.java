package bam.bam.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

import bam.bam.R;

/**
 * classe Internet
 *
 * @author Marc
 */
public class Internet {

    /**
     * savoir si le téléphone est connecté
     *
     * @return si le téléphone est connecté
     */
    public static boolean isConnected(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    /**
     * message d'erreur internet
     *
     * @param list si on est sur les tabs
     * @param context le context
     */
    public static void infoInet(boolean list,Context context)
    {
        InfoToast.display(list, context.getString(R.string.warnInet),context);
    }
}
