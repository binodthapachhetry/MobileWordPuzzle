package edu.neu.madcourse.binodthapachhetry.Communication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.neu.madcourse.binodthapachhetry.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CommunicationScraggleMainActivityFragment extends Fragment {

    private AlertDialog mDialog;

    public CommunicationScraggleMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_communication_scraggle_main, container, false);

        View newGameButton = rootView.findViewById(R.id.scraggle_new_game_button);
        View buttonRules = rootView.findViewById(R.id.scraggle_button_rules);
        View acknowledgementButton = rootView.findViewById(R.id.scraggle_acknowledgement_button);

        // New Game Button
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CommunicationScraggleGameActivity.class);
                getActivity().startActivity(intent);
            }
        });

        // Rules Button
        buttonRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CommunicationScraggleGameRules.class);
                startActivity(i);
            }
        });

        // Acknowledgement Button
        acknowledgementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.scraggle_acknowledgement_text);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                mDialog = builder.show();
            }
        });

        return rootView;
    }
}
