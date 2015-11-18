package bam.bam.bam.views.alerts;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import bam.bam.R;
import bam.bam.bam.views.fragment.ProfilFragment;

/**
 * class alert pour supprimer la photo
 *
 * @author Marc
 */
public class AlertPhoto extends AlertDialog.Builder {


    public AlertPhoto(final ProfilFragment fragment, final ImageView image) {
        super(fragment.getActivity());
        Context context = fragment.getActivity();


        setCancelable(false);
        setTitle(context.getString(R.string.photo));
        setMessage(context.getString(R.string.supprPhoto));

        setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() { // bouton Ok
            public void onClick(DialogInterface dialog, int which) {
                image.setImageResource(R.mipmap.profil);
                fragment.setPhotoChange(true);
            }
        });

        setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() { // bouton annuler
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        show();
    }
}
