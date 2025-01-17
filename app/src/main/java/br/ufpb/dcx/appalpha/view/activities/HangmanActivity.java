package br.ufpb.dcx.appalpha.view.activities;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.ufpb.dcx.appalpha.control.ChallengeFacade;
import br.ufpb.dcx.appalpha.control.HangmanController;
import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.util.ImageLoadUtil;
import br.ufpb.dcx.appalpha.control.util.AudioUtil;
import br.ufpb.dcx.appalpha.control.util.ChronometerUtil;
import br.ufpb.dcx.appalpha.control.util.TextUtil;

/**
 * Class of activity in the game for show the image of hanged man, word and keyboard
 */
public class HangmanActivity extends AppCompatActivity {
    private static final String TAG = "HangmanActivity";
    final int MAX_ERR = 6;
    HangmanController hangmanController;
    ChronometerUtil chronometerUtil;
    private ImageView imgWord;

    /**
     * Dalloc local variables
     */
    public void memoryFree() {
        hangmanController = null;
        chronometerUtil = null;
        imgWord.setImageDrawable(null);
    }

    /**
     * On create activity, setup local variables
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);

        // set underscore in the TextView of screen
        TextView txtUnderscore = findViewById(R.id.txt_underscore);
        txtUnderscore.setText(ChallengeFacade.getInstance().getUnderlinedWordWithSpaces());

        // set ImageView da forca in HangmanController to access and update the image
        ImageView img_forca = findViewById(R.id.img_forca);
        hangmanController = new HangmanController(img_forca);

        // set image of word
        imgWord = findViewById(R.id.img_palavra);
        ImageLoadUtil.getInstance().loadImage(ChallengeFacade.getInstance().getCurrentChallenge().getImageUrl(), imgWord, getApplicationContext());

        // setup chronometer
        chronometerUtil = new ChronometerUtil(findViewById(R.id.cronometro), getApplicationContext());
        chronometerUtil.comecarCronometro();

    }

    /**
     * Action call for Back button press
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AudioUtil.getInstance(getApplicationContext()).pararTSSePlayer();
    }

    /**
     * On dealloc activity free memory and clean letter attempt
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //AudioUtil.getInstance().stopSound();
        memoryFree();
        ChallengeFacade.getInstance().resetTryLetterAttempts();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Action for vibrate de device when tap on the letter
     */
    private void feedbackTatil() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(80);
        }
    }

    /**
     * Action for check the attempted letter an change background color of button pressed based in wrong or correct
     *
     * @param letraClicada letter of player attempted
     * @param btnClicado   button of pressed letter
     */
    public void feedbackColorButtonLetter(String letraClicada, Button btnClicado) {
        feedbackTatil();

        int resultado = ChallengeFacade.getInstance().getAttempResult(letraClicada);

        if (resultado == ChallengeFacade.ATTEMPT_ACEPTED) {
            btnClicado.setBackgroundResource(R.drawable.greem_rounded_backgroud);
        } else if (resultado == ChallengeFacade.ATTEMPT_EXISTS) {
            Toast.makeText(getApplicationContext(), "Você já chutou essa letra!", Toast.LENGTH_SHORT).show();
        } else { // ATTEMP_REJECTED
            btnClicado.setBackgroundResource(R.drawable.red_rounded_backgroud);
        }
    }

    /**
     * Update the information in the screen
     *
     * @param letraClicada letter of player attempted
     * @param btnClicado   button of pressed letter
     */
    public void updateData(String letraClicada, Button btnClicado) {

        // update background of button
        feedbackColorButtonLetter(letraClicada, btnClicado);

        // update image of hanged man according to attempt count
        initHangman();

        // set the text view with the new underscore
        setUnderscoreInTextview(ChallengeFacade.getInstance().getCurrentUnderlinedWord());


        verifyChallengeItsOver();
    }

    /**
     * Update the image of hanged man
     */
    private void initHangman() {
        hangmanController.mudaForca(ChallengeFacade.getInstance().getErroCount());
    }

    /**
     * Check if the number of error are fired or if the word was accepted
     */
    private void verifyChallengeItsOver() {
        Intent it = new Intent(this, ProgressActivity.class);

        if (ChallengeFacade.getInstance().getErroCount() == MAX_ERR || ChallengeFacade.getInstance().checkWordAccepted()) {
            ChallengeFacade.getInstance().increaseTime(chronometerUtil.stopChronometerAndGetTime());
            startActivity(it);
            finish();
        }

    }

    /**
     * Set text view with underscore of word
     *
     * @param underscore underscore of word
     */
    private void setUnderscoreInTextview(String underscore) {
        TextView txtUnderscore = findViewById(R.id.txt_underscore);
        txtUnderscore.setText(ChallengeFacade.getInstance().getUnderlinedWordWithSpaces());
    }

    /**
     * Play the word sound
     *
     * @param v view
     */
    public void playWordSound(View v) {
        String soundUrl = ChallengeFacade.getInstance().getCurrentChallenge().getSoundUrl();
        if (soundUrl != null && !soundUrl.equals("")) {
            if (soundUrl.startsWith("http")) {
                AudioUtil.getInstance().playSoundURL(soundUrl);
            } else if (TextUtil.isAllInteger(soundUrl)) {
                AudioUtil.getInstance().playSound(Integer.parseInt(soundUrl));
            } else {
                AudioUtil.getInstance().speakWord(ChallengeFacade.getInstance().getCurrentChallenge().getWord());
            }
        } else {
            AudioUtil.getInstance().speakWord(ChallengeFacade.getInstance().getCurrentChallenge().getWord());
        }
    }

    /***
     * Action of clicked button
     * @param view button clicked
     */
    public void letterClick(View view) {
        Button btClick = (Button) view;
        updateData(btClick.getText().toString().toLowerCase(), btClick);
        Log.i("Botão clicado:", btClick.getText().toString());
    }

}
