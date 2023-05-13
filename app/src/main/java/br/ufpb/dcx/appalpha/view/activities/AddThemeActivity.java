package br.ufpb.dcx.appalpha.view.activities;

import android.content.Intent;
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
import br.ufpb.dcx.appalpha.view.activities.theme.ThemeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class of Activity to import themes via API
 */
public class AddThemeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddThemeActivity";
    private TextInputLayout tlIdTheme;
    private Button btnImport;
    private ThemesApiService themesApiService;
    private ThemeSqlService themeSqlService;
    private ImageButton back_btn;

    /**
     * On create activity, setup local variables
     *
     * @param savedInstanceState
     */
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

        openKeyboard();

        findViewById(R.id.theme_id_field).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    onClick(findViewById(R.id.btnImport));
                }
                return false;
            }
        });
    }

    /**
     * Action for open keyboard and focus on filed box
     */
    public void openKeyboard() {
        TextView editText1 = findViewById(R.id.theme_id_field);
        editText1.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * Get theme Id from field box
     *
     * @return
     */
    public long getInputThemeID() {
        long ret = -1;
        try {
            ret = Integer.parseInt(tlIdTheme.getEditText().getText().toString());
        } catch (Exception e) {
        }
        return ret;
    }

    /**
     * Listen to detect click on any view in the activity
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btnImport):
                long themeId = getInputThemeID();
                if (themeId == -1) {
                    Toast.makeText(getApplicationContext(), "Erro ao recuperar tema, verifique se o id inserido é válido.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!themeSqlService.existsByApiId(themeId)) {
                    Call call = new RetrofitInitializer().contextService().find(themeId);
                    call.enqueue(new Callback<Theme>() {
                        @Override
                        public void onResponse(Call<Theme> call, Response<Theme> response) {
                            if (response.body() != null) {
                                Theme theme = response.body();

                                // theme Id is ur apiId
                                theme.setApiId(theme.getId());
                                theme.setId(null);

                                try {
                                    themeSqlService.insert(theme, theme.getChallenges());
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Erro ao salvar Tema: "+theme.getName(), Toast.LENGTH_LONG).show();
                                } finally {
                                    Toast.makeText(getApplicationContext(), "Tema " + theme.getName() + " importado com sucesso!", Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(AddThemeActivity.this, ThemeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), "Erro ao recuperar tema, verifique se o id inserido é válido.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Theme> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Ocorreu um erro ao recuperar o Tema de ID: " + themeId, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Tema de ID " + themeId + " já está importado. Tente outro tema.", Toast.LENGTH_SHORT).show();
                }

                break;

            case (R.id.back_btn):
                finish();
                break;
        }
    }

    /**
     * Action call for Back button press
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
