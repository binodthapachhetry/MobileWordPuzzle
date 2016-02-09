package edu.neu.madcourse.binodthapachhetry.Dictionary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import edu.neu.madcourse.binodthapachhetry.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DictionaryActivityFragment extends Fragment implements View.OnClickListener {
    private AlertDialog mDialog;
    private Resources resources;
    private Context context;
    EditText editText;
    TextView textView;
    Trie tt = null;
    public List<String> stringVisited = new ArrayList<String>();


    public DictionaryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
        final Context contextOnCreate = getContext();

        View rootView = inflater.inflate(R.layout.fragment_dictionary, container, false);

        editText = (EditText) rootView.findViewById(R.id.textBox);
        editText.setOnClickListener(this);

        textView = (TextView) rootView.findViewById(R.id.textViewBox);
        textView.setMovementMethod(new ScrollingMovementMethod());

        final MediaPlayer mp = MediaPlayer.create(contextOnCreate,R.raw.dictionary_match);
        mp.setVolume(0.5f, 0.5f);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if ((s.length() == 0)){
                    tt = null;
//                    textView.setText("");

                }else if (!(stringVisited.contains(s.toString()))) {
                    if (s.length()>2) {
                        if (tt.contains(s.toString())) {
                            textView.append(s + "\n");
                            mp.start();
                            stringVisited.add(s.toString());
                        }
                    } else if (s.length() == 2) {


                        tt = new Trie();


                        try {
                            String fileName = s.toString() + ".txt";
                            AssetManager am = contextOnCreate.getAssets();
                            InputStream ins = null;
                            ins = am.open(fileName);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
                            String line;
                            try {
                                while ((line = reader.readLine()) != null) {
                                    tt.add(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                    }
                }


            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // buttons
        View acknowledgeButton = rootView.findViewById(R.id.button_acknowledgement);
        View returnButton = rootView.findViewById(R.id.button_main_menu);
        View clearButton = rootView.findViewById(R.id.button_clear);

        // clearButton
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.getText().clear();
                textView.setText("");
                stringVisited.clear();
            }
        });



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

    @Override
    public void onClick(View v) {
    }
}
