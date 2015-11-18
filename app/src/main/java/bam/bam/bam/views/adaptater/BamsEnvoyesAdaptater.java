package bam.bam.bam.views.adaptater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bam.bam.R;
import bam.bam.bam.dataBDD.ReponseDAO;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.alerts.AlertBamEnvSuppr;
import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Utility;

/**
 * Adaptateur des bams envoyes.
 *
 * @author Marc
 */
public class BamsEnvoyesAdaptater extends RecyclerView.Adapter<BamsEnvoyesAdaptater.ViewHolder> {

    /**
     * nombre de réponses par bam
     */
    Map<Integer,Integer> nbRepsByItBam;

    /**
     * tableau contenant les icones
     */
    private List<Bam> bams;

    /**
     * utilisateur
     */
    private User user;

    /**
     * context courant
     */
    private Context context;

    /**
     * fragment des bams envoyes / reponses
     */
    private BamsEnvoyesReponsesFragment berf;

    /**
     * classe ou on va chercher tout les objets du layout de la vue
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * le pseudo
         */
        TextView pseudo;

        /**
         * la photo
         */
        ImageView photo;

        /**
         * nombre de réponse
         */
        TextView nbRep;

        /**
         * temps restant
         */
        TextView time;

        /**
         * le layout pour griser le bam
         */
        LinearLayout llgrise;

        /**
         * le titre
         */
        TextView titre;

        /**
         * le prix
         */
        TextView prix;

        /**
         * la vue
         */
        View vue;

        public ViewHolder(View itemView,int ViewType) {
            super(itemView);

            vue = itemView;
            llgrise = (LinearLayout) itemView.findViewById(R.id.llgrise);
            pseudo = (TextView) itemView.findViewById(R.id.pseudo);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            nbRep = (TextView) itemView.findViewById(R.id.nbRep);
            time = (TextView) itemView.findViewById(R.id.time);
            titre = (TextView) itemView.findViewById(R.id.titre);
            prix = (TextView) itemView.findViewById(R.id.prix);
        }
    }


    public BamsEnvoyesAdaptater(User user, List<Bam> bams, Context context, BamsEnvoyesReponsesFragment berf,Map<Integer,Integer> nbRepsByItBam){
        this.bams = new ArrayList<>(bams);
        this.user = user;
        this.context = context;
        this.berf = berf;
        this.nbRepsByItBam = nbRepsByItBam;
    }

    /**
     * retourne le bon type de vue avec le bon layout
     *
     * @param parent parent
     * @param viewType type de vue
     * @return viewHolder
     */
    @Override
    public BamsEnvoyesAdaptater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bam_envoyes_item, parent, false);

        return new ViewHolder(v,viewType);
    }


    /**
     * met les valeurs dans les differents objets de la vue
     *
     * @param holder holder
     * @param position position de l'item
     */
    @Override
    public void onBindViewHolder(BamsEnvoyesAdaptater.ViewHolder holder, int position) {

        final Bam bam = bams.get(position);
        holder.pseudo.setText(user.getUser_pseudo());


        holder.photo.setImageBitmap(Utility.decodeBase64(user.getPhoto_data()));

        holder.vue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bam.getTime() != -1) {
                    berf.frameNext(bam);
                } else {
                    InfoToast.display(true, context.getString(R.string.bamExpire), context);
                }
            }
        });

        holder.vue.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                if (bam.getTime() != -1) {
                    new AlertBamEnvSuppr(context,bam,berf,false);
                } else {
                    InfoToast.display(true, context.getString(R.string.bamExpire), context);
                }

                return true;
            }
        });

        holder.time.setText(Utility.getStringTime(bam.getTime()));

        holder.titre.setText(bam.getBam_title());

        if(bam.getBam_price() != 0) {
            holder.prix.setText(Utility.formatDecimal(bam.getBam_price()) + " €");
            holder.prix.setTextColor(context.getResources().getColor(R.color.bamPayant));
        }
        else {
            holder.prix.setText(context.getString(R.string.gratuit));
            holder.prix.setTextColor(context.getResources().getColor(R.color.bamGratuit));
        }

        holder.nbRep.setText(String.valueOf(nbRepsByItBam.get(bam.getId())));

        if(bam.getTime() == -1)
        {
            holder.llgrise.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.llgrise.setVisibility(View.GONE);
        }
    }


    /**
     * obtenir le nombre d'items dans la liste
     *
     * @return  nombre d'items dans la liste
     */
    @Override
    public int getItemCount() {
        return bams.size();
    }


    /**
     * charger les nouveaux items
     *
     * @param user l'utilisateur
     * @param bams les bams envoyés
     * @param nbRepsByItBam nombre de réponses par bam
     */
    public void setNewList(User user,List<Bam> bams,Map<Integer,Integer> nbRepsByItBam) {
        this.user = user;
        this.bams = new ArrayList<>(bams);
        this.nbRepsByItBam = new HashMap<>(nbRepsByItBam);
        notifyDataSetChanged();
    }

    /**
     * supprimer un bam
     *
     * @param bam bam à enlever
     */
    public void annulerBam(Bam bam)
    {
        int nbBamActifs = berf.getNombreBamTV();
        int idx = bams.indexOf(bam);
        bam.setBam_State(2);
        int i;

        for(i = idx; i < nbBamActifs-1;i++)
        {
            bams.set(i,bams.get(i+1));
        }
        bams.set(i,bam);

        notifyDataSetChanged();
        berf.decNombreBamTV();
    }
}