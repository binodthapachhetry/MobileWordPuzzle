package edu.neu.madcourse.binodthapachhetry.Communication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import edu.neu.madcourse.binodthapachhetry.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CommunicationScraggleHomeFragment extends Fragment {
    private Context context;
    private Resources resources;
    RemoteClient remoteClient;


    public CommunicationScraggleHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        resources = getResources();
//        final Context contextOnCreate = getContext();
        remoteClient = new RemoteClient(this.getContext());

        View rootView = inflater.inflate(R.layout.fragment_communication_scraggle_home, container, false);


        final EditText regUserName = (EditText) rootView.findViewById(R.id.registeredusertextBox);
        final View regUserButton = rootView.findViewById(R.id.registered_user_button);

        final EditText newUserName = (EditText) rootView.findViewById(R.id.newusertextBox);
        View newUserButton = rootView.findViewById(R.id.new_user_button);

        regUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(regUserName.getText().length() != 0){
                    remoteClient.fetchValue(regUserName.getText().toString());
                    // check if username exists; in firebase check if key present
                    // if present get the value(regID)
                    // if not present let the user know that using dialog box

                }
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newUserName.getText().length() != 0){


                    // check if username already exists
                    // if present let the user know that using dialog box; and ask a new username
                    // if not present do the registration using the username as key and regid as value


//                    remoteClient.saveValue(newUserName.getText().toString(), value.getText().toString());
                }
            }
        });



        return rootView;
    }
}
