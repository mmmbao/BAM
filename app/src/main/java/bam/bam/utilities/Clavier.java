package bam.bam.utilities;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * classe pour le clavier
 *
 * @author Marc
 */
public class Clavier {

    /**
     * fermer le clavier
     *
     * @param context le context
     */
    public static void ferrmerClavier(Activity context)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        context.getCurrentFocus().clearFocus();
    }
}
