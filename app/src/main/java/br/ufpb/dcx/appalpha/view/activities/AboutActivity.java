package br.ufpb.dcx.appalpha.view.activities;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import br.ufpb.dcx.appalpha.R;

/**
 * About activity screen
 */
public class AboutActivity extends AppCompatActivity {

    /**
     * On create activity, setup local variables
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }

    /**
     * Action to go back
     *
     * @param v
     */
    public void backToMenu(View v) {
        finish();
    }

    /**
     * Action to open url in browser
     *
     * @param view
     */
    public void redirectToUrl(View view) {
        Intent browserintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pt.freeimages.com"));
        startActivity(browserintent);
    }
}
