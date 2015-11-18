package bam.bam.bam.views.adaptater;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bam.bam.R;
import bam.bam.bam.controllers.CallReciever;
import bam.bam.bam.dataWS.AppelJSONParser;
import bam.bam.bam.modeles.Bam;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;
import bam.bam.utilities.InfoToast;
import bam.bam.utilities.Utility;

/**
 * Adaptateur des bams reponses.
 *
 * @author Marc
 */
public class BamsReponsesAdaptater extends RecyclerView.Adapter<BamsReponsesAdaptater.ViewHolder> {


    /**
     * liste des users
     */
    private List<User> users;

    /**
     * context courant
     */
    private Context context;

    /**
     * gestion des appel
     */
    private AppelJSONParser appelJSONParser;

    /**
     * bam utilisé
     */
    private Bam bam;

    /**
     * fragment des bams envoyés / réponses
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
         * le téléphone
         */
        ImageView tel;

        /**
         * la vue
         */
        View vue;

        public ViewHolder(View itemView,int ViewType) {
            super(itemView);

            vue = itemView;
            pseudo = (TextView) itemView.findViewById(R.id.pseudo);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            tel = (ImageView) itemView.findViewById(R.id.tel);
        }
    }


    public BamsReponsesAdaptater(List<User> users,Bam bam, BamsEnvoyesReponsesFragment berf){
        this.users = new ArrayList<>(users);
        this.context = berf.getActivity();
        this.berf = berf;
        this.appelJSONParser = new AppelJSONParser(context);
        this.bam = bam;
    }

    /**
     * retourne le bon type de vue avec le bon layout
     *
     * @param parent parent
     * @param viewType type de vue
     * @return viewHolder
     */
    @Override
    public BamsReponsesAdaptater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bam_reponses_item,parent,false);

        return new ViewHolder(v,viewType);
    }


    /**
     * met les valeurs dans les differents objets de la vue
     *
     * @param holder holder
     * @param position position de l'item
     */
    @Override
    public void onBindViewHolder(final BamsReponsesAdaptater.ViewHolder holder, final int position) {

        holder.pseudo.setText(users.get(position).getUser_pseudo());
        holder.photo.setImageBitmap(Utility.decodeBase64(users.get(position).getPhoto_data()));
        holder.tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bam.getTime() != -1) { // si le bam a pas expiré, on lance l'appel
                    CallReciever.setLastBamCall(bam);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + users.get(position).getUser_phone_number()));
                    context.startActivity(callIntent);
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            appelJSONParser.setAppel(users.get(position).getId(), bam.getBam_user_id(), bam.getId());
                            return null;
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }
                else
                {
                    InfoToast.display(true, context.getString(R.string.bamExpire),context);
                    berf.frameBack();
                }
            }
        });

    }

    /**
     * obtenir le nombre d'items dans la liste
     *
     * @return  nombre d'items dans la liste
     */
    @Override
    public int getItemCount() {
        return users.size();
    }


    /**
     * charger les nouveaux items
     *
     * @param users les utilisateurs
     */
    public void setNewList(List<User> users) {
        this.users = new ArrayList<>(users);
        notifyDataSetChanged();
    }
}