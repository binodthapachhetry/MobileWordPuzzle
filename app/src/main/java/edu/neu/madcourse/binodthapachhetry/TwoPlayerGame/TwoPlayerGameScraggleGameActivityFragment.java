package edu.neu.madcourse.binodthapachhetry.TwoPlayerGame;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import edu.neu.madcourse.binodthapachhetry.Dictionary.Trie;
import edu.neu.madcourse.binodthapachhetry.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class TwoPlayerGameScraggleGameActivityFragment extends Fragment {
    static private int mLargeIds[] = {R.id.scraggle_large1, R.id.scraggle_large2, R.id.scraggle_large3,
            R.id.scraggle_large4, R.id.scraggle_large5, R.id.scraggle_large6, R.id.scraggle_large7, R.id.scraggle_large8,
            R.id.scraggle_large9,};
    static private int mSmallIds[] = {R.id.scraggle_small1, R.id.scraggle_small2, R.id.scraggle_small3,
            R.id.scraggle_small4, R.id.scraggle_small5, R.id.scraggle_small6, R.id.scraggle_small7, R.id.scraggle_small8,
            R.id.scraggle_small9,};

    private Handler mHandler = new Handler();
    private int mSoundX, mSoundO, mSoundMiss, mSoundRewind;


    private SoundPool mSoundPool;
    private float mVolume = 1f;

    private TwoPlayerGameScraggleTile mEntireBoard = new TwoPlayerGameScraggleTile(this);
    private TwoPlayerGameScraggleTile mLargeTiles[] = new TwoPlayerGameScraggleTile[9];
    private TwoPlayerGameScraggleTile mSmallTiles[][] = new TwoPlayerGameScraggleTile[9][9];

    public TwoPlayerGameScraggleControlFragment scraggleControl;
    public TwoPlayerGameScraggleMiscFragment scraggleMisc;

    public Trie tt;

    wordFoundListener wfListener;

    ArrayList<String> lgTileWords = new ArrayList<>(9);

    public ArrayList<ArrayList<Integer>> tileSelected = new ArrayList<ArrayList<Integer>>();

    public ArrayList<String> wordsFound = new ArrayList<>();

    public ArrayList<ArrayList<TwoPlayerGameScraggleTile>> mAvailable = new ArrayList<ArrayList<TwoPlayerGameScraggleTile>>();


    public int scoreSummary = 0;

    private int mLastLarge;
    private int mLastSmall;
    public Context context;

    public ArrayList<String> wordsToUse = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> sequences = new ArrayList<>();

    private static int zeroRules[] = {1, 3, 4};
    private static int oneRules[] = {0, 2, 3, 4, 5};
    private static int twoRules[] = {1, 4, 5};
    private static int threeRules[] = {0, 1, 4, 6, 7};
    private static int fourRules[] = {0, 1, 2, 3, 5, 6, 7, 8};
    private static int fiveRules[] = {1, 2, 4, 7, 8};
    private static int sixRules[] = {3, 4, 7};
    private static int sevenRules[] = {3, 4, 5, 6, 8};
    private static int eightRules[] = {4, 5, 7};
    private static int rules[][] = {zeroRules, oneRules, twoRules, threeRules, fourRules, fiveRules, sixRules, sevenRules, eightRules};

    public TwoPlayerGameScraggleGameActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mSoundX = mSoundPool.load(getActivity(), R.raw.ting, 1);

        for (int i = 0; i < 9; i++) {
            mAvailable.add(new ArrayList<TwoPlayerGameScraggleTile>());
        }

        for (int i = 0; i < 9; i++) {
            tileSelected.add(new ArrayList<Integer>());
        }

        context = this.getActivity();
        readWordFile();
        readSequenceFile();
        initGame();
        for (int i = 0; i < 9; i++) {
            lgTileWords.add("");
        }

    }

    public boolean isAvailable(int large, TwoPlayerGameScraggleTile tile) {
        if (mAvailable.get(large).isEmpty()) {
            return true;
        } else {
            return mAvailable.get(large).contains(tile);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.scraggle_large_board, container, false);
        initViews(rootView);
        return rootView;

    }


    private void initViews(View rootView) {
        mEntireBoard.setView(rootView);

        Collections.shuffle(wordsToUse);
        Collections.shuffle(sequences);

        ArrayList<String> wordsInTiles = new ArrayList<>();

        ArrayList<ArrayList<Integer>> sequenceForTiles = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            wordsInTiles.add(wordsToUse.get(i));
            sequenceForTiles.add(sequences.get(i));
        }

        for (int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);
            String randomWord = wordsInTiles.get(large);
            ArrayList<Integer> sequenceForTile = sequenceForTiles.get(large);

            for (int small = 0; small < 9; small++) {
                final TextView inner = (TextView) outer.findViewById(mSmallIds[small]);
                final int fLarge = large;
                final int fSmall = small;
                final TwoPlayerGameScraggleTile smallTile = mSmallTiles[large][small];
                smallTile.setView(inner);

                int index = sequenceForTile.get(small);
                final String letter = String.valueOf(randomWord.charAt(index)).toUpperCase();
                inner.setText(letter);

                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isAvailable(fLarge, smallTile)) {

                            smallTile.animate();
                            String word = lgTileWords.get(fLarge) + letter.toLowerCase();
                            lgTileWords.set(fLarge, word);
                            int tile_status = smallTile.getTileStatus();

                            if (smallTile.getTileStatus() == 4) {
                                inner.setBackgroundResource(R.drawable.tile_empty);
                                smallTile.setTileStatus(TwoPlayerGameScraggleTile.TileStatus.SELECTED);
                                tileSelected.get(fLarge).add(fSmall);
                                setAvailableFromLastMove(fLarge, fSmall);

                            } else if (smallTile.getTileStatus() == 5) {
                                inner.setBackgroundResource(R.drawable.tile_available);
                                smallTile.setTileStatus(TwoPlayerGameScraggleTile.TileStatus.UNSELECTED);


                                Object a = fSmall;
                                int index = tileSelected.get(fLarge).indexOf(a);
                                for (int i = index; i < tileSelected.get(fLarge).size(); i++) {
                                    int smallIndex = tileSelected.get(fLarge).get(index);
                                    mSmallTiles[fLarge][smallIndex].setTileStatus(TwoPlayerGameScraggleTile.TileStatus.UNSELECTED);
                                    mSmallTiles[fLarge][smallIndex].getView().setBackgroundResource(R.drawable.tile_available);
                                    tileSelected.get(fLarge).remove(i);

                                }

                                int lastSelected = 0;
                                if (index != 0) {
                                    lastSelected = tileSelected.get(fLarge).get(index - 1);

                                }
                                setAvailableFromLastMove(fLarge, lastSelected);

                            }

                            boolean found = checkWord(lgTileWords.get(fLarge));

                            if (found && !wordsFound.contains(lgTileWords.get(fLarge))) {
                                scoreSummary = (scoreSummary + 1);
                                wfListener.addScores(scoreSummary);
                                wfListener.addWords(lgTileWords.get(fLarge));
                                wfListener.sendNotification(lgTileWords.get(fLarge),scoreSummary);
                                Log.d("Starting to GCM :", lgTileWords.get(fLarge));
//                                wfSendListener.sendNotification(lgTileWords.get(fLarge));
                                wordsFound.add(lgTileWords.get(fLarge));
                                setColorsWordFound(fLarge);
                                mSoundPool.play(mSoundX, mVolume, mVolume, 1, 0, 1f);


                            } else if (found && wordsFound.contains(lgTileWords.get(fLarge))) {
                                makeMove(fLarge, fSmall);
                                setColorsNotSelected(fLarge);

                            }


                        } else {

                            for (int i = 0; i < mAvailable.get(fLarge).size(); i++) {
                                mAvailable.get(fLarge).get(i).animate();
                            }
                        }


                    }
                });

            }

            }



        }


    private void makeMove(int large, int small) {
        mLastLarge = large;
        mLastSmall = small;
        TwoPlayerGameScraggleTile sTile = mSmallTiles[large][small];
        TwoPlayerGameScraggleTile lGTile = mLargeTiles[large];


        Context context = getContext();
        CharSequence text = "DUPLICATE WORDS NOT ALLOWED!";
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();

    }

    public interface wordFoundListener {
        public void addWords(String word);
        public void addScores(int score);
        public void sendNotification(String word, Integer score);
    }

//    public interface wordFoundSendNotificationListener{
//        public void sendNotification(String word);
//
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            wfListener = (wordFoundListener) activity;
//            wfSendListener = (wordFoundSendNotificationListener) activity;
        } catch (Exception e) {

        }

    }





    private void setColorsWordFound(int large) {
        for (int i = 0; i < tileSelected.get(large).size(); i++) {
            int index = tileSelected.get(large).get(i);
            mSmallTiles[large][index].getView().setBackgroundResource(R.drawable.tile_green);
            mSmallTiles[large][index].setTileStatus(TwoPlayerGameScraggleTile.TileStatus.WORDFOUND);
        }
    }

    private void setColorsNotSelected(int large) {
        for (int i = 0; i < tileSelected.get(large).size(); i++) {
            int index = tileSelected.get(large).get(i);
            mSmallTiles[large][index].getView().setBackgroundResource(R.drawable.tile_available);
            mSmallTiles[large][index].setTileStatus(TwoPlayerGameScraggleTile.TileStatus.UNSELECTED);

        }
    }

    public void restartGame() {
        initGame();
        initViews(getView());

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                mSmallTiles[i][j].setTileStatus(TwoPlayerGameScraggleTile.TileStatus.UNSELECTED);
                mSmallTiles[i][j].getView().setBackgroundResource(R.drawable.border_style);
            }
        }
    }

    public void initGame() {
        mEntireBoard = new TwoPlayerGameScraggleTile(this);
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new TwoPlayerGameScraggleTile(this);
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new TwoPlayerGameScraggleTile(this);
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);
    }

    private void setAvailableFromLastMove(int large, int small) {
        clearAvailable();

        if (numberSmallTileSelected(large) == 0) {
            setSmallAvailable(large);
        } else {
            for (int i = 0; i < rules[small].length; i++) {
                int smallIndex = rules[small][i];
                TwoPlayerGameScraggleTile adjTile = mSmallTiles[large][smallIndex];
                addAvailable(large, adjTile);
            }

            for (int i = 0; i < tileSelected.get(large).size(); i++) {
                int smallIndex = tileSelected.get(large).get(i);
                TwoPlayerGameScraggleTile tile = mSmallTiles[large][smallIndex];

                if (!mAvailable.get(large).contains(tile)) {
                    addAvailable(large, tile);
                }
            }

        }
    }

    private void clearAvailable() {
        for (int i = 0; i < 9; i++) {
            mAvailable.get(i).clear();
        }
    }

    private void addAvailable(int small, TwoPlayerGameScraggleTile tile) {
        mAvailable.get(small).add(tile);
    }

    public String getState() {
        StringBuilder builder = new StringBuilder();
        builder.append(mLastLarge);
        builder.append(',');
        builder.append(mLastSmall);
        builder.append(',');

        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                builder.append(mSmallTiles[large][small].getOwner().name());
                builder.append(',');
            }
        }

        return builder.toString();
    }

    public void putState(String data) {
        String[] fields = data.split(",");
        int index = 0;
        mLastLarge = Integer.parseInt(fields[index++]);
        mLastSmall = Integer.parseInt(fields[index++]);

        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                TwoPlayerGameScraggleTile.Owner owner = TwoPlayerGameScraggleTile.Owner.valueOf(fields[index++]);
                mSmallTiles[large][small].setOwner(owner);
            }
        }
//        updateAllTiles();
    }

    private int numberSmallTileSelected(int small) {
        return tileSelected.get(small).size();
    }

    private void setSmallAvailable(int large) {
        for (int small = 0; small < 9; small++) {
            TwoPlayerGameScraggleTile tile = mSmallTiles[large][small];
            if (tile.getTileStatus() == 4) {
                addAvailable(large, tile);
            }
        }
    }


    public void readSequenceFile(){
        InputStream inputStream2 = null;
        try {
            inputStream2 = context.getAssets().open("sequence.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream2 != null) {
            String sequence;
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream2, "UTF-8"));
                while ((sequence = bufferedReader.readLine()) != null) {
                    char arChr[] = sequence.toCharArray();
                    ArrayList<Integer> arInt = new ArrayList<>();
                    for (int i = 0; i < arChr.length; i++) {
                        arInt.add(Character.getNumericValue(arChr[i]));
                    }
                    sequences.add(arInt);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readWordFile() {

        try {
            String fileName = "WordsFile.txt";
            AssetManager am = context.getAssets();
            InputStream ins = null;
            ins = am.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    wordsToUse.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean checkWord(String s) {
        if (s.length() > 1) {
            tt = new Trie();
            try {
                String fileName = s.substring(0, 2) + ".txt";
                AssetManager am = context.getAssets();
                InputStream ins = null;
                ins = am.open(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
                String line;
                while ((line = reader.readLine()) != null) {
                    tt.add(line);
                }
                if (tt.contains(s)) {
                    return true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public boolean isLevelTwo() {
        for (int i = 0; i < 9; i++) {
            tileSelected.get(i).clear();
            for (int j = 0; j < 9; j++) {
                TwoPlayerGameScraggleTile tile = mSmallTiles[i][j];
                View tileView = tile.getView();
                if (tile.getTileStatus() != 6) {
                    tileView.setBackgroundResource(R.drawable.tile_black);
                    tileView.setClickable(false);
                } else {
                    tile.setTileStatus(TwoPlayerGameScraggleTile.TileStatus.UNSELECTED);
                    tileView.setBackgroundResource(R.drawable.border_style);
                }
            }
        }

        return true;


    }

}
