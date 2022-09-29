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


public class AddThemeManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddThemeManagerActivity";
    private TextInputLayout tlIdTheme;
    private ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manager_theme);
        tlIdTheme = findViewById(R.id.tlIdTheme);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

        findViewById(R.id.criar_tema_menu).setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateThemeActivity.class);
            this.startActivity(intent);
        });

        findViewById(R.id.educapi_menu).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddThemeActivity.class);
            this.startActivity(intent);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
