package bam.bam.globalDisplay.views.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bam.bam.R;

/**
 * fragment vide en fond pour les tabs
 *
 * @author Marc
 */
public class TabsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Creation du layout
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }
}
