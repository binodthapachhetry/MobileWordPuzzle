package edu.neu.madcourse.binodthapachhetry;


import android.app.AlertDialog;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private AlertDialog mDialog;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        // Tic Tac Toe Button
        View tttButton = rootView.findViewById(R.id.tictactoe_button);
        tttButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),edu.neu.madcourse.binodthapachhetry.tictactoe.MainActivity.class);
                getActivity().startActivity(i);
            }
        });


        // About button
        View aboutButton = rootView.findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(getActivity(), AboutActivity.class);
                startActivity(j);
            }
        });

        // Quit button
        View quitButton = rootView.findViewById(R.id.quit_button);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.quit_text);

                builder.setPositiveButton(R.string.yes_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,
                                                int i) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        });

                builder.setNegativeButton(R.string.no_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,
                                                int i) {
                                // nothing
                            }
                        });
                mDialog = builder.show();
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Get rid of the about dialog if it's still up
        if (mDialog != null)
            mDialog.dismiss();
    }
}
