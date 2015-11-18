package bam.bam.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * classe utilitaire
 *
 * @author Marc
 */
public final class Utility {

    /**
     * convertisseur dp/pixel
     *
     * @param dp dp
     * @param context le context
     * @return pixel
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * le format decmal
     *
     * @param nombre le float
     * @return le float formaté (String)
     */
    public static String formatDecimal(float nombre) {
        NumberFormat df = new DecimalFormat("#.##");
        return df.format(nombre).replace(',','.');
    }

    /**
     * convertir Date en String
     *
     * @param date la date Date
     * @return la date String
     */
    public static String dateToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    /**
     * convertir String en Date
     *
     * @param date la date String
     * @return la date Date
     */
    public static Date stringToDate (String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return df.parse(date);
        } catch (ParseException e) {
        }

        return new Date();
    }


    /**
     * obtenir le temps
     *
     * @param time temps en seconde
     * @return le temps
     */
    public static String getStringTime(int time) {

        String tps = "";
        int res;

        if(time == -1)
            return "Fini !";

        res = time / 60;
        if ( res < 1) {
            tps = String.valueOf(time) + " s";
        }
        else
        {
            time = res;
            res = time / 60;
            if ( res < 1) {
                tps = String.valueOf(time) + " mn";
            }
            else
            {
                time = res;
                res = time / 24;
                if ( res < 1) {
                    tps = String.valueOf(time) + " h";
                }
                else
                {
                    time = res;
                    res = time / 30;
                    if ( res < 1) {
                        tps = String.valueOf(time) + " d";
                    }
                    else
                    {
                        time = res;
                        res = time / 12;
                        if ( res < 1) {
                            tps = String.valueOf(time) + " mth";
                        }
                        else
                        {
                            tps = String.valueOf(res) + " y";
                        }
                    }
                }
            }
        }


        return tps;
    }

    /**
     * convertir base64 en bitmap
     *
     * @param image en base64
     * @return bitmap
     */
    public static String encodeTobase64(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b,Base64.DEFAULT);
    }


    /**
     *  convertir du bitmap en base64
     *
     * @param input image bitmap
     * @return base64
     */
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
        ByteArrayInputStream imageStream = new ByteArrayInputStream(decodedByte);

        return BitmapFactory.decodeStream(imageStream);
    }

    /**
     * obtenir l'id du téléphone
     *
     * @param context le context
     * @return l'id du téléphone
     */
    public static String getPhoneId(Context context)
    {
        return  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
