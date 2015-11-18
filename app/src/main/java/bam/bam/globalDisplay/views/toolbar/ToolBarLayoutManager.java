package bam.bam.globalDisplay.views.toolbar;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import bam.bam.R;
import bam.bam.globalDisplay.views.MainActivity;

/**
 * layout manager de la toolBar
 *
 * @author Marc
 */
public class ToolBarLayoutManager {

    /**
     * activity globale
     */
    private MainActivity activity;

    /**
     * toolbar de l'application
     */
    private Toolbar toolbar;

    /**
     * bouton du navigation drawer
     */
    private ActionBarDrawerToggle drawerToggle;

    public ToolBarLayoutManager(MainActivity activity)
    {
        this.activity = activity;
        toolbar = (Toolbar) activity.findViewById(R.id.tool_bar);
    }

    /**
     * charger les composants de la barre
     */
    public void load()
    {
        //mettre l'action barre
        activity.setSupportActionBar(toolbar);

        // mettre la status bar transparante
        setStatusBarColor();
    }

    /**
     * mettre la status bar transparante
     */
    private void setStatusBarColor()
    {
        // test si version < à Lollipop
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < Build.VERSION_CODES.LOLLIPOP) {

            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.statusBarShadow));
        }
        else
        {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        // le bouton du navigation drawer
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * mettre le titre de la page
     *
     * @param title titre de la page
     */
    public void setTitle(String title) {
        activity.getSupportActionBar().setTitle(title);
    }

    /**
     * mettre le bouton pour le navigation drawer
     *
     * @param drawer layout du navigation drawer
     * @return drawerToggle
     */
    public ActionBarDrawerToggle addDrawerToggle(DrawerLayout drawer) {

        final AppCompatActivity act = activity;
        drawerToggle = new ActionBarDrawerToggle(activity, drawer,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.invalidateOptionsMenu();

                // si le clavier est ouvert, on le ferme
                InputMethodManager inputMethodManager = (InputMethodManager)  act.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
                act.getCurrentFocus().clearFocus();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                activity.invalidateOptionsMenu();
            }
        };
        drawerToggle.syncState();

        return drawerToggle;
    }

    /**
     * remplacer le bouton pour la navDrawer par une fleche
     *
     * @param fleche true si on veut la fleche
     */
    public void setFleche(boolean fleche) {
        drawerToggle.setDrawerIndicatorEnabled(fleche);
    }

    /**
     * synchroniser le drawerToggle
     */
    public void syncDrawerToggle() {
        drawerToggle.syncState();
    }

    /**
     * configurer le drawerToggle
     *
     * @param newConfig config
     */
    public void configDrawerToggle(Configuration newConfig) {
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * savoir si on a une fleche ou non
     *
     * @return s'il y a une fleche ou non
     */
    public boolean isDrawerIndicatorEnabled() {
        return drawerToggle.isDrawerIndicatorEnabled();
    }
}
