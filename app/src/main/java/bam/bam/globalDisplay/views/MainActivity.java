package bam.bam.globalDisplay.views;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import bam.bam.R;
import bam.bam.bam.controllers.refresher.Refresher;
import bam.bam.globalDisplay.FragmentParams;
import bam.bam.globalDisplay.views.navDrawer.NavigationDrawerLayoutManager;
import bam.bam.globalDisplay.views.tabs.TabsLayoutManager;
import bam.bam.globalDisplay.views.toolbar.ToolBarLayoutManager;
import bam.bam.notifications.MyService;
import bam.bam.utilities.Clavier;
import bam.bam.utilities.Internet;

/**
 * Avtivity qui va mettre la barre, le navigation drawer, et les tabs
 *
 * @author Marc
 */
public class MainActivity extends AppCompatActivity {

    /**
     * numéro de la fragment courante
     */
    private int currFragment;

    /**
     * layout manager de la toolBar
     */
    private ToolBarLayoutManager toolBarLayoutManager;

    /**
     * layout manager du navigation drawer
     */
    private NavigationDrawerLayoutManager navDrawerLM;

    /**
     * layout manager des tabs
     */
    private TabsLayoutManager tabsLayoutManager;

    /**
     * savoir si c'est le premier lancement de l'appli
     */
    private boolean first;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        first = b.getBoolean("first");
        boolean notification = b.getBoolean("notification");

        // si l'application était fermé, on lance lance le splashcreen quand on clic sur la notif
        if(notification)
        {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(1);
            Intent mainIntent = new Intent(MainActivity.this,SplashScreen.class);
            startActivity(mainIntent);
        }

        setContentView(R.layout.drawer);

        // charger la toolBar
        toolBarLayoutManager = new ToolBarLayoutManager(this);
        toolBarLayoutManager.load();

        // charger le navigationDrawer
        navDrawerLM = new NavigationDrawerLayoutManager(this, toolBarLayoutManager);
        navDrawerLM.load();

        // charger les tabs
        tabsLayoutManager = new TabsLayoutManager(this);
        tabsLayoutManager.load();

        if (first) {
            premierLancement();
        } else {
            // charger les listes avec un delay, sinon il y aura un écran noir si internet est lent
            Handler handler = new Handler();
            Runnable delay = new Runnable() {
                @Override
                public void run() {
                    chargerListes();
                }
            };

            handler.postDelayed(delay, 1000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // lancer les notifications
        Intent serviceIntent = new Intent(this,MyService.class);
        this.stopService(serviceIntent);
        this.startService(serviceIntent);

        toolBarLayoutManager.syncDrawerToggle();
    }

    /**
     * si c'est la première connexion, charger la page profil
     */
    public void premierLancement() {
        FragmentParams fParams = FragmentParams.PROFIL_FIRST;
        loadFragment(fParams.ordinal(), false, getString(fParams.getPageTitle()));
    }

    /**
     * mettre le chargement de fond
     */
    public void chargerListes() {
        // chager la première page
        FragmentParams fParams = FragmentParams.TABS;
        loadFragment(fParams.ordinal(), true, getString(fParams.getPageTitle()));

        // lancer chargement des listes
        Refresher.getInstance().setActivity(this);
        Refresher.getInstance().firstLoad();
    }

    /**
     * charger une nouvelle fragment
     *
     * @param position position de la fragment
     * @param tabs     savoir s'il faut afficher les tabs ou non
     * @param title    titre de la page
     */
    public void loadFragment(int position, boolean tabs, String title) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = null;

        // mettre le titre
        toolBarLayoutManager.setTitle(title);

        try {
            if (currFragment != position) { // si la fragment est différente
                fragment = (Fragment) getClassePosition(position).newInstance();
            } else // sinon reste sur la même page, on ferme de drawer
            {
                navDrawerLM.closeDrawer();
            }

        } catch (Exception e){}

        if (fragment != null) {
            ft.replace(R.id.main_fragment, fragment);


            ft.commitAllowingStateLoss();
            navDrawerLM.closeDrawer();
            tabsLayoutManager.setTabsVisibility(tabs);
            currFragment = position;



            // Affectation de l'état du drawerToogle
            if (position == FragmentParams.SENDBAM.ordinal() || position == FragmentParams.PROFIL_FIRST.ordinal()) {
                toolBarLayoutManager.setFleche(false);
            } else {
                toolBarLayoutManager.setFleche(true);
            }
        }

        // changer l'item courant du navigation drawer
        navDrawerLM.notifyColor();
    }


    /**
     * gestion de la touche retour
     */
    @Override
    public void onBackPressed() {

        if (!navDrawerLM.closeDrawer() //si le navigationDrawer est ouvert, on le ferme
                && !tabsLayoutManager.backEnvoyesReponses() //si on est sur la page de réponses
                && !tabsLayoutManager.backTab()) { // si on est pas sur le premier tab, on revient dessus

            if (currFragment == FragmentParams.PROFIL_FIRST.ordinal()) { // si on fait retour sur la page profil à la première connexion
                finish();
                System.exit(0);
            }

            FragmentParams fParams = FragmentParams.TABS;
            if (currFragment != fParams.ordinal()) {

                loadFragment(fParams.ordinal(), true, getString(fParams.getPageTitle()));

                // changer l'item courant du navigation drawer
                navDrawerLM.notifyColor();
            } else {
                super.onBackPressed();
            }

        }
    }

    /**
     * ouvrir / fermer le drawer ou retour
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Si le bouton Home est appuyé dans l'actionbar.
            case android.R.id.home:

                // Si c'est une flèche retour (up carret)
                if (!toolBarLayoutManager.isDrawerIndicatorEnabled()) {
                    onBackPressed();

                    // si le clavier est ouvert, on le ferme
                    Clavier.ferrmerClavier(this);
                }
                // Si clic sur les trois traits  : on ouvre/ferme le menu
                else {
                    if (!navDrawerLM.closeDrawer()) {
                        navDrawerLM.openDrawer();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolBarLayoutManager.syncDrawerToggle();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toolBarLayoutManager.configDrawerToggle(newConfig);
    }

    /**
     * obtenir la position de la fragment courante
     *
     * @return position de la page courante
     */
    public int getCurrent_fragment_pos() {
        return currFragment;
    }

    /**
     * obtenir la classe d'une fragment
     *
     * @param position position de la fragment
     * @return classe de la fragment
     */
    public Class<?> getClassePosition(int position) {
        return FragmentParams.values()[position].getFragmentClass();
    }


    /**
     * obtenir le layout manager des tabs
     *
     * @return layout manager des tabs
     */
    public TabsLayoutManager getTabsLayoutManager() {
        return tabsLayoutManager;
    }

    /**
     * savoir si c'est la première connexion
     *
     * @return si c'est la première connexion
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * mettre si c'est la première connexion
     *
     * @param first si c'est la première connexion
     */
    public void setFirst(boolean first) {
        this.first = first;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        FragmentParams fParams = FragmentParams.TABS;

        //aller sur la page des bams recus
        if(getCurrent_fragment_pos() != fParams.ordinal())
            loadFragment(fParams.ordinal(), true, getString(fParams.getPageTitle()));
        else
        {
            tabsLayoutManager.backEnvoyesReponses();
            tabsLayoutManager.backTab();
        }

        //refresh les listes
        Refresher.getInstance().onRefresh();
    }
}