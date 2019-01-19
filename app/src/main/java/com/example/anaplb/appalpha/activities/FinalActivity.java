package com.example.anaplb.appalpha.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anaplb.appalpha.R;

public class FinalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent it = getIntent();


        TextView txt = findViewById(R.id.textView);
        ImageView img = findViewById(R.id.imageView9);
        int pontuacao = it.getIntExtra("pontuacao", 0);

        pontuacao(img, pontuacao);


        /*if(ganhou) {
            img.setImageResource(R.drawable.meninafeliz);
            txt.setText(R.string.venceu);
        } else {
            txt.setText(R.string.perdeu);
            img.setImageResource(R.drawable.meninotriste);
        }*/
    }

    public void pontuacao(ImageView img, int pontuacao) {

        switch (pontuacao) {

            case 0:
                img.setImageResource(R.drawable.zero);
                break;

            case 1:
                img.setImageResource(R.drawable.um);
                break;

            case 2:
                img.setImageResource(R.drawable.dois);
                break;

            case 3:
                img.setImageResource(R.drawable.tres);
                break;

            case 4:
                img.setImageResource(R.drawable.quatro);
                break;

            case 5:
                img.setImageResource(R.drawable.cinco);
                break;
        }

    }

    public void jogarNovamente(View v) {
        Intent it = new Intent(getApplicationContext(), TemaActivity.class);
        startActivity(it);
        finish();
    }

    public void sair(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}