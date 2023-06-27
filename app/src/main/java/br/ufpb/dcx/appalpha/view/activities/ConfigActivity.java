package br.ufpb.dcx.appalpha.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.config.AppConfig;

/**
 * Class for activity of settings manager options of App
 */
public class ConfigActivity extends AppCompatActivity {
    private AppConfig appConfig;
    private RadioGroup rgLetterType;
    private RadioButton rbCasual;
    private RadioButton rbCursiva;
    private RadioButton rbBastao;
    private RadioGroup rgLetterCase;
    private RadioButton rbUpper;
    private RadioButton rbLower;

    /**
     * On create activity, setup local variables
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        this.appConfig = AppConfig.getInstance(getApplicationContext());

        this.rgLetterType = findViewById(R.id.rgLetterType);
        this.rgLetterCase = findViewById(R.id.rgLetterCase);
        this.rbCasual = findViewById(R.id.rb_casual);
        this.rbCursiva = findViewById(R.id.rb_cursiva);
        this.rbBastao = findViewById(R.id.rb_bastao);
        this.rbUpper = findViewById(R.id.rb_uppercase);
        this.rbLower = findViewById(R.id.rb_lowercase);
    }

    /**
     * When return back to activity update the settings in view
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadConfigsInView();
    }

    /**
     * Action for Close the activity
     * @param view
     */
    public void backToMainScreen(View view){
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Retrieve current settings in the view and set to local variable.
     */
    private void pushChanges(){
        String rgSelectedLetterType =((RadioButton)findViewById(this.rgLetterType.getCheckedRadioButtonId())).getText().toString();
        String rgSelectedLetterCase =((RadioButton)findViewById(this.rgLetterCase.getCheckedRadioButtonId())).getText().toString();
        this.appConfig.setCurrentLetterType(rgSelectedLetterType);
        this.appConfig.setCurrentLetterCase(rgSelectedLetterCase);
    }

    /**
     * Load the saved settings and update the current status in view
     */
    private void loadConfigsInView(){
        switch(this.appConfig.getCurrentLetterType()){
            case(AppConfig.CASUAL):
                rgLetterType.check(rbCasual.getId());
            break;

            case(AppConfig.CURSIVA):
                rgLetterType.check(rbCursiva.getId());
            break;

            case(AppConfig.BASTAO):
                rgLetterType.check(rbBastao.getId());
            break;

        }

        switch(this.appConfig.getCurrentLetterCase()){
            case(AppConfig.UPPER):
                rgLetterCase.check(rbUpper.getId());
            break;

            case(AppConfig.LOWER):
                rgLetterCase.check(rbLower.getId());
            break;

        }
    }

    /**
     * Save all settings and reload the screen
     * @param view
     */
    public void saveChanges(View view){
        pushChanges();
        this.appConfig.saveAllChange();
        this.recreate();
        Toast.makeText(this, "Configurações salvas com sucesso!", Toast.LENGTH_SHORT).show();
    }

}
