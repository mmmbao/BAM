package bam.bam.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * classe qui active les notifs à l'allumage du téléphone
 *
 * @author Marc
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context,MyService.class);
            context.startService(serviceIntent);
        }
    }
}
