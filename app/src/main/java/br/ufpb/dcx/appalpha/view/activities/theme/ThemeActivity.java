package br.ufpb.dcx.appalpha.view.activities.theme;

import android.app.Activity;
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
import br.ufpb.dcx.appalpha.view.activities.AddThemeManagerActivity;
import br.ufpb.dcx.appalpha.view.activities.ForcaActivity;
import br.ufpb.dcx.appalpha.view.activities.ViewAnimation;

/**
 * Class to Activity of Principal Theme Panel List
 */
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
    private FloatingActionButton fabEdit;
    boolean isRotate = false;
    private boolean isDeleteMode = false;
    private boolean isEditMode = false;

    /**
     * On create activity, setup local variables
     * @param savedInstanceState
     */
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
        fabEdit = findViewById(R.id.fabEdit);
        AudioUtil.getInstance(getApplicationContext());

        ViewAnimation.init(fabDel);
        ViewAnimation.init(fabAdd);
        ViewAnimation.init(fabEdit);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddThemeManagerActivity.class);
            this.startActivity(intent);
            toggleMoreAction(false);
        });

        fabDel.setOnClickListener(v -> {
            this.isDeleteMode = !this.isDeleteMode;
            this.fillRecycleView(this.themes);
            toggleMoreAction(false);
        });

        fabEdit.setOnClickListener(v -> {
            this.isEditMode = !this.isEditMode;
            this.fillRecycleView(this.themes);
            toggleMoreAction(false);
        });

        this.fabMore.setOnClickListener(view -> {
            toggleMoreAction(!this.isRotate);
        });
    }

    /**
     * Action of button more, in the bottom right
     * @param show
     */
    public void toggleMoreAction(boolean show)
    {
        this.isRotate = ViewAnimation.rotateFab(fabMore, show);
        if(isRotate){
            ViewAnimation.showIn(fabAdd);
            ViewAnimation.showIn(fabEdit);
            ViewAnimation.showIn(fabDel);
        }else{
            ViewAnimation.showOut(fabAdd);
            ViewAnimation.showOut(fabEdit);
            ViewAnimation.showOut(fabDel);
        }
    }

    /**
     * Update view list of Themes
     */
    public void updateRecycleView() {
        getAllThemes();
        fillRecycleView(themes);
    }

    /**
     * Populate View with list of Themes
     * @param themes
     */
    public void fillRecycleView(List<Theme> themes){
        recyclerView.setLayoutManager(layManager);
        recyclerView.setAdapter(new ThemeAdapter(themes, getApplicationContext(), this.isDeleteMode, this.isEditMode));
    }

    /**
     * when return to activity update content of activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.updateRecycleView();
        ScreenUtil.getInstance().unlockScreenTouch(this);
        activity = this;
    }

    /**
     * Get all stored list themes
     */
    public void getAllThemes(){
        this.themes = this.themeSqlService.getAll();
    }

    /**
     * Action of pressed Theme button
     * @param selectedTheme
     */
    public static void clickInGoToSelectedTheme(Theme selectedTheme){
        Log.i(TAG, "Theme " + selectedTheme.getName() + " Clicked!");
        if(selectedTheme.getChallenges() != null && selectedTheme.getChallenges().size() > 0) {
            ThemeActivity.goToSelectedChallenge(selectedTheme);
            ScreenUtil.getInstance().lockScreenTouch(ThemeActivity.activity);
        }else{
            Toast.makeText(ThemeActivity.activity, "O tema selecionado não possui desafios, tente outro tema.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Set the selected Theme and Challenges in the Facade
     * @param selectedTheme
     */
    private static void setChallengesInFacade(Theme selectedTheme){
        ChallengeFacade.getInstance().init(selectedTheme.getChallenges(), selectedTheme);
    }

    /**
     * Setup and Open the activity with selected Theme
     * @param selectedTheme
     */
    private static void goToSelectedChallenge(Theme selectedTheme){
        setChallengesInFacade(selectedTheme);
        playThemeSong(selectedTheme);

        Intent intent = new Intent(ThemeActivity.activity, ForcaActivity.class);
        Handler handler = new Handler();
        //Wait the song end to start new activity
        handler.postDelayed(() -> ThemeActivity.activity.startActivity(intent), AudioUtil.getInstance().getDuration());
    }

    /**
     * Play sound of Theme selected
     * @param selectedTheme
     */
    private static void playThemeSong(Theme selectedTheme) {
        Log.i(TAG, selectedTheme.toString());
        String soundUrl = selectedTheme.getSoundUrl();
        if (soundUrl != null && !soundUrl.equals("")) {
            if (soundUrl.startsWith("http")) {
                AudioUtil.getInstance().playSoundURL(soundUrl);
            } else if (TextUtil.isAllInteger(soundUrl)) { // caso seja um tema interno
                AudioUtil.getInstance().playSound(Integer.parseInt(soundUrl));
            } else {
                AudioUtil.getInstance().speakWord(selectedTheme.getName());
                AudioUtil.getInstance().esperarTssParar();
            }
        } else { // Nesse caso, pode falar usando sintetização de voz
            AudioUtil.getInstance().speakWord(selectedTheme.getName());
            AudioUtil.getInstance().esperarTssParar();
        }
    }

}
