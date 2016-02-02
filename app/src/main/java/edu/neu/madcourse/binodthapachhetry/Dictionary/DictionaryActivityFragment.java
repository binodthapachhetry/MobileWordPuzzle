package edu.neu.madcourse.binodthapachhetry.Dictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.neu.madcourse.binodthapachhetry.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DictionaryActivityFragment extends Fragment {
    private AlertDialog mDialog;

    public DictionaryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dictionary, container, false);


        // buttons
        View acknowledgeButton = rootView.findViewById(R.id.button_acknowledgement);
        View returnButton = rootView.findViewById(R.id.button_main_menu);



        // acknowledgeButton
        acknowledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.dictionary_acknowledgement_message);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nothing
                            }
                        });
                mDialog = builder.show();
            }
        });

        // returnButton
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return rootView;
    }
}
