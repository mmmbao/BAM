package bam.bam.globalDisplay;

import bam.bam.R;
import bam.bam.bam.views.fragment.ProfilFragment;
import bam.bam.bam.views.fragment.SendBamFragment;
import bam.bam.globalDisplay.views.tabs.TabsFragment;

/**
 * enum pour les fragments
 *
 * @author Marc
 */
public enum FragmentParams {

    PROFIL(ProfilFragment.class,R.string.profil_titre),
    TABS(TabsFragment.class,R.string.tabs_titre),
    SENDBAM(SendBamFragment.class,R.string.send_bam_titre),
    PROFIL_FIRST(ProfilFragment.class,R.string.profil_titre);

    /**
     * Framgnet à lancer lors d'un clic sur l'item, ou null si section
     */
    private final Class fragmentClass;

    /**
     * Nom associé  au titre de la page
     */
    private final int pageTitle;


    /**
     * Constructeur pour un élément
     *
     * @param fragmentClass framgnet à lancer
     * @param pageTitle titre de la page
     */
    FragmentParams(Class fragmentClass, int pageTitle) {
        this.fragmentClass = fragmentClass;
        this.pageTitle = pageTitle;
    }

    /**
     * classe de la fragment
     *
     * @return classe de la fragment
     */
    public Class getFragmentClass() {
        return fragmentClass;
    }

    /**
     * titre de la page dans la barre
     *
     * @return titre de la page dans la barre
     */
    public int getPageTitle() {
        return pageTitle;
    }
}

