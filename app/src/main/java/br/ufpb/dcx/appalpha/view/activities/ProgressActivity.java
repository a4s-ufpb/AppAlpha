package br.ufpb.dcx.appalpha.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.ChallengeFacade;
import br.ufpb.dcx.appalpha.control.util.AudioUtil;
import br.ufpb.dcx.appalpha.control.util.ImageLoadUtil;
import br.ufpb.dcx.appalpha.control.util.TextUtil;

/**
 * Activity for play sound of word and after speak each letter of word
 */
public class ProgressActivity extends AppCompatActivity {
    private final String TAG = "ProgressActivity";
    private TextView txt;
    private boolean hasChangedActivity;
    private int millis = 2500;
    private char[] letters;
    private Thread readLetterThread;

    /**
     * On create activity, setup local variables
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_progress);

        setUnderscore();

        setLetters();

        setChallengeImage();

        hasChangedActivity = false;

        String soundUrl = ChallengeFacade.getInstance().getCurrentChallenge().getSoundUrl();

        if (soundUrl != null && !soundUrl.equals("")) {
            if (soundUrl.startsWith("http")) {
                AudioUtil.getInstance().playSoundURL(soundUrl);
            } else if (TextUtil.isAllInteger(soundUrl)) { // Para desafios internos do appalpha
                AudioUtil.getInstance(getApplicationContext()).playSound(Integer.parseInt(soundUrl));
            } else {
                AudioUtil.getInstance().speakWord(ChallengeFacade.getInstance().getCurrentChallenge().getWord());
            }
        } else {
            AudioUtil.getInstance().speakWord(ChallengeFacade.getInstance().getCurrentChallenge().getWord());
        }



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readLetterByLetter();
            }
        }, AudioUtil.getInstance(getApplicationContext()).getDuration());
    }

    /**
     * Action to start speaking letter bt letter of the word
     */
    private void readLetterByLetter() {
        readLetterThread = new Thread() {

            @Override
            public void run() {

                AudioUtil.getInstance().esperarTssParar();

                try {
                    while (!isInterrupted()) {
                        for (final char letra : TextUtil.normalize(new String(letters)).toCharArray()) {

                            try {
                                Thread.sleep(millis);
                            } catch (InterruptedException e) {
                                Log.i("for try", "entrou no catch");
                                break;
                            }

                            final Runnable update = new Runnable() {
                                @Override
                                public void run() {
                                    playLetterSong(letra);
                                    updateUnderscoreInTextViewAndFacade(letra);
                                }
                            };

                            if (hasChangedActivity) {
                                Thread.currentThread().interrupt();
                            } else {
                                runOnUiThread(update);
                            }
                        }
                        break;
                    }
                } catch (Exception e) {
                    Log.i("erro", e.getMessage());
                }
            }
        };

        readLetterThread.start();

    }

    /**
     * Action call for Back button press
     */
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        AudioUtil.getInstance(getApplicationContext()).pararTSSePlayer();
    }

    /**
     * Dealloc the activity
     */
    protected void onDestroy() {
        super.onDestroy();
        //leitor.interrupt();
        hasChangedActivity = true;
    }

    /**
     * Play the sound of letter
     * @param letter letter to be played
     */
    public void playLetterSong(char letter) {
        int idSound = 0;

        switch (Character.toUpperCase(letter)) {

            case 'A':
                idSound = R.raw.letraa;
                break;

            case 'B':
                idSound = R.raw.letrab;
                break;

            case 'C':
                idSound = R.raw.letrac;
                break;

            case 'D':
                idSound = R.raw.letrad;
                break;

            case 'E':
                idSound = R.raw.letrae;
                break;

            case 'F':
                idSound = R.raw.letraf;
                break;

            case 'G':
                idSound = R.raw.letrag;
                break;

            case 'H':
                idSound = R.raw.letrah;
                break;

            case 'I':
                idSound = R.raw.letrai;
                break;

            case 'J':
                idSound = R.raw.letraj;
                break;

            case 'K':
                idSound = R.raw.letrak;
                break;

            case 'L':
                idSound = R.raw.letral;
                break;

            case 'M':
                idSound = R.raw.letram;
                break;

            case 'N':
                idSound = R.raw.letran;
                break;

            case 'O':
                idSound = R.raw.letrao;
                break;

            case 'P':
                idSound = R.raw.letrap;
                break;

            case 'Q':
                idSound = R.raw.letraq;
                break;

            case 'R':
                idSound = R.raw.letrar;
                break;

            case 'S':
                idSound = R.raw.letras;
                break;

            case 'T':
                idSound = R.raw.letrat;
                break;

            case 'U':
                idSound = R.raw.letrau;
                break;

            case 'V':
                idSound = R.raw.letrav;
                break;

            case 'W':
                idSound = R.raw.letraw;
                break;

            case 'X':
                idSound = R.raw.letrax;
                break;

            case 'Y':
                idSound = R.raw.letray;
                break;

            case 'Z':
                idSound = R.raw.letraz;
                break;

        }

        if(idSound != 0) {
            AudioUtil.getInstance(getApplicationContext()).playSound(idSound);
        } else {
            AudioUtil.getInstance(getApplicationContext()).speakWord(String.valueOf(letter));
        }
    }

    /**
     * set o textView with underscore of word
     */
    public void setUnderscore() {
        ChallengeFacade.getInstance().setUnderlinedWord();
    }

    /**
     * Update the current underscore
     * @param letra letter to be passed in the underscore
     */
    public void updateUnderscoreInTextViewAndFacade(char letra) {
        String newUnderscore = updateUnderscore(letra);
        Log.i(TAG, "new underscore = "+newUnderscore);
        setTextViewWord(newUnderscore);
        updateUnderscore(newUnderscore);
    }

    /**
     * Set the current underscore in the ChallengeFacade
     */
    public void updateUnderscore(String underscore) {
        ChallengeFacade.getInstance().setCurrentUnderlinedWord(underscore);
    }

    /**
     * Explode the word by the char letter, to be used in the underscore
     */
    public void setLetters() {
        letters = new char[ChallengeFacade.getInstance().getCurrentChallenge().getWord().length()];
        for (int i = 0; i < ChallengeFacade.getInstance().getCurrentChallenge().getWord().length(); i++) {
            letters[i] = ChallengeFacade.getInstance().getCurrentChallenge().getWord().charAt(i);
        }
    }

    /**
     * Update the underscore showing the letter specified
     * @param letra
     * @return
     */
    public String updateUnderscore(char letra) {
        char[] vetor = ChallengeFacade.getInstance().getCurrentUnderlinedWord().toCharArray();

        for (int i = 0; i < ChallengeFacade.getInstance().getCurrentChallenge().getWord().length(); i++) {
            if (Character.toUpperCase(
                    TextUtil.normalize(ChallengeFacade.getInstance().getCurrentChallenge().getWord())
                            .charAt(i)) == Character.toUpperCase(letra)) {

                if (Character.toUpperCase(
                        TextUtil.normalize(ChallengeFacade.getInstance().getCurrentUnderlinedWord())
                                .charAt(i)) == Character.toUpperCase(letra)) {
                    continue;
                }
                vetor[i] = ChallengeFacade.getInstance().getCurrentChallenge().getWord().charAt(i);
                break;
            }

        }
        return new String(vetor);
    }

    /**
     * Open the next Challenge activity
     */
    public void goToTheNextChallenge() {
        hasChangedActivity = true;
        Intent it = new Intent(getApplicationContext(), HangmanActivity.class);
        ChallengeFacade.getInstance().nextChallenge();
        startActivity(it);
        finish();
    }

    /**
     * Action for Open the final activity when the game end
     */
    public void goToTheFinalActivity() {
        hasChangedActivity = true;
        Intent it = new Intent(getApplicationContext(), FinalActivity.class);
        startActivity(it);

    }

    /**
     * Check if the challenge ended, and go to next challenge or for final activity
     * @param v View of pressed button
     */
    public void goToTheNextActivityByCondiction(View v)
    {
        // Desativar o botão depois do click
        v.setEnabled(false);

        AudioUtil.getInstance(getApplicationContext()).pararTSSePlayer();

        Log.i(TAG, "pg" + ChallengeFacade.getInstance().getProgressCount());
        Log.i(TAG, "size" + ChallengeFacade.getInstance().getChallenges().size());
        if (ChallengeFacade.getInstance().getProgressCount() == ChallengeFacade.getInstance().getChallenges().size() - 1) {
            goToTheFinalActivity();
        } else {
            goToTheNextChallenge();
        }
        finish();
    }

    /**
     * Action for load the image url of Challenge
     */
    public void setChallengeImage() {
        ImageView img_desafio = findViewById(R.id.img_desafio);
        String imgUrl = ChallengeFacade.getInstance().getCurrentChallenge().getImageUrl();

        ImageLoadUtil.getInstance().loadImage(imgUrl, img_desafio, getApplicationContext());
    }

    /**
     * Set the word in the Text view
     * @param underscore
     */
    public void setTextViewWord(String underscore) {
        txt = findViewById(R.id.txt_underscore);
        txt.setText(underscore);
    }

}