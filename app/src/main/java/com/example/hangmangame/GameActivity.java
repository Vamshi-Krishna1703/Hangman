package com.example.hangmangame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
//import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private String[] words;
    private Random rand;
    private String currWord;
    private LinearLayout wordLayout;
    private TextView[] charViews;

    private TextView h;
    private GridView letters;
    private LetterAdapter ltrAdapt;
    private TextView s;
    int score=0;
    private ImageView[] bodyParts;
    private int numParts=6;
    private int currPart;
    private int numChars;
    private int numCorr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Resources res = getResources();
        words = res.getStringArray(R.array.words);
        rand = new Random();
        currWord = "";
        wordLayout = (LinearLayout)findViewById(R.id.word);
        letters = (GridView)findViewById(R.id.letters);
        h=(TextView)findViewById(R.id.hint);
        s=(TextView)findViewById(R.id.score);
        bodyParts = new ImageView[numParts];
        bodyParts[0] = (ImageView)findViewById(R.id.head);
        bodyParts[1] = (ImageView)findViewById(R.id.body);
        bodyParts[2] = (ImageView)findViewById(R.id.arm1);
        bodyParts[3] = (ImageView)findViewById(R.id.arm2);
        bodyParts[4] = (ImageView)findViewById(R.id.leg1);
        bodyParts[5] = (ImageView)findViewById(R.id.leg2);

        playGame();
    }

    private void playGame()
    {
        String newWord = words[rand.nextInt(words.length)];
        while(newWord.equals(currWord)) newWord = words[rand.nextInt(words.length)];
        String x[] = newWord.split(":");
        currWord=x[0];
        newWord=x[0];
        String hint=x[1];
        charViews = new TextView[currWord.length()];
        wordLayout.removeAllViews();

        currPart=0;
        numChars=currWord.length();
        numCorr=0;
        for(int p = 0; p < numParts; p++) {
            bodyParts[p].setVisibility(View.INVISIBLE);
        }
        h.setText("Hint : "+hint);
        ltrAdapt=new LetterAdapter(this);
        letters.setAdapter(ltrAdapt);
        s.setText("Score : "+score);

        for (int c = 0; c < currWord.length(); c++) {
            charViews[c] = new TextView(this);
            charViews[c].setText(""+currWord.charAt(c));

            charViews[c].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(Color.BLACK);
            charViews[c].setBackgroundResource(R.drawable.letter_bg);
            //add to layout
            wordLayout.addView(charViews[c]);
        }
    }

    public void letterPressed(View view) {
        //user has pressed a letter to guess
        String ltr = ((TextView) view).getText().toString();
        char letterChar = ltr.charAt(0);
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_down);
        boolean correct = false;
        for (int k = 0; k < currWord.length(); k++) {
            if (currWord.charAt(k) == letterChar) {
                correct = true;
                numCorr++;
                charViews[k].setTextColor(Color.WHITE);
            }
        }

        if (correct) {
            //correct guess
            if (numCorr == numChars) {
                //user has won
                disableBtns();
                //Toast.makeText(getApplicationContext(),"Winner Yo hoo",Toast.LENGTH_LONG).show();
                // Display Alert Dialog
                score++;
           AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
            winBuild.setTitle("YAY");
            winBuild.setMessage("You win!\n\nThe answer was:\n\n"+currWord+"\n\n Score : "+score);
            winBuild.setPositiveButton("Play Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GameActivity.this.playGame();
                        }});

            winBuild.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GameActivity.this.finish();
                        }});

            winBuild.show();
            }
        } else if (currPart < numParts) {
            //some guesses left
            bodyParts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        } else {
            //user has lost
            disableBtns();

            // Display Alert Dialog
            AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
            loseBuild.setTitle("OOPS");
            loseBuild.setMessage("You lose!\n\nThe answer was:\n\n" + currWord+"\n\n Score : "+score);
            loseBuild.setPositiveButton("Play Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GameActivity.this.playGame();
                        }
                    });

            loseBuild.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GameActivity.this.finish();
                        }
                    });

            loseBuild.show();
            score=0;
        }
    }
    public void disableBtns() {
        int numLetters = letters.getChildCount();
        for (int l = 0; l < numLetters; l++) {
            letters.getChildAt(l).setEnabled(false);
        }
    }

}
