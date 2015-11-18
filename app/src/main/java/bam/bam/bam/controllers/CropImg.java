package bam.bam.bam.controllers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import bam.bam.R;
import bam.bam.bam.views.fragment.ProfilFragment;
import bam.bam.utilities.InfoToast;

/**
 * classe redimensionnement de l'image
 *
 * @author Marc
 */
public class CropImg {

    /**
     * la fragment
     */
    private ProfilFragment fragment;

    /**
     * le context
     */
    private Context context;

    /**
     * les données
     */
    private Intent data;

    public CropImg(ProfilFragment fragment, Intent data) {
        this.context = fragment.getActivity();
        this.fragment = fragment;
        this.data = data;
    }

    /**
     * lancer le crop
     */
    public void execute()
    {
        Uri selectedImage = data.getData();


        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(selectedImage, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 200);
            cropIntent.putExtra("outputY", 200);
            cropIntent.putExtra("scaleUpIfNeeded", true);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            fragment.startActivityForResult(cropIntent, ProfilFragment.PIC_CROP);

        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = context.getString(R.string.resizeMessage);
            InfoToast.display(false, errorMessage, context);
        }
    }
}
