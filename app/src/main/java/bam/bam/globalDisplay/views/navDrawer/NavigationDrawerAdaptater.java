package bam.bam.globalDisplay.views.navDrawer;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import bam.bam.globalDisplay.FragmentParams;
import bam.bam.R;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.globalDisplay.NavDrawerParams;

/**
 * Adaptateur du navigation drawer.
 *
 * @author Marc
 */
public class NavigationDrawerAdaptater extends RecyclerView.Adapter<NavigationDrawerAdaptater.ViewHolder> {

    /**
     * pour savoir si le type de la vue est un header
     */
    private static final int TYPE_HEADER = 0;
    /**
     * pour savoir si le type de la vue est un item
     */
    private static final int TYPE_ITEM = 1;
    /**
     * pour savoir si le type de la vue est un titre
     */
    private static final int TYPE_TITLE = 2;

    /**
     * Ensemble des éléments de menu, sauf le header
     */
    private final NavDrawerParams[] menusItem = NavDrawerParams.values();

    /**
     * activity globale
     */
    private MainActivity activity;

    /**
     * classe qui va chercher tout les objets du layout de la vue correspondante
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        View vue;
        TextView textView;
        ImageView imageView;
        TextView title;


        public ViewHolder(View itemView,int ViewType) {
            super(itemView);


            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                vue = itemView;
                Holderid = 1;
            }

            if(ViewType == TYPE_HEADER) {
                Holderid = 0;
            }

            if(ViewType == TYPE_TITLE) {
                title = (TextView) itemView.findViewById(R.id.title);
                Holderid = 2;
            }
        }


    }


    public NavigationDrawerAdaptater(MainActivity activity){
        this.activity = activity;
    }

    /**
     * retourne le bon type de vue avec le bon layout
     *
     * @param parent parent
     * @param viewType type de vue
     * @return viewHolder
     */
    @Override
    public NavigationDrawerAdaptater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item,parent,false);

            return new ViewHolder(v,viewType);
        }

        if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header,parent,false);

            return new ViewHolder(v,viewType);
        }

        if (viewType == TYPE_TITLE) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_title,parent,false);

            return new ViewHolder(v,viewType);
        }

        return null;

    }


    /**
     * met les valeurs dans les differents objets de la vue
     *
     * @param holder holder
     * @param position position de l'item
     */
    @Override
    public void onBindViewHolder(final NavigationDrawerAdaptater.ViewHolder holder, final int position) {

        final NavDrawerParams fParams = getNavDrawerParams(position);

        // Si on n'a pas trouvé de menu, inutile de continuer
        if (fParams == null) {
            return;
        }

        if(holder.Holderid ==1) {

            holder.textView.setText(fParams.getNavTitle());
            holder.imageView.setImageResource(fParams.getIconRes());

            holder.vue.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (position == FragmentParams.TABS.ordinal() + 1)
                            activity.loadFragment(position - 1, true, activity.getString(FragmentParams.values()[position-1].getPageTitle()));
                        else
                            activity.loadFragment(position - 1, false, activity.getString(FragmentParams.values()[position-1].getPageTitle()));
                    }

                    if (event.getAction() == MotionEvent.ACTION_CANCEL && !(position == activity.getCurrent_fragment_pos() + 1)) {
                        holder.vue.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        holder.vue.setBackgroundColor(activity.getResources().getColor(R.color.itemSelect));
                    }

                    return false;
                }
            });


            if(activity.getCurrent_fragment_pos() != FragmentParams.SENDBAM.ordinal()) {
                if (position == activity.getCurrent_fragment_pos() + 1) {
                    holder.vue.setBackgroundColor(activity.getResources().getColor(R.color.itemSelect));
                } else {
                    holder.vue.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }

        if(holder.Holderid ==2) {
            holder.title.setText(fParams.getNavTitle());
        }
    }

    /**
     * obtenir le nombre d'items dans la liste
     *
     * @return  nombre d'items dans la liste
     */
    @Override
    public int getItemCount() {
        return menusItem.length + 1;
    }

    /**
     * avoir le type de la view
     *
     * @param position position de la vue
     * @return type de la view
     */
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        if (isPositionTitle(position))
            return TYPE_TITLE;

        return TYPE_ITEM;
    }

    /**
     * Indique si l'élément à la position donnée correspond à l'Header
     *
     * @param position position de l'élément
     * @return TRUE si la position est celle du header
     */
    private boolean isPositionHeader(int position) {
        // Le header est à la position 0
        return position == 0;
    }

    /**
     * Indique si l'élément d'une position donnée est un titre (section)
     *
     * @param position position de l'élément
     * @return TRUE si l'éléement est un titre de section
     */
    private boolean isPositionTitle(int position) {
        return getNavDrawerParams(position).isSection();
    }

    /**
     * obtenir les paramètres d'une fragment
     *
     * @param position position de la fragment
     * @return paramètres de la fragment
     */
    private NavDrawerParams getNavDrawerParams(int position) {
        if (position > 0) {
            // -1 car on a un élément de plus dans le drawer au début(le drawer)
            return menusItem[position - 1];
        }
        // null retourné si position = 0 = header
        return null;
    }
}