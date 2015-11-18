package bam.bam.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import bam.bam.R;
import bam.bam.globalDisplay.views.MainActivity;
import bam.bam.utilities.Utility;


/**
 * classe des notifications recus
 *
 * @author Marc
 */
public class NotificationRecus {

    private Context context;
    private String titre;
    private float prix;

    public NotificationRecus(Context context,String titre,float prix) {
        this.context = context;
        this.titre = titre;
        this.prix = prix;
    }

    /**
     * création de la notification
     */
    public void createNotification(){

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // classe à lancer après un clic sur la notif
        Intent intent = new Intent(context, MainActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("notification", true);
        b.putBoolean("first", false);
        intent.putExtras(b);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker(context.getString(R.string.notification))
                .setSmallIcon(R.mipmap.notif)
                .setAutoCancel(true)
                .setContentTitle(titre)
                .setContentIntent(pendingIntent);



        if(prix != 0) {
            builder.setContentText(Utility.formatDecimal(prix) + " €");
        }
        else {
            builder.setContentText(context.getString(R.string.gratuit));
        }

        if (Build.VERSION.SDK_INT >= 21) {
            builder.setColor(Color.BLACK);
        }

        //suivant la version android
        if (Build.VERSION.SDK_INT < 16) {
            nm.notify(1, builder.getNotification());
        } else {
            nm.notify(1, builder.build());
        }
    }

}
