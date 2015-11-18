package bam.bam.utilities;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import bam.bam.globalDisplay.views.MainActivity;

/**
 * gestionnaire des toasts
 *
 * @author Marc
 */
public class InfoToast {

    /**
     * toast à afficher
     */
    private static Toast toast;

    /**
     * afficher un toast si tout est ok
     *
     * @param list savoir si on est sur les listes (tabs)
     * @param message message à afficher
     */
    public static void display(boolean list, String message, Context context)
    {

        if(context instanceof MainActivity) { // si on est sur l'appli
            MainActivity contextM = (MainActivity)context;
            if (contextM.getTabsLayoutManager().isTabsVisibile() && list || !contextM.getTabsLayoutManager().isTabsVisibile() && !list) {
                showToast(message,context);
            }
        }
        else // si on est sur le splashScreen
        {
            showToast(message,context);
        }
    }

    /**
     * afficher le toast
     *
     * @param message message à affiher
     */
    private static void showToast(String message,Context context)
    {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context,message, Toast.LENGTH_LONG);
        toast.show();
    }
}
