package bam.bam.globalDisplay.views.tabs;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.balysv.materialripple.MaterialRippleLayout;

import bam.bam.R;
import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;
import bam.bam.globalDisplay.FragmentParams;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.globalDisplay.views.toolbar.ViewPagerAdapter;

/**
 * layout manager pour les tabs
 *
 * @author Marc
 */
public class TabsLayoutManager {

    /**
     * activity de l'appli
     */
    private MainActivity activity;

    /**
     * viewPager
     */
    private ViewPager pager;

    /**
     * adaptateur du viewPager
     */
    private ViewPagerAdapter adapterVP;

    /**
     * gestion des fragments des tabs
     */
    private SlidingTabLayout tabs;

    /**
     * layout des tabs
     */
    private RelativeLayout tabsLayout;

    /**
     * crayon pour envoyer un bam
     */
    private MaterialRippleLayout senBam;

    /**
     * layout du crayon pour envoyer un bam
     */
    private RelativeLayout boutonCrayonL;

    /**
     * fragment des bams envoyes / reponses
     */
    private BamsEnvoyesReponsesFragment berf;

    public TabsLayoutManager(MainActivity activity)
    {
        this.activity = activity;

        // titres des tabs
        String tabsTitles[] = new String[] {
                activity.getString(R.string.tab_recus),
                activity.getString(R.string.tab_envoyes)
        };

        // nombre de tabs
        int numberOfTabs = tabsTitles.length;

        senBam =  (MaterialRippleLayout) activity.findViewById(R.id.sendBam);
        boutonCrayonL = (RelativeLayout)activity.findViewById(R.id.boutonCrayonL);
        pager = (ViewPager) activity.findViewById(R.id.pager);
        tabs = (SlidingTabLayout) activity.findViewById(R.id.tabs);
        tabsLayout = (RelativeLayout)activity.findViewById(R.id.tabsLayout);
        adapterVP =  new ViewPagerAdapter(activity.getSupportFragmentManager(),tabsTitles,numberOfTabs);

    }

    /**
     * charger les composants des tabs
     */
    public void load()
    {

        pager.setAdapter(adapterVP);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return activity.getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);

        //charger la page pour envoyer un bam quand on clic sur le crayon
        senBam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentParams fParams = FragmentParams.SENDBAM;
                activity.loadFragment(fParams.ordinal(), false, activity.getString(fParams.getPageTitle()));
            }
        });

        berf = (BamsEnvoyesReponsesFragment)adapterVP.getItem(1);
    }


    /**
     * savoir si les tabs sont visibles ou non
     *
     * @return si les tabs sont visibles ou non
     */
    public boolean isTabsVisibile()
    {
        if (tabsLayout.getVisibility() == View.VISIBLE)
        {
            return true;
        }

        return false;

    }


    /**
     * mettre les tabs visible ou non
     *
     * @param tabs si les tabs doivent etre visible ou non
     */
    public void setTabsVisibility(boolean tabs)
    {
        if (tabs)
        {
            tabsLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            pager.setCurrentItem(0);
            tabsLayout.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * mettre le crayon visible ou non
     *
     * @param visibility visibilité pour le crayon
     */
    public void setCrayonVisibility(int visibility) { boutonCrayonL.setVisibility(visibility);}

    /**
     * revenir sur la page envoyés depuis la page réponses
     *
     * @return true si on a fait un back
     */
    public boolean backEnvoyesReponses() {
        if (!(berf.isEnvVisible()) && tabsLayout.getVisibility() == View.VISIBLE) {
            berf.frameBack();
            return true;
        }

        return false;
    }

    /**
     * revenir sur la page reçus depuis la page envoyés
     *
     * @return true si on a fait un back
     */
    public boolean backTab() {
        if (pager.getCurrentItem() != 0 && tabsLayout.getVisibility() == View.VISIBLE) {
            pager.setCurrentItem(0);
            return true;
        }

        return false;
    }

    /**
     * obtenir l'adaptateur du viewPager
     *
     * @return adaptateur du viewPager
     */
    public ViewPagerAdapter getAdapterVP() {
        return adapterVP;
    }

    /**
     * mettre le tab voulu
     *
     * @param elem numéro du tab
     */
    public void setPager(int elem) {
        pager.setCurrentItem(elem,false);
    }
}