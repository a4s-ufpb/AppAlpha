package com.example.anaplb.appalpha.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.anaplb.appalpha.R;
import com.example.anaplb.appalpha.config.AppConfig;

public class ConfigActivity extends AppCompatActivity {
    private AppConfig configurator;
    private RadioGroup rgLetterType;
    private RadioButton rbCasual;
    private RadioButton rbCursiva;
    private RadioButton rbBastao;
    private RadioButton rbImprensa;
    private RadioGroup rgLetterCase;
    private RadioButton rbUpper;
    private RadioButton rbLower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        this.configurator = AppConfig.getInstance(getApplicationContext());

        this.rgLetterType = findViewById(R.id.rgLetterType);
        this.rgLetterCase = findViewById(R.id.rgLetterCase);
        this.rbCasual = findViewById(R.id.rb_casual);
        this.rbCursiva = findViewById(R.id.rb_cursiva);
        this.rbBastao = findViewById(R.id.rb_bastao);
        this.rbImprensa = findViewById(R.id.rb_imprensa);
        this.rbUpper = findViewById(R.id.rb_uppercase);
        this.rbLower = findViewById(R.id.rb_lowercase);




    }

    @Override
    protected void onResume() {
        super.onResume();
        loadConfigsInView();
    }

    public void backToMainScreen(View view){
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pushChanges();
        this.configurator.saveAllChange(getApplicationContext());
    }

    private void pushChanges(){
        String rgSelectedLetterType =((RadioButton)findViewById(this.rgLetterType.getCheckedRadioButtonId())).getText().toString();
        String rgSelectedLetterCase =((RadioButton)findViewById(this.rgLetterCase.getCheckedRadioButtonId())).getText().toString();
        this.configurator.setCurrentLetterType(rgSelectedLetterType);
        this.configurator.setCurrentLetterCase(rgSelectedLetterCase);
    }

    private void loadConfigsInView(){
        Log.i("Json-Config","Entrou em LoadConfigs");
        Log.i("Json-Config","CurrentLetterType: " + this.configurator.getCurrentLetterType());
        Log.i("Json-Config","CurrentLetterCase: " + this.configurator.getCurrentLetterCase());
        switch(this.configurator.getCurrentLetterType()){
            case(AppConfig.CASUAL):
                rgLetterType.check(rbCasual.getId());
            break;

            case(AppConfig.CURSIVA):
                rgLetterType.check(rbCursiva.getId());
            break;

            case(AppConfig.BASTAO):
                rgLetterType.check(rbBastao.getId());
            break;

            case(AppConfig.IMPRENSA):
                rgLetterType.check(rbImprensa.getId());
            break;

        }

        switch(this.configurator.getCurrentLetterCase()){
            case(AppConfig.UPPER):
                rgLetterCase.check(rbUpper.getId());
            break;

            case(AppConfig.LOWER):
                rgLetterCase.check(rbLower.getId());
            break;

        }
    }


}
