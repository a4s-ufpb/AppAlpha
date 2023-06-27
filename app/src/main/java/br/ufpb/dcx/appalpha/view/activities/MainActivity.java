package br.ufpb.dcx.appalpha.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.PermissionControll;
import br.ufpb.dcx.appalpha.control.api.ApiConfig;
import br.ufpb.dcx.appalpha.control.dbhelper.DbHelper;
import br.ufpb.dcx.appalpha.control.service.MockThemes;
import br.ufpb.dcx.appalpha.view.activities.theme.ThemeActivity;

/**
 * Class for activity of principal menu screen of App
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private PermissionControll pc;
    SharedPreferences sPreferences = null;
    public static Context mainContext = null;

    /**
     * On create activity, setup local variables
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = getApplicationContext();
        this.pc = new PermissionControll(this);
        pc.getReadExternalStoragePermission();
        pc.getWriteExternalStoragePermission();

        verifyFirstRunAndInjectDb();

        // init API Configurations
        ApiConfig.getInstance(getApplicationContext());
    }

    /**
     * Return the main Context
     *
     * @return
     */
    public static Context getMainContext() {
        return mainContext;
    }

    /**
     * Action for populate the database first time
     */
    public void verifyFirstRunAndInjectDb() {
        boolean sholdCreateDatabase = false;

        sPreferences = getSharedPreferences("firstRun", MODE_PRIVATE);
        if (sPreferences.getBoolean("firstRun", true)) {
            sPreferences.edit().putBoolean("firstRun", false).apply();
            sholdCreateDatabase = true;
            Log.i(TAG, "First Run");
        } else {
            Log.i(TAG, "Don't is the first Run");
        }

        File dataBaseFile = getApplicationContext().getDatabasePath(DbHelper.NAME);
        if (!dataBaseFile.exists()) {
            sholdCreateDatabase = true;
        }

        if (sholdCreateDatabase) {
            Log.i(TAG, "Criando Default Database.");
            MockThemes mt = new MockThemes(getApplicationContext());
            mt.run();
        }
    }

    /**
     * Action for open the record list screen
     *
     * @param v
     */
    public void goToRecords(View v) {
        Intent it = new Intent(getApplicationContext(), RecordsActivity.class);
        startActivity(it);
    }

    /**
     * Action for open the Theme list menu screen
     *
     * @param v
     */
    public void goToThemes(View v) {
        Intent it = new Intent(getApplicationContext(), ThemeActivity.class);
        startActivity(it);
    }

    public void exit(View v) {
        finish();
    }

    /**
     * Action to open the about screen
     *
     * @param v
     */
    public void goToAbout(View v) {
        Intent it = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(it);
    }

    /**
     * Action to open the app settings screen
     *
     * @param view
     */
    public void goToConfigScreen(View view) {
        Intent it = new Intent(getApplicationContext(), ConfigActivity.class);
        startActivity(it);
    }

    /**
     * Result of permission alert by the user
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionControll.READ_PERMISSION_REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission READ has been granted by user");

                } else {
                    Log.i(TAG, "Permission READ has been denied by user");
                }

            case PermissionControll.WRITE_PERMISSION_REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission WRITE has been granted by user");

                } else {
                    Log.i(TAG, "Permission WRITE has been denied by user");
                }
                return;
        }
    }
}
