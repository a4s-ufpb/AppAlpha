package br.ufpb.dcx.appalpha.view.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.ufpb.dcx.appalpha.control.ChallengeFacade;
import br.ufpb.dcx.appalpha.control.ForcaController;
import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.util.ImageLoadUtil;
import br.ufpb.dcx.appalpha.control.util.AudioUtil;
import br.ufpb.dcx.appalpha.control.util.Cronometro;
import br.ufpb.dcx.appalpha.control.log.LogManagerExtStor;
import br.ufpb.dcx.appalpha.control.util.TextUtil;
import br.ufpb.dcx.appalpha.locator.ServiceLocator;


public class ForcaActivity extends AppCompatActivity {
    private static final String TAG = "ForcaActivity";
    final int QTD_MAX_ERROS = 6;
    ForcaController forcaController;
    Cronometro cronometro;
    private LogManagerExtStor logManagerExt;
    private ImageView imgPalavra;
    ChallengeFacade challengeFacade;

    public void memoryFree() {
        forcaController = null;
        cronometro = null;
        imgPalavra.setImageDrawable(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forca);

        challengeFacade = ServiceLocator.getChallengeFacade();

        // Setando o underscore no TextView da tela
        TextView txtUnderscore = findViewById(R.id.txt_underscore);
        txtUnderscore.setText(challengeFacade.getUnderscoreWithSpaces());
        //txtUnderscore.setText(setSpacesInWord(ChallengeFacade.getInstance().getCurrentUnderscore()));

        // Setando o ImageView da forca no objeto para modificação ao longo do jogo
        ImageView img_forca = findViewById(R.id.img_forca);
        forcaController = new ForcaController(img_forca);

        // Setando imagem da palavra

        imgPalavra = findViewById(R.id.img_palavra);
        ImageLoadUtil.getInstance().loadImage(challengeFacade.getCurrentChallenge().getImageUrl(), imgPalavra, getApplicationContext());
        //ImageLoadUtil.getInstance().loadImage(ChallengeFacade.getInstance().getCurrentChallenge().getImageUrl(), imgPalavra, getApplicationContext());

        // Iniciando cronômetro
        cronometro = new Cronometro(findViewById(R.id.cronometro), getApplicationContext());
        cronometro.comecarCronometro();

        //Iniciando o gerenciador de Logs para memória interna
        this.logManagerExt = new LogManagerExtStor(getApplicationContext());

    }

    protected void onDestroy() {
        super.onDestroy();
        AudioUtil.getInstance().stopSound();
        memoryFree();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.logManagerExt.saveLogInFile();
    }

    /**
     * Muda a cor do botão clicado de acordo com o resultado do chute. A cor do botão mudará para verde caso acerte, caso não, mudará para vermelho.
     * @param letraClicada a letra que o usuário chutou
     * @param btnClicado botão correspondente a essa letra
     */
    public void feedbackColorButtonLetter(String letraClicada, Button btnClicado) {
        int resultado = challengeFacade.getAttempResult(letraClicada);
        //int resultado =  ChallengeFacade.getInstance().getAttempResult(letraClicada);

        if (resultado == ChallengeFacade.ATTEMPT_ACEPTED) {
            btnClicado.setBackgroundResource(R.drawable.greem_rounded_backgroud);
        } else if (resultado == ChallengeFacade.ATTEMPT_EXISTS) {
            Toast.makeText(getApplicationContext(), "Você já chutou essa letra!", Toast.LENGTH_SHORT).show();
        } else { // ATTEMP_REJECTED
            btnClicado.setBackgroundResource(R.drawable.red_rounded_backgroud);

            Log.i("Json-Log", challengeFacade.getCurrentChallenge().getWord() + " - " + letraClicada);
            this.logManagerExt.addNewErro(challengeFacade.getCurrentChallenge().getWord(), letraClicada); //Adicionando caso de erro ao arquivo JSON
            /*Log.i("Json-Log", ChallengeFacade.getInstance().getCurrentChallenge().getWord() + " - " + letraClicada);
            this.logManagerExt.addNewErro(ChallengeFacade.getInstance().getCurrentChallenge().getWord(), letraClicada); //Adicionando caso de erro ao arquivo JSON*/
        }
    }

    /**
     * Atualiza os dados da tela como a img da forca e o TextView com o underscore
     * @param letraClicada letra que o usuário chutou
     * @param btnClicado botão correspondente a letra
     */
    public void updateData(String letraClicada, Button btnClicado) {

        // Mudando cor do botão
        feedbackColorButtonLetter(letraClicada, btnClicado);

        // Atualiza a imagem da forca de acordo com a quantidade de erros
        initForca();

        // Setando o text view com o novo underscore
        setUnderscoreInTextview(challengeFacade.getCurrentUnderscore());
        //setUnderscoreInTextview(ChallengeFacade.getInstance().getCurrentUnderscore());


        verifyChallengeItsOver();
    }

    /**
     * Atualiza a imagem da forca
     */
    private void initForca() {
        forcaController.mudaForca(challengeFacade.getErroCount());
        //forcaController.mudaForca(ChallengeFacade.getInstance().getErroCount());
    }

    /**
     * Verifica se a quantidade máxima de erros foi atingida ou se o usuário já acertou a palavra
     */
    private void verifyChallengeItsOver() {
        Intent it = new Intent(this, ProgressActivity.class);

        if (ChallengeFacade.getInstance().getErroCount() == QTD_MAX_ERROS || ChallengeFacade.getInstance().checkWordAccepted()) {
            ChallengeFacade.getInstance().increaseTime(cronometro.parandoCronometroEPegandoTempo());
            startActivity(it);
            finish();
        }

    }

    /**
     * Seta o text view com o underscore da palavra
     *
     * @param underscore underscore da palavra
     */
    private void setUnderscoreInTextview(String underscore) {
        TextView txtUnderscore = findViewById(R.id.txt_underscore);
        txtUnderscore.setText(challengeFacade.getUnderscoreWithSpaces());
        //txtUnderscore.setText(setSpacesInWord(underscore));
    }

    /**
     * Dá um espaço entre as letras para o usuário poder ver quantas letras a palavra tem
     * @param underscore underscore da palavra a ser acertada
     * @return o underscore com as letras espaçadas
     */
    private String setSpacesInWord(String underscore) {
        StringBuilder novaString = new StringBuilder();

        for(int i = 0; i < underscore.length(); i++) {
            novaString.append(underscore.charAt(i)) ;
            novaString.append(" ");
        }

        return novaString.toString();
    }

    /**
     * Toca o som da palavra
     *
     * @param v view
     */
    public void playWordSound(View v) {
        String soundUrl = challengeFacade.getCurrentChallenge().getSoundUrl();
        //String soundUrl = ChallengeFacade.getInstance().getCurrentChallenge().getSoundUrl();
        if (soundUrl != null && !soundUrl.equals("")) {
            if (soundUrl.startsWith("http")) {
                // TODO obter som da URL, se for URL
            } else if (TextUtil.isAllInteger(soundUrl)) {
                AudioUtil.getInstance().playSound(Integer.parseInt(soundUrl));
            } else {
                AudioUtil.getInstance().speakWord(challengeFacade.getCurrentChallenge().getWord());
                //AudioUtil.getInstance().speakWord(ChallengeFacade.getInstance().getCurrentChallenge().getWord());
            }
        } else {
            AudioUtil.getInstance().speakWord(challengeFacade.getCurrentChallenge().getWord());
            //AudioUtil.getInstance().speakWord(ChallengeFacade.getInstance().getCurrentChallenge().getWord());
        }
    }

    /***
     * Evento de click em letra do teclado. A letra clicada e o botão são enviados para serem analizados e tratados de acordo com o desafio.
     * @param view Botão clicado
     */
    public void letterClick(View view){
        Button btClick = (Button) view;
        updateData(btClick.getText().toString().toLowerCase(), btClick);
        Log.i("Botão clicado:", btClick.getText().toString());
    }

}
