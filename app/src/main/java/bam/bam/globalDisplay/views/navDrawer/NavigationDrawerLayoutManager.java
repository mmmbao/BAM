package bam.bam.globalDisplay.views.navDrawer;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import bam.bam.R;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.globalDisplay.views.toolbar.ToolBarLayoutManager;

/**
 * layout manager du navigation drawer
 *
 * @author Marc
 */
public class NavigationDrawerLayoutManager {

    /**
     * activity globale
     */
    private MainActivity activity;

    /**
     * liste du navigation drawer
     */
    private RecyclerView recyclerView;

    /**
     * adaptateur de la liste du navigation drawer
     */
    private RecyclerView.Adapter adapter;

    /**
     * layout manager de la liste du navigation drawer
     */
    private RecyclerView.LayoutManager layoutManager;

    /**
     * adaptateur de la liste du navigation drawer
     */
    private DrawerLayout drawer;

    /**
     * layout manager de la toolBar
     */
    private ToolBarLayoutManager toolBarLayoutManager;

    public NavigationDrawerLayoutManager(MainActivity activity,ToolBarLayoutManager toolBarLayoutManager)
    {
        this.activity = activity;
        this.toolBarLayoutManager = toolBarLayoutManager;

        recyclerView = (RecyclerView) activity.findViewById(R.id.RecyclerView);
        drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        adapter = new NavigationDrawerAdaptater(activity);
        layoutManager = new LinearLayoutManager(activity);

    }

    /**
     * charger les composants du navigation drawer
     */
    public void load()
    {

        // mettre le NaviationDrawer
        addNavigationDrawerLayout();

        // mettre le bouton
        drawer.setDrawerListener(toolBarLayoutManager.addDrawerToggle(drawer));
    }

    /**
    * ajout du navigation drawer
    */
    private void addNavigationDrawerLayout()
    {
        // mettre le recyclerView du NaviationDrawer
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // foncer la page quand le navigation drawer est open
        drawer.setScrimColor(activity.getResources().getColor(R.color.shadowNav));
    }

    /**
     * fermer le drawer
     *
     * @return true si le drawer a été fermé
     */
    public boolean closeDrawer() {
        if (drawer.isDrawerOpen(recyclerView)) {
            drawer.closeDrawer(recyclerView);
            return true;
        }

        return false;
    }

    /**
     * ouvrir le drawer
     *
     * @return true si le drawer a été ouvert
     */
    public boolean openDrawer() {
        if (!drawer.isDrawerOpen(recyclerView)) {
            drawer.openDrawer(recyclerView);
            return true;
        }

        return false;
    }

    /**
     * changer la page courrante du navigationDrawer
     */
    public void notifyColor() {
        adapter.notifyDataSetChanged();
    }


}
