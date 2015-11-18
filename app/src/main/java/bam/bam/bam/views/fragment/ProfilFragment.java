package bam.bam.bam.views.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import bam.bam.R;
import bam.bam.bam.controllers.CropImg;
import bam.bam.bam.controllers.enregistrements.EnregistrementProfil;
import bam.bam.bam.dataBDD.UserDAO;
import bam.bam.bam.modeles.User;
import bam.bam.bam.views.alerts.AlertPhoto;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.utilities.Utility;

/**
 * fragment profil
 *
 * @author Marc
 */
public class ProfilFragment extends Fragment
{

    /**
     * valeur pour la gallery
     */
    public static int RESULT_LOAD_IMAGE = 1;

    /**
     * valeur pour le crop
     */
    public static int PIC_CROP = 2;

    /**
     * la photo
     */
    private ImageView image;

    /**
     * si on a changé la photo
     */
    private boolean photoChange = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Creation du layout
        final View view = inflater.inflate(R.layout.fragment_profil, container, false);

        MainActivity act = ((MainActivity)getActivity());

        // gestion de l'image
        image = (ImageView)view.findViewById(R.id.photo);
        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        image.setOnLongClickListener(new View.OnLongClickListener() {


            @Override
            public boolean onLongClick(View v) {
                new AlertPhoto(ProfilFragment.this,image);

                return true;
            }
        });

        EditText tel = (EditText) view.findViewById(R.id.tel);
        EditText pseudoET = (EditText) view.findViewById(R.id.pseudoET);
        TextView pseudoTV = (TextView) view.findViewById(R.id.pseudoTV);

        Button btn = (Button) view.findViewById(R.id.saveProfil);
        btn.setOnClickListener(new EnregistrementProfil(this,act,image,tel,pseudoET));

        if(!act.isFirst()) // si c'est pour une modification de profil
        {
            User user = new UserDAO(act).getUserByDevice(Utility.getPhoneId(act));
            image.setImageBitmap(Utility.decodeBase64(user.getPhoto_data()));

            pseudoTV.setText(user.getUser_pseudo());
            pseudoTV.setVisibility(View.VISIBLE);
            pseudoET.setVisibility(View.GONE);

            tel.setText(user.getUser_phone_number());
        }

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && null != data) {

            // si on clic sur une image
            if (requestCode == RESULT_LOAD_IMAGE) {
                new CropImg(this,data).execute();
            }

            // si on valide le redimensionnement
            if(requestCode == PIC_CROP)
            {
                photoChange = true;
                Bundle extras = data.getExtras();

                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");

                //display the returned cropped image
                image.setImageBitmap(thePic);
            }
        }
    }

    public void setPhotoChange(boolean photoChange) {
        this.photoChange = photoChange;
    }

    public boolean isPhotoChange() {
        return photoChange;
    }
}
