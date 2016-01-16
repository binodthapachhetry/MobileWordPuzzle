package edu.neu.madcourse.binodthapachhetry;


import android.app.AlertDialog;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

//    private ArrayAdapter<String> mItemAdapter;

    private AlertDialog mDialog;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Handle buttons here
        View aboutButton = rootView.findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.about_title);
                builder.setMessage(R.string.about_text);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label,
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



        // List of items in the main menu
//        String[] itemArray = {"About","GenerateError","TicTacToe","Quit"};
//        ArrayList<String> allItems = new ArrayList<String>(Arrays.asList(itemArray));
//
//        // Creating an ArrayAdapter
//        mItemAdapter =
//                new ArrayAdapter<String>(
//                        getActivity(),
//                        R.layout.list_item,
//                        R.id.list_item_textview,
//                        allItems);
//        // Get a reference to the ListView, and attach this adapter
//        ListView listView = (ListView) rootView.findViewById(R.id.listview_item);
//        listView.setAdapter(mItemAdapter);

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
