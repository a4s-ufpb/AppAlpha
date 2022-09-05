package br.ufpb.dcx.appalpha.view.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.api.RetrofitInitializer;
import br.ufpb.dcx.appalpha.control.service.ThemeSqlService;
import br.ufpb.dcx.appalpha.control.service.ThemesApiService;
import br.ufpb.dcx.appalpha.model.bean.Theme;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddThemeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddThemeActivity";
    private TextInputLayout tlIdTheme;
    private Button btnImport;
    private ThemesApiService themesApiService;
    private ThemeSqlService themeSqlService;
    private ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_theme);
        tlIdTheme = findViewById(R.id.tlIdTheme);
        btnImport = findViewById(R.id.btnImport);
        themesApiService = ThemesApiService.getInstance();
        themeSqlService = ThemeSqlService.getInstance(getApplicationContext());
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

        btnImport.setOnClickListener(this);

        abrirTeclado();

        findViewById(R.id.theme_id_field).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    onClick(findViewById(R.id.btnImport));
                }
                return false;
            }
        });
    }

    public void abrirTeclado()
    {
        TextView editText1 = findViewById(R.id.theme_id_field);
        editText1.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public long getInputThemeID()
    {
        long ret = -1;
        try {
            ret = Integer.parseInt(tlIdTheme.getEditText().getText().toString());
        }catch (Exception e) {
        }
        return ret;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.btnImport):
                long themeId = getInputThemeID();
                if (themeId==-1)
                {
                    Toast.makeText(getApplicationContext(), "Erro ao recuperar tema, verifique se o id inserido é válido.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!themeSqlService.existsByApiId(themeId)) {
                    Call call = new RetrofitInitializer().contextService().find(themeId);
                    call.enqueue(new Callback<Theme>() {
                        @Override
                        public void onResponse(Call<Theme> call, Response<Theme> response) {
                            if(response.body() != null){
                                Theme theme = response.body();
                                Log.i(TAG, "Theme challenges:" + theme.getChallenges().size());

                                themeSqlService.insert(theme, theme.getChallenges());
                                Toast.makeText(getApplicationContext(), "Tema " + theme.getName() + " importado com sucesso!", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Theme recuperado com sucesso!");
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "Erro ao recuperar tema, verifique se o id inserido é válido.", Toast.LENGTH_LONG).show();
                                Log.i(TAG, "Erro ao recuperar theme com id " + themeId);
                            }
                        }

                        @Override
                        public void onFailure(Call<Theme> call, Throwable t) {
                            Log.e(TAG, "Erro ao recuperar tema: "+t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Tema de ID " + themeId + " já está importado. Tente outro tema.", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Tema de id " + themeId + " já existe!");
                }

                break;

            case(R.id.back_btn):
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
