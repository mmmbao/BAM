package bam.bam.globalDisplay;

import bam.bam.R;
import bam.bam.bam.views.fragment.ProfilFragment;
import bam.bam.globalDisplay.views.tabs.TabsFragment;

/**
 * enum pour le navDrawer
 *
 * @author Marc
 */
public enum NavDrawerParams {

    PROFIL(R.string.profil_titre, R.mipmap.compte, ProfilFragment.class),
    TABS(R.string.bam_recus_envoyes_titre, R.mipmap.summary,TabsFragment.class);


    /**
     * Nom associé  à l'élément de menu ou titre de la section.
     */
    private final int navTitle;

    /**
     * Identifiant de l'image associée, ou -1 si non applicable
     */
    private final int iconRes;

    /**
     * Framgnet à lancer lors d'un clic sur l'item, ou null si section
     */
    private final Class fragmentClass;

    /**
     * Constructeur pour un élément de type section
     *
     * @param title titre de la section
     */
    NavDrawerParams(int title) {
        this(title, -1, null);
    }

    /**
     * Constructeur pour un élément cliquable
     *
     * @param navTitle titre du menu item
     * @param iconRes identifiant de la ressource image associée
     * @param fragmentClass framgnet à lancer
     */
    NavDrawerParams(int navTitle, int iconRes, Class fragmentClass) {
        this.navTitle = navTitle;
        this.iconRes = iconRes;
        this.fragmentClass = fragmentClass;
    }

    /**
     * titre de la page dans le navigation drawer
     *
     * @return titre de la page dans le navigation drawer
     */
    public int getNavTitle() {
        return navTitle;
    }

    /**
     * icon de la page dans le navigation drawer
     *
     * @return icon de la page dans le navigation drawer
     */
    public int getIconRes() {
        return iconRes;
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
     * savoir si l'item est une section ou nom
     *
     * @return si l'item est une section ou nom
     */
    public boolean isSection() {
        return fragmentClass == null;
    }
}

