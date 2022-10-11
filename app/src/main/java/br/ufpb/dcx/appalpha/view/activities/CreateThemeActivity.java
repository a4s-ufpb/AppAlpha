package br.ufpb.dcx.appalpha.view.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.service.ThemeSqlService;
import br.ufpb.dcx.appalpha.control.util.ImageLoadUtil;
import br.ufpb.dcx.appalpha.control.util.ImgurHelper;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;
import br.ufpb.dcx.appalpha.view.activities.theme.ThemeActivity;


public class CreateThemeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CreateThemeActivity";
    private ImageButton back_btn;

    private ThemeSqlService service;

    private RecyclerView recyclerView;
    private GridLayoutManager layManager;

    public ArrayList<Challenge> palavras;

    private TextInputLayout tlIdPalavra;
    private String urlImagePalavra;
    private ImageView imagemPalavra;

    private TextInputLayout tlIdTema;
    private String urlImageTema;
    private ImageView imagemTema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.palavras = new ArrayList<Challenge>();

        service = ThemeSqlService.getInstance(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_theme);
        back_btn = findViewById(R.id.btn_back);
        back_btn.setOnClickListener(this);

        tlIdPalavra = findViewById(R.id.tlIdPalavra);
        imagemPalavra = findViewById(R.id.imageView7);

        tlIdTema = findViewById(R.id.tlIdTheme2);
        imagemTema = findViewById(R.id.imageView72);

        recyclerView = findViewById(R.id.palavrasRows);
        layManager = new GridLayoutManager(getApplicationContext(), 2);

        imagemTema.setOnClickListener(v -> {
            ImagePicker.with(CreateThemeActivity.this)
                        .crop()
                        .compress(1024)
                        .start(1);
        });

        imagemPalavra.setOnClickListener(v -> {
            ImagePicker.with(CreateThemeActivity.this)
                    .crop()
                    .compress(1024)
                    .start(2);
        });

        findViewById(R.id.buttonAddPalavra).setOnClickListener(v -> {
            String palavra = tlIdPalavra.getEditText().getText().toString();

            if(palavra == null || palavra.length() == 0) {
                Toast.makeText(getApplicationContext(), "Insira uma Palavra", Toast.LENGTH_LONG).show();
                return;
            }
            if(urlImagePalavra == null || urlImagePalavra.length() == 0) {
                Toast.makeText(getApplicationContext(), "Insira uma Imagem para a palavra", Toast.LENGTH_LONG).show();
                return;
            }

            Challenge palavaNova = new Challenge(palavra, null, null, urlImagePalavra);
            palavras.add(palavaNova);
            recyclerView.getAdapter().notifyItemInserted(palavras.indexOf(palavaNova));

            tlIdPalavra.getEditText().setText(null);
            urlImagePalavra = null;
            imagemPalavra.setImageResource(android.R.drawable.ic_menu_gallery);
        });

        fillRecycleView();
    }

    public void saveChanges(View v)
    {
        String temaNome = tlIdTema.getEditText().getText().toString();

        if(temaNome == null || temaNome.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insira um nome para o Tema", Toast.LENGTH_LONG).show();
            return;
        }

        if(urlImageTema == null || urlImageTema.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insira uma Imagem para o Tema", Toast.LENGTH_LONG).show();
            return;
        }

        if(palavras.size() == 0) {
            Toast.makeText(getApplicationContext(), "Adicione algumas palavras ao Tema", Toast.LENGTH_LONG).show();
            return;
        }

        salvarTemaAtual();

        Intent intent = new Intent(CreateThemeActivity.this, ThemeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void salvarTemaAtual()
    {
        Theme newTheme = new Theme(tlIdTema.getEditText().getText().toString(),  urlImageTema, null, null);
        newTheme.setDeletavel(true);
        service.insert(newTheme, palavras);
        Toast.makeText(CreateThemeActivity.this, String.format("Tema '%s' Salvo com Sucesso!", newTheme.getName()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            Uri dataUri = data.getData();
            File dataFile = null;
            try {
                dataFile = new File(new URI(dataUri.toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            sendImage(dataFile, requestCode);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateImagemPalavra()
    {
        ImageLoadUtil.getInstance().loadImage(urlImagePalavra, imagemPalavra, getApplicationContext());
    }

    public void updateImagemTema()
    {
        ImageLoadUtil.getInstance().loadImage(urlImageTema, imagemTema, getApplicationContext());
    }

    public void sendImage(File fileImage, int requestCode)
    {
        ImgurHelper.getImageLink(getApplicationContext(), fileImage, null, new ImgurHelper.ImgurHelperCompletionHandler() {
            @Override
            public void success(String link) {
                Log.w(TAG, "ImgurHelper success: "+ link);
                if(requestCode == 1) {
                    urlImageTema = link;
                    updateImagemTema();
                } else if(requestCode == 2) {
                    urlImagePalavra = link;
                    updateImagemPalavra();
                }
            }

            @Override
            public void failed(String reason) {
                Log.w(TAG,"ImgurHelper: failed: "+ reason);
                Toast.makeText(CreateThemeActivity.this, "ImgurHelper: Error "+reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fillRecycleView()
    {
        recyclerView.setLayoutManager(layManager);
        recyclerView.setAdapter(new CreatePalavraAdapter(this, getApplicationContext(), true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.btn_back):
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
