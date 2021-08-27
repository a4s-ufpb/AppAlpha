package br.ufpb.dcx.appalpha.view.activities.theme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import br.ufpb.dcx.appalpha.control.util.AudioUtil;
import br.ufpb.dcx.appalpha.control.util.ScreenUtil;
import br.ufpb.dcx.appalpha.control.util.TextUtil;
import br.ufpb.dcx.appalpha.model.bean.Theme;
import br.ufpb.dcx.appalpha.view.activities.AddThemeActivity;
import br.ufpb.dcx.appalpha.view.activities.ForcaActivity;
import br.ufpb.dcx.appalpha.view.activities.ViewAnimation;


public class ThemeActivity extends AppCompatActivity {
    private static final String TAG = "ThemeActivity";
    private GridLayoutManager layManager;
    private RecyclerView recyclerView;
    private List<Theme> themes = new ArrayList<>();
    protected static Activity activity;
    private ThemeSqlService themeSqlService;
    private FloatingActionButton fabMore;
    private FloatingActionButton fabDel;
    private FloatingActionButton fabAdd;
    boolean isRotate = false;
    private boolean isDeleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tema);
        getLayoutInflater().inflate(R.layout.activity_tema, null);

        recyclerView = findViewById(R.id.rcThemes);
        layManager = new GridLayoutManager(getApplicationContext(), 2);
        this.themeSqlService = ThemeSqlService.getInstance(getApplicationContext());
        fabMore = findViewById(R.id.fabMore);
        fabDel = findViewById(R.id.fabDel);
        fabAdd = findViewById(R.id.fabAdd);
        AudioUtil.getInstance(getApplicationContext());

        ViewAnimation.init(fabDel);
        ViewAnimation.init(fabAdd);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddThemeActivity.class);
            this.startActivity(intent);
        });

        fabDel.setOnClickListener(v -> {
            this.isDeleteMode = !this.isDeleteMode;
            this.fillRecycleView(this.themes);
        });

        this.fabMore.setOnClickListener(view -> {
            this.isRotate = ViewAnimation.rotateFab(view, !this.isRotate);
            if(isRotate){
                ViewAnimation.showIn(fabAdd);
                ViewAnimation.showIn(fabDel);
            }else{
                ViewAnimation.showOut(fabAdd);
                ViewAnimation.showOut(fabDel);
            }
        });
    }

    public void updateRecycleView() {
        getAllThemes();
        fillRecycleView(themes);
    }

    public void fillRecycleView(List<Theme> themes){
        recyclerView.setLayoutManager(layManager);
        recyclerView.setAdapter(new ThemeAdapter(themes, getApplicationContext(), this.isDeleteMode));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateRecycleView();
        ScreenUtil.getInstance().unlockScreenTouch(this);
        activity = this;
    }

    public void getAllThemes(){
        this.themes = this.themeSqlService.getAll();
    }

    public static void clickInGoToSelectedTheme(Theme selectedTheme){
        Log.i(TAG, "Theme " + selectedTheme.getName() + " Clicked!");
        if(selectedTheme.getChallenges() != null && selectedTheme.getChallenges().size() > 0) {
            ThemeActivity.goToSelectedChallenge(selectedTheme);
            ScreenUtil.getInstance().lockScreenTouch(ThemeActivity.activity);
        }else{
            Toast.makeText(ThemeActivity.activity, "O tema selecionado não possui desafios, tente outro tema.", Toast.LENGTH_LONG).show();
        }
    }

    private static void setChallengesInFacade(Theme selectedTheme){
        ChallengeFacade.getInstance().init(selectedTheme.getChallenges(), selectedTheme);
    }

    private static void goToSelectedChallenge(Theme selectedTheme){
        setChallengesInFacade(selectedTheme);
        playThemeSong(selectedTheme);

        Intent intent = new Intent(ThemeActivity.activity, ForcaActivity.class);
        Handler handler = new Handler();
        //Wait the song end to start new activity
        handler.postDelayed(() -> ThemeActivity.activity.startActivity(intent), AudioUtil.getInstance().getDuration());

    }

    private static void playThemeSong(Theme selectedTheme) {
        Log.i(TAG, selectedTheme.toString());
        String soundUrl = selectedTheme.getSoundUrl();
        if (soundUrl != null && !soundUrl.equals("")) {
            if (soundUrl.startsWith("http")) {
                // TODO obter som da URL, se for URL
            } else if (TextUtil.isAllInteger(soundUrl)) { // caso seja um tema interno
                AudioUtil.getInstance().playSound(Integer.parseInt(soundUrl));
            } else {
                AudioUtil.getInstance().speakWord(selectedTheme.getName());
            }
        } else { // Nesse caso, pode falar usando sintetização de voz
            AudioUtil.getInstance().speakWord(selectedTheme.getName());
        }
    }

}
