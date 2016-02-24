package edu.neu.madcourse.binodthapachhetry.Scraggle;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.neu.madcourse.binodthapachhetry.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScraggleGameRulesFragment extends Fragment {

    public ScraggleGameRulesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rulesView = inflater.inflate(R.layout.fragment_scraggle_game_rules, container, false);

        return rulesView;
    }
}
