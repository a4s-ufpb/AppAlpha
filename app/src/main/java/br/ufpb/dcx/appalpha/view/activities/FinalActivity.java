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


public class FinalActivity extends AppCompatActivity {
    private static final int pontuacaoInicial = 1000;
    private RecordsSqlService recordeService;
    double pontuacaoFinal;

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
     * Pega o nome do jogador para colocar no recorde
     */
    public void savePlayerName() {
        EditText txt_nome = findViewById(R.id.edit_nome);
        String nome = txt_nome.getText().toString();

        inserindoNoBanco(pontuacaoFinal, nome);
    }


    /**
     * Através do botão no xml salva a pontuacao do jogador
     * @param v
     */
    public void saveRecord(View v) {
        savePlayerName();
        Toast.makeText(getApplicationContext(), "Recorde salvo com sucesso!", Toast.LENGTH_SHORT).show();
        sair();
    }

    /**
     * Cadastra pontuação do jogador
     * @param pontuacao pontuação do jogador
     * @param nome nome do jogador
     */
    public void inserindoNoBanco(double pontuacao, String nome) {
        recordeService.cadastrarNovoRecorde(pontuacao, nome);
    }

    /**
     * Diminui da pontuação inicial o tempo e os erros do usuário
     * @param tempo tempo que ele levou para terminar os desafios
     * @param erros erros que o usuário cometeu durante os desafios
     * @return pontuação final do usuário
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
     * Dependendo da pontuação do usuário mostra uma imagem com as estrelas que ele ganhou
     * @param img imageview que vai ser setada com uma das imagens das estrelas
     * @param pontuacao pontuação do usuário
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

    public void sair() {
        finish();
    }
}
