package br.ufpb.dcx.appalpha.view.activities.theme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.ChallengeFacade;
import br.ufpb.dcx.appalpha.control.service.ThemeSqlService;
import br.ufpb.dcx.appalpha.control.util.ScreenUtil;
import br.ufpb.dcx.appalpha.control.util.SomUtil;
import br.ufpb.dcx.appalpha.control.api.RetrofitInitializer;
import br.ufpb.dcx.appalpha.model.bean.Theme;
import br.ufpb.dcx.appalpha.view.activities.AddThemeActivity;
import br.ufpb.dcx.appalpha.view.activities.ForcaActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ThemeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ThemeActivity";
    private GridLayoutManager layManager;
    private RecyclerView recyclerView;
    private List<Theme> themes = new ArrayList<>();
    protected static Activity activity;
    private ThemeSqlService themeSqlService;
    private FloatingActionButton fabAddTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tema);
        getLayoutInflater().inflate(R.layout.activity_tema, null);

        recyclerView = findViewById(R.id.rcThemes);
        layManager = new GridLayoutManager(getApplicationContext(), 2);
        this.themeSqlService = ThemeSqlService.getInstance(getApplicationContext());
        fabAddTheme = findViewById(R.id.fabAddTheme);
        fabAddTheme.setOnClickListener(this);
    }

    public void fillRecycleView(List<Theme> themes){
        recyclerView.setLayoutManager(layManager);
        recyclerView.setAdapter(new ThemeAdapter(themes, getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        addDefaultThemes();
        fillRecycleView(themes);
        ScreenUtil.getInstance().unlockScreenTouch(this);
        activity = this;
    }

    public void addDefaultThemes(){
        this.themes = this.themeSqlService.getAll();
    }

    public static void OnClickListener(OnClickListener hook){
        Theme selectedTheme = hook.onItemClicked();
        Log.i(TAG, "Theme " + selectedTheme.getName() + " Clicked!");
        if(selectedTheme.getChallenges() != null && selectedTheme.getChallenges().size() > 0) {
            ThemeActivity.goToSelectedChallenge(selectedTheme);
            ScreenUtil.getInstance().lockScreenTouch(ThemeActivity.activity);
        }else{
            Toast.makeText(ThemeActivity.activity, "O tema selecionado não possui desafios, tente outro tema.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.fabAddTheme):
                Intent intent = new Intent(this, AddThemeActivity.class);
                this.startActivity(intent);
                break;
        }
    }

    interface OnClickListener{
        Theme onItemClicked();
    }

    private static void setChallengesInFacade(Theme selectedTheme){
        ChallengeFacade.getInstance().init(selectedTheme.getChallenges(), selectedTheme);
    }

    private static void goToSelectedChallenge(Theme selectedTheme){
        setChallengesInFacade(selectedTheme);
        playThemeSong(selectedTheme);

        Intent intent = new Intent(ThemeActivity.activity, ForcaActivity.class);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() { //Wait the song end to start new activity
            @Override
            public void run() {
                ThemeActivity.activity.startActivity(intent);

            }
        }, SomUtil.getInstance().getDuracao());

    }

    private static void playThemeSong(Theme selectedTheme){
        SomUtil.getInstance().playSound(ThemeActivity.activity, Integer.parseInt(selectedTheme.getSoundUrl()));
    }

    public void botaoEscolha(ImageView img_button) {

    }

}
