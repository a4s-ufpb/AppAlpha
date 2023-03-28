package br.ufpb.dcx.appalpha.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.ChallengeFacade;
import br.ufpb.dcx.appalpha.control.service.RecordsSqlService;
import br.ufpb.dcx.appalpha.control.util.AudioUtil;

/**
 * Class for activity to show points at end of game
 */
public class FinalActivity extends AppCompatActivity {
    private static final int pontuacaoInicial = 1000;
    private RecordsSqlService recordeService;
    double pontuacaoFinal;

    /**
     * On create activity, setup local variables
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        AudioUtil.getInstance(getApplicationContext()).playSound(R.raw.applause);

        recordeService = RecordsSqlService.getInstance(getApplicationContext());

        TextView txtPoints = findViewById(R.id.textView);
        ImageView img = findViewById(R.id.imageView9);

        // Pegando informações da activity anterior
        pontuacaoFinal = getPoints(ChallengeFacade.getInstance().getTime(), ChallengeFacade.getInstance().getSumError());

        pontuacao(img, pontuacaoFinal);

        txtPoints.setText(String.format("Sua pontuação final foi: %s", pontuacaoFinal));

    }

    /**
     * On dalloc activity stop TTS
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        AudioUtil.getInstance(getApplicationContext()).pararTSSePlayer();
    }

    /**
     * Save the name of player and points in the local database
     */
    public void savePlayerName() {
        EditText txt_nome = findViewById(R.id.edit_nome);
        String nome = txt_nome.getText().toString();

        inserindoNoBanco(pontuacaoFinal, nome);
    }


    /**
     * Action of save button
     * @param v
     */
    public void saveRecord(View v) {
        savePlayerName();
        Toast.makeText(getApplicationContext(), "Recorde salvo com sucesso!", Toast.LENGTH_SHORT).show();
        sair();
    }

    /**
     * Save the record of player in local database
     * @param pontuacao points of player
     * @param nome name of player
     */
    public void inserindoNoBanco(double pontuacao, String nome) {
        recordeService.cadastrarNovoRecorde(pontuacao, nome);
    }

    /**
     * Calculate the points of the player by time and attempt failed of letter
     * @param tempo time until player has finished the game
     * @param erros total error count the player has got
     * @return result of points the player have
     */
    public double getPoints(double tempo, int erros) {

        double pontuacao = pontuacaoInicial - ( (erros * 10) + (tempo * 100) );

        if(pontuacao < 0) {
            return 10;
        } else {
            return pontuacao;
        }
    }

    /**
     * Set the image of star relative of points of player
     * @param img imageview allocated to show the stars
     * @param pontuacao points of player
     */
    public void pontuacao(ImageView img, double pontuacao) {

        if(pontuacao < 0) {
            img.setImageResource(R.drawable.zero);
        } else if(pontuacao <= 200.0) {
            img.setImageResource(R.drawable.um);
        } else if(pontuacao <= 400.0) {
            img.setImageResource(R.drawable.dois);
        } else if(pontuacao <= 600.0) {
            img.setImageResource(R.drawable.tres);
        } else if(pontuacao < 900.0) {
            img.setImageResource(R.drawable.quatro);
        } else {
            img.setImageResource(R.drawable.cinco);
        }
    }

    /**
     * Action to Close the activity
     */
    public void sair() {
        finish();
    }
}
