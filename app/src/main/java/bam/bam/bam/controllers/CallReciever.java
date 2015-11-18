package bam.bam.bam.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import bam.bam.bam.modeles.Bam;
import bam.bam.bam.views.fragment.BamsEnvoyesReponsesFragment;

/**
 * classe qui gère les appels
 *
 * @author Marc
 */
public class CallReciever extends BroadcastReceiver {

    /**
     * fragment de la liste réponses
     */
    private static BamsEnvoyesReponsesFragment berf;

    /**
     * le bam pour lequel on appelle
     */
    private static Bam lastBamCall = null;

    @Override
    public void onReceive(Context context, Intent intent) {

        // quand on raccroche
        if (lastBamCall != null && intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE))
        {
            if(lastBamCall.getTime() != -1) // si le bam a expiré, on va sur la liste des bams envoyéss
                berf.frameBack();
        }

        lastBamCall = null;
    }

    /**
     * mettre la fragment
     *
     * @param b la fragment
     */
    public static void setBerf(BamsEnvoyesReponsesFragment b) {
        berf = b;
    }

    /**
     * mettre le bam pour lequel on appelle
     *
     * @param lastBamCall le bam pour lequel on appelle
     */
    public static void setLastBamCall(Bam lastBamCall) {
        CallReciever.lastBamCall = lastBamCall;
    }
}
