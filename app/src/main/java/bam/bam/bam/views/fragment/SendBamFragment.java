package bam.bam.bam.views.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import bam.bam.R;
import bam.bam.bam.controllers.enregistrements.EnregistrementSendBam;
import bam.bam.globalDisplay.views.MainActivity;

/**
 * fragment envoyer un bam
 *
 * @author Marc
 */
public class SendBamFragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Creation du layout
        View view = inflater.inflate(R.layout.fragment_send_bam, container, false);


        EditText titre = (EditText)view.findViewById(R.id.titre);
        EditText desc = (EditText)view.findViewById(R.id.desc);
        EditText prix = (EditText)view.findViewById(R.id.prix);

        prix.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String prix = s.toString();

                if (prix.length() >= 5 && !prix.contains(".")) {
                    s = s.delete(4,prix.length());
                }
            }
        });


        Button btn = (Button) view.findViewById(R.id.envoyerBam);
        btn.setOnClickListener(new EnregistrementSendBam(titre,desc,prix,(MainActivity)getActivity()));

        return view;
    }
}