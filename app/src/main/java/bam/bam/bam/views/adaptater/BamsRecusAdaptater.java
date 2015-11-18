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
import bam.bam.bam.controllers.verifications.VerifValidation;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.fragment.BamsRecusFragment;
import bam.bam.utilities.Utility;

/**
 * Adaptateur des bams recus.
 *
 * @author Marc
 */
public class BamsRecusAdaptater extends RecyclerView.Adapter<BamsRecusAdaptater.ViewHolder> {

    /**
     * liste des bams
     */
    private List<Bam> bams;

    /**
     * savoir si le bam est expand
     */
    private Map<Integer,Boolean> expand;

    /**
     * user de chaque bam
     */
    private Map<Bam,User> bamUsers;

    /**
     * context courant
     */
    private Context context;

    /**
     * fragment des bams ecus
     */
    private BamsRecusFragment brf;


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
         * la desciption
         */
        TextView desc;

        /**
         * temps restant
         */
        TextView time;

        /**
         * le titre
         */
        TextView titre;

        /**
         * le prix
         */
        TextView prix;

        /**
         * image pour accepter un bam
         */
        ImageView ok;

        /**
         * image pour refuser un bam
         */
        ImageView ko;

        /**
         * le layout pour mettre une taille fixe ou variable
         */
        LinearLayout linear;

        /**
         * le boutonCrayon
         */
        LinearLayout boutons;

        /**
         * la vue
         */
        View vue;

        public ViewHolder(View itemView,int ViewType) {
            super(itemView);

            vue = itemView;
            pseudo = (TextView) itemView.findViewById(R.id.pseudo);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            desc = (TextView) itemView.findViewById(R.id.desc);
            time = (TextView) itemView.findViewById(R.id.time);
            titre = (TextView) itemView.findViewById(R.id.titre);
            prix = (TextView) itemView.findViewById(R.id.prix);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
            ok = (ImageView) itemView.findViewById(R.id.ok);
            ko = (ImageView) itemView.findViewById(R.id.ko);
            boutons = (LinearLayout) itemView.findViewById(R.id.boutons);
        }
    }


    public BamsRecusAdaptater(Map<Bam, User> bamUsers, List<Bam> bams,BamsRecusFragment brf){
        this.brf = brf;
        this.bamUsers = new HashMap<>(bamUsers);
        this.bams = new ArrayList<>(bams);
        this.context = brf.getActivity();
        this.expand = new HashMap<>();
    }

    /**
     * retourne le bon type de vue avec le bon layout
     *
     * @param parent parent
     * @param viewType type de vue
     * @return viewHolder
     */
    @Override
    public BamsRecusAdaptater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bam_recus_item,parent,false);

        return new ViewHolder(v,viewType);
    }


    /**
     * met les valeurs dans les differents objets de la vue
     *
     * @param holder holder
     * @param position position de l'item
     */
    @Override
    public void onBindViewHolder(final BamsRecusAdaptater.ViewHolder holder, final int position) {

        final Bam bam = bams.get(position);
        User user = bamUsers.get(bam);

        holder.pseudo.setText(user.getUser_pseudo());
        holder.photo.setImageBitmap(Utility.decodeBase64(user.getPhoto_data()));
        holder.desc.setText(bam.getBam_description());


        holder.ko.setOnClickListener(new VerifValidation(context, bam,
                context.getResources().getString(R.string.refuser_bam),
                R.mipmap.ko, this));

        holder.ok.setOnClickListener(new VerifValidation(context, bam,
                context.getResources().getString(R.string.accepter_bam),
                R.mipmap.ok, this));

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

        setLinearExpandClose(holder, position);

        expand.put(position, false);
    }

    /**
     * mettre le bam expand ou non
     *
     * @param holder holder
     * @param position position de l'item
     */
    private void setLinearExpandClose(final ViewHolder holder, final int position) {

        setCloseParams(holder, position);

        holder.vue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expand.get(position)) {
                    setCloseParams(holder, position);
                } else {
                    setExpandParams(holder, position);
                }
            }
        });
    }

    /**
     * paramètre d'un bam en mode expand
     *
     * @param holder holder
     * @param position la position
     */
    public void setExpandParams(ViewHolder holder, int position)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        holder.linear.setLayoutParams(layoutParams);

        holder.titre.setMaxLines(Integer.MAX_VALUE);
        holder.desc.setVisibility(View.VISIBLE);
        holder.boutons.setVisibility(View.VISIBLE);
        expand.remove(position);
        expand.put(position, true);
    }

    /**
     * paramètre d'un bam en mode fermé
     *
     * @param holder holder
     * @param position la position
     */
    public void setCloseParams(ViewHolder holder, int position)
    {
        int dp70 = (int) Utility.convertDpToPixel(70, context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp70);
        holder.linear.setLayoutParams(layoutParams);

        holder.titre.setMaxLines(2);
        holder.desc.setVisibility(View.GONE);
        holder.boutons.setVisibility(View.GONE);
        expand.remove(position);
        expand.put(position, false);
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

    public void setNewList(Map<Bam, User> bamUsers, List<Bam> bams) {
        this.bamUsers = new HashMap<>(bamUsers);
        this.bams = new ArrayList<>(bams);
        this.expand = new HashMap<>();
        notifyDataSetChanged();
    }

    /**
     * supprimer un bam
     *
     * @param bam bam à enlever
     */
    public void removeBam(Bam bam)
    {
        bams.remove(bam);
        bamUsers.remove(bam);
        notifyDataSetChanged();
        brf.setNombreBamTV(bams.size());
    }
}