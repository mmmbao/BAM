package bam.bam.globalDisplay.views.toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;
import bam.bam.bam.views.fragment.BamsRecusFragment;

/**
 * gestion des fragments des tabs
 *
 * @author Marc
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * titre des tabs
     */
    private CharSequence Titles[];

    /**
     * nombre de tab
     */
    private int NumbOfTabs;

    /**
     * tab bams recus
     */
    BamsRecusFragment tabR;

    /**
     * tab bams envoyés / réponses
     */
    BamsEnvoyesReponsesFragment tabER;


    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        tabR = new BamsRecusFragment();
        tabER = new BamsEnvoyesReponsesFragment();
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    /**
     * avoir le bon fragment pour le tab
     *
     * @param position position du tabs
     * @return bon fragment pour le tab
     */
    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            return tabR;
        }
        else
        {
            return tabER;
        }


    }

    /**
     * titre du tabs
     *
     * @param position postion du tabs
     * @return titre du tabs
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    /**
     * nombre de tabs
     * @return nombre de tabs
     */
    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}