package edu.neu.madcourse.binodthapachhetry.Communication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.neu.madcourse.binodthapachhetry.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CommunicationScraggleGameRulesFragment extends Fragment {

    public CommunicationScraggleGameRulesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rulesView = inflater.inflate(R.layout.fragment_scraggle_game_rules, container, false);

        return rulesView;
    }
}
