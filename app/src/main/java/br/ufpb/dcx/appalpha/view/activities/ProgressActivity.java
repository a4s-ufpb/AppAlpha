package br.ufpb.dcx.appalpha.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.ChallengeFacade;
import br.ufpb.dcx.appalpha.control.config.ButtonDelay;
import br.ufpb.dcx.appalpha.control.util.AudioUtil;
import br.ufpb.dcx.appalpha.control.util.TextUtil;

public class ProgressActivity extends AppCompatActivity {
    private final String TAG = "ProgressActivity";
    private TextView txt;
    private boolean hasChangedActivity;
    private int millis = 2500;
    private char[] letras;
    private Thread leitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_progresso_);

        setUnderscore();

        setLetters();

        setChallengeImage();

        hasChangedActivity = false;

        String soundUrl = ChallengeFacade.getInstance().getCurrentChallenge().getSoundUrl();

        if (soundUrl != null && !soundUrl.equals("")) {
            if (soundUrl.startsWith("http")) {
                // TODO obter som da URL, se for URL
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

    private void readLetterByLetter() {
        leitor = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        for (final char letra : TextUtil.normalize(new String(letras)).toCharArray()) {
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

        leitor.start();

    }

    /**
     * Quando a activity é destruida também destroi o objeto do media player
     */
    protected void onDestroy() {
        super.onDestroy();
        //leitor.interrupt();
        AudioUtil.getInstance(getApplicationContext()).stopSound();

    }

    /**
     * Procura o audio da letra com base na letra passada como parâmetro e toca esse audio
     *
     * @param letra Letra que será tocada
     */
    public void playLetterSong(char letra) {
        Log.i(TAG, letra + "");
        int idSom = 0;

        switch (Character.toUpperCase(letra)) {

            case 'A':
                idSom = R.raw.letraa;
                break;

            case 'B':
                idSom = R.raw.letrab;
                break;

            case 'C':
                idSom = R.raw.letrac;
                break;

            case 'D':
                idSom = R.raw.letrad;
                break;

            case 'E':
                idSom = R.raw.letrae;
                break;

            case 'F':
                idSom = R.raw.letraf;
                break;

            case 'G':
                idSom = R.raw.letrag;
                break;

            case 'H':
                idSom = R.raw.letrah;
                break;

            case 'I':
                idSom = R.raw.letrai;
                break;

            case 'J':
                idSom = R.raw.letraj;
                break;

            case 'K':
                idSom = R.raw.letrak;
                break;

            case 'L':
                idSom = R.raw.letral;
                break;

            case 'M':
                idSom = R.raw.letram;
                break;

            case 'N':
                idSom = R.raw.letran;
                break;

            case 'O':
                idSom = R.raw.letrao;
                break;

            case 'P':
                idSom = R.raw.letrap;
                break;

            case 'Q':
                idSom = R.raw.letraq;
                break;

            case 'R':
                idSom = R.raw.letrar;
                break;

            case 'S':
                idSom = R.raw.letras;
                break;

            case 'T':
                idSom = R.raw.letrat;
                break;

            case 'U':
                idSom = R.raw.letrau;
                break;

            case 'V':
                idSom = R.raw.letrav;
                break;

            case 'W':
                idSom = R.raw.letraw;
                break;

            case 'X':
                idSom = R.raw.letrax;
                break;

            case 'Y':
                idSom = R.raw.letray;
                break;

            case 'Z':
                idSom = R.raw.letraz;
                break;

        }

        AudioUtil.getInstance(getApplicationContext()).playSound(idSom);
    }

    /**
     * Seta o textView com o underscore da palavra
     */
    public void setUnderscore() {
        ChallengeFacade.getInstance().setUnderlinedWord();
    }

    /**
     * Atualiza o underscore atual com o novo
     *
     * @param letra letra que vai ser adicionada ao underscore
     */
    public void updateUnderscoreInTextViewAndFacade(char letra) {
        String newUnderscore = updateUnderscore(letra);
        Log.i(TAG, "new underscore = "+newUnderscore);
        setTextViewWord(newUnderscore);
        updateUnderscore(newUnderscore);
    }

    /**
     * Seta o underscore no objeto tratando palavra para que
     * na próxima vez que ele gerar o underscore novo ele tenha o mais atualizado
     */
    public void updateUnderscore(String underscore) {
        ChallengeFacade.getInstance().setCurrentUnderlinedWord(underscore);
    }

    /**
     * Quebra a palavra do desafio em um array de char,
     * para que ele possa adicionar um a um no underscore
     */
    public void setLetters() {
        letras = new char[ChallengeFacade.getInstance().getCurrentChallenge().getWord().length()];
        for (int i = 0; i < ChallengeFacade.getInstance().getCurrentChallenge().getWord().length(); i++) {
            letras[i] = ChallengeFacade.getInstance().getCurrentChallenge().getWord().charAt(i);
        }
    }

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

    public void goToTheNextChallenge() {
        hasChangedActivity = true;
        Intent it = new Intent(getApplicationContext(), ForcaActivity.class);
        ChallengeFacade.getInstance().nextChallenge();
        startActivity(it);
        finish();
    }

    /**
     * Redireciona para a activity da pontuação final
     */
    public void goToTheFinalActivity() {
        hasChangedActivity = true;
        Intent it = new Intent(getApplicationContext(), FinalActivity.class);
        startActivity(it);

    }

    /**
     * Verifica se os desafios acabaram, se sim manda para a tela de pontuação, se não manda para a tela da forca
     *
     * @param v View do botão
     */
    public void goToTheNextActivityByCondiction(View v) {
        // Testa se o botão foi clicado mais de uma vez em um intervalo de 1 segundo
        if (ButtonDelay.delay(1000)) {
            Log.i(TAG, "pg" + ChallengeFacade.getInstance().getProgressCount());
            Log.i(TAG, "size" + ChallengeFacade.getInstance().getChallenges().size());
            if (ChallengeFacade.getInstance().getProgressCount() == ChallengeFacade.getInstance().getChallenges().size() - 1) {
                goToTheFinalActivity();
            } else {
                goToTheNextChallenge();
            }
        }
        finish();
    }

    public void setChallengeImage() {
        ImageView img_desafio = findViewById(R.id.img_desafio);
        String imgUrl = ChallengeFacade.getInstance().getCurrentChallenge().getImageUrl();

        if (imgUrl != null && !imgUrl.isEmpty()) {
            if (imgUrl.startsWith("http")) {
                Picasso.get().load(imgUrl).into(img_desafio);
            } else {
                img_desafio.setImageResource(Integer.parseInt(imgUrl));
            }
        } else {
            // TODO tratar quando não tem imagem
        }

    }

    public void setTextViewWord(String underscore) {
        txt = findViewById(R.id.txt_underscore);
        txt.setText(underscore);
    }

}