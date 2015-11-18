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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.controllers.CallReciever;
import bam.bam.bam.dataBDD.ReponseDAO;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.controllers.refresher.Refresher;
import bam.bam.bam.views.adaptater.BamsEnvoyesAdaptater;
import bam.bam.bam.views.adaptater.BamsReponsesAdaptater;
import bam.bam.bam.views.alerts.AlertBamEnvSuppr;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Utility;

/**
 * fragment bams envoyes et réponses
 *
 * @author Marc
 */
public class BamsEnvoyesReponsesFragment extends Fragment {

    /**
     * activity de l'appli
     */
    private MainActivity activity;

    /**
     * si la page bams envoyés est visible
     */
    private boolean envVisible = true;

    /**
     * fragment bams envoyés
     */
    private RelativeLayout fragEnv;

    /**
     * fragment bams réponses
     */
    private RelativeLayout fragRep;

    /**
     * liste des bams envoyés
     */
    private RecyclerView rvEnv;

    /**
     * liste des réponses
     */
    private RecyclerView rvRep;

    /**
     * adaptateur liste des bams envoyés
     */
    private BamsEnvoyesAdaptater adpEnv;

    /**
     * adaptateur liste des réponses
     */
    private BamsReponsesAdaptater adpRep;

    /**
     * nombre de bams actifs envoyés
     */
    private TextView nombreBamTV;

    /**
     * prix du bam
     */
    private TextView tvPrix;

    /**
     * temps du bam
     */
    private TextView tvTime;

    /**
     * titre du bam
     */
    private TextView tvTitle;

    /**
     * description du bam
     */
    private TextView tvDesc;

    /**
     * dernier bam utilisé pour les réponses
     */
    private Bam lastBamRep = null;

    /**
     * nombre de bams envoyés actifs
     */
    private int nbBamsActs = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bam_envoyes_reponses, container, false);
        activity = (MainActivity)getActivity();
        CallReciever.setBerf(this);

        // frame bams envoyés
        rvEnv = (RecyclerView)v.findViewById(R.id.recyclerView);
        nombreBamTV = (TextView)v.findViewById(R.id.nbBAM);
        fragEnv = (RelativeLayout)v.findViewById(R.id.fragEnv);

        SwipeRefreshLayout swRLEnv = (SwipeRefreshLayout)v.findViewById(R.id.swRLEnv);
        swRLEnv.setOnRefreshListener(Refresher.getInstance());
        Refresher.getInstance().addswRL(swRLEnv);

        LinearLayoutManager lmEnv = new LinearLayoutManager(getActivity());
        lmEnv.setOrientation(LinearLayoutManager.VERTICAL);
        rvEnv.setLayoutManager(lmEnv);


        // frame bams réponses
        rvRep = (RecyclerView)v.findViewById(R.id.recyclerViewR);
        tvTitle = (TextView)v.findViewById(R.id.title);
        tvDesc = (TextView)v.findViewById(R.id.desc);
        tvPrix = (TextView)v.findViewById(R.id.prixRep);
        tvTime = (TextView)v.findViewById(R.id.timeRep);
        fragRep = (RelativeLayout)v.findViewById(R.id.fragRep);

        SwipeRefreshLayout swRLRep = (SwipeRefreshLayout)v.findViewById(R.id.swRLRep);
        swRLRep.setOnRefreshListener(Refresher.getInstance());
        Refresher.getInstance().addswRL(swRLRep);

        LinearLayoutManager lmRep = new LinearLayoutManager(getActivity());
        lmRep.setOrientation(LinearLayoutManager.VERTICAL);
        rvRep.setLayoutManager(lmRep);

        MaterialRippleLayout rippleLayout = (MaterialRippleLayout)v.findViewById(R.id.ripple);
        rippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lastBamRep != null && lastBamRep.getTime() == -1) {
                    InfoToast.display(true, activity.getString(R.string.bamExpire), activity);
                }
            }
        });

        rippleLayout.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                if (lastBamRep != null && lastBamRep.getTime() != -1) {
                    new AlertBamEnvSuppr(activity, lastBamRep,BamsEnvoyesReponsesFragment.this,true);
                } else {
                    InfoToast.display(true, activity.getString(R.string.bamExpire), activity);
                }

                return true;
            }
        });

        return v;
    }

    /**
     * charger la liste des bams envoyés
     *
     * @param user utilisateur
     * @param bams les bams envoyés
     * @param nbActifs nombre de bams actifs
     * @param nbRepsByItBam nombre de réponses par bams
     */
    public void loadAdpEnv(User user,List<Bam> bams,int nbActifs,Map<Integer,Integer> nbRepsByItBam) {

        if(adpEnv == null) {
            adpEnv = new BamsEnvoyesAdaptater(user,bams,getActivity(),this,nbRepsByItBam);
            rvEnv.setAdapter(adpEnv);
        }
        else
        {
            adpEnv.setNewList(user,bams,nbRepsByItBam);
        }

        nbBamsActs = nbActifs;
        nombreBamTV.setText(nbActifs + " " + getString(R.string.textEnvoyes));
    }

    /**
     * charger la liste des réponses
     *
     * @param users liste des réponses
     */
    public void loadAdpRep(List<User> users) {

        if (adpRep == null) {
            adpRep = new BamsReponsesAdaptater(users, lastBamRep, this);
            rvRep.setAdapter(adpRep);
        } else {
            adpRep.setNewList(users);
        }

        tvTitle.setText(lastBamRep.getBam_title());
        tvDesc.setText(lastBamRep.getBam_description());

        if(lastBamRep.getBam_price() != 0) {
            tvPrix.setText(Utility.formatDecimal(lastBamRep.getBam_price()) + " €");
            tvPrix.setTextColor(getResources().getColor(R.color.bamPayant));
        }
        else {
            tvPrix.setText(getString(R.string.gratuit));
            tvPrix.setTextColor(getResources().getColor(R.color.bamGratuit));
        }

        tvTime.setText(Utility.getStringTime(lastBamRep.getTime()));
    }

    /**
     * aller sur la page de bams envoyés
     *
     */
    public void frameBack()
    {
        fragEnv.setVisibility(View.VISIBLE);
        fragRep.setVisibility(View.GONE);
        activity.getTabsLayoutManager().setCrayonVisibility(View.VISIBLE);
        envVisible = true;
    }

    /**
     * aller sur la page de réponses
     *
     * @param bam le bam pour les réponses
     */
    public void  frameNext(Bam bam)
    {
        fragEnv.setVisibility(View.GONE);
        fragRep.setVisibility(View.VISIBLE);
        activity.getTabsLayoutManager().setCrayonVisibility(View.GONE);
        envVisible = false;
        lastBamRep = bam;

        loadListBamRepBDD();
    }

    /**
     * charger la liste des réponses à partir de la BDD interne
     */
    private void loadListBamRepBDD() {

        ReponseDAO reponseDAO = new ReponseDAO(activity);
        loadAdpRep(reponseDAO.getUsersRep(lastBamRep.getId()));
    }

    /**
     * avoir le bam utilisé pour la liste de réponses
     *
     * @return le bam utilisé pour la liste de réponses
     */
    public Bam getLastBamRep() {
        return lastBamRep;
    }

    /**
     * savoir si on est sur la liste envoyés ou réponses
     *
     * @return si on est sur la liste envoyés ou réponses
     */
    public boolean isEnvVisible()
    {
        return envVisible;
    }

    /**
     * décrémenter le nombre de bams recus
     */
    public void decNombreBamTV() {
        nbBamsActs--;
        nombreBamTV.setText(nbBamsActs + " " + getString(R.string.textEnvoyes));
    }

    /**
     * retourne le nombre de bams recus
     */
    public int getNombreBamTV() {
        return nbBamsActs;
    }

    public BamsEnvoyesAdaptater getAdpEnv() {
        return adpEnv;
    }
}