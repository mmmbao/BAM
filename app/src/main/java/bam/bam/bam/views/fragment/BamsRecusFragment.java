package bam.bam.bam.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.controllers.refresher.Refresher;
import bam.bam.bam.views.adaptater.BamsRecusAdaptater;

/**
 * fragment bams recus
 *
 * @author Marc
 */
public class BamsRecusFragment extends Fragment {

    /**
     * liste des bams recus
     */
    private RecyclerView rv;

    /**
     * nombre de bams recus
     */
    private TextView nombreBamTV;

    /**
     * adaptateur liste des bams recus
     */
    private BamsRecusAdaptater adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_bam_recus,container,false);


        rv = (RecyclerView)v.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(lm);
        nombreBamTV = (TextView)v.findViewById(R.id.nbBAM);

        SwipeRefreshLayout swRL = (SwipeRefreshLayout)v.findViewById(R.id.swRL);
        swRL.setOnRefreshListener(Refresher.getInstance());
        Refresher.getInstance().addswRL(swRL);

        return v;
    }

    /**
     * charger la liste des bams recus
     *
     * @param bams les bams recus
     * @param bamUsers les utilisateurs ayant envoyés les bams
     */
    public void loadAdpRec(List<Bam> bams,Map<Bam, User> bamUsers)
    {
        if(adapter == null) {
            adapter = new BamsRecusAdaptater(bamUsers, bams,this);
            rv.setAdapter(adapter);
        }
        else
        {
            adapter.setNewList(bamUsers,bams);
        }

        nombreBamTV.setText(bams.size() + " " + getString(R.string.textRecus));
    }

    /**
     * mettre le nombre de bams recus
     *
     * @param nb nombre de bams recus
     */
    public void setNombreBamTV(int nb) {
        nombreBamTV.setText(nb + " " + getString(R.string.textRecus));
    }
}

