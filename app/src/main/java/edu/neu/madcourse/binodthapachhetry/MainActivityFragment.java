package edu.neu.madcourse.binodthapachhetry;

import android.support.v4.app.Fragment;
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

    private ArrayAdapter<String> mItemAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // List of items in the main menu
        String[] itemArray = {"About","GenerateError","TicTacToe","Quit"};
        ArrayList<String> allItems = new ArrayList<String>(Arrays.asList(itemArray));

        // Creating an ArrayAdapter
        mItemAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item,
                        R.id.list_item_textview,
                        allItems);
        // Get a reference to the ListView, and attach this adapter
        ListView listView = (ListView) rootView.findViewById(R.id.listview_item);
        listView.setAdapter(mItemAdapter);

        return rootView;
    }
}
