package br.ufpb.dcx.appalpha.view.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.config.AlphaTextView;
import br.ufpb.dcx.appalpha.control.service.ChallengeSqlService;
import br.ufpb.dcx.appalpha.control.service.ThemeSqlService;
import br.ufpb.dcx.appalpha.control.util.ImageLoadUtil;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;
import br.ufpb.dcx.appalpha.view.activities.theme.ThemeActivity;
import br.ufpb.dcx.appalpha.view.fragment.search.SearchFragment;

/**
 * Class of activity for create, edit, remove, update themes and words by the user and save locally
 */
public class CreateThemeActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "CreateThemeActivity";
    private ImageButton back_btn;

    private ThemeSqlService themeSqlService;
    private ChallengeSqlService challengeSqlService;

    private RecyclerView recyclerView;
    private GridLayoutManager layManager;

    public Theme theme;

    private TextInputLayout tlIdTheme;
    private String urlImageTheme;
    private ImageView imageTheme;

    private boolean editMode = false;
    private Long editThemeID;

    private TextInputLayout tlIdWord;
    private String urlImageWord;
    private ImageView imageWord;

    public boolean editWordMode;
    public Challenge editWord;
    public List<Long> wordsID_Rem;
    public List<Challenge> words_Add;

    public HashMap<Object, String> imagePathToObjectMap;
    public String imageSavePath;

    /**
     * On create activity, setup local variables
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.wordsID_Rem = new ArrayList<Long>();
        this.words_Add = new ArrayList<Challenge>();
        this.imagePathToObjectMap = new HashMap<Object, String>();

        this.imageSavePath = getFilesDir().getAbsolutePath() + File.separator + "images";

        this.theme = new Theme(null, null, null, null);
        this.editWord = new Challenge(null, null,null,null,null);

        themeSqlService = ThemeSqlService.getInstance(getApplicationContext());
        challengeSqlService = ChallengeSqlService.getInstance(getApplicationContext());

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent != null) {
            Object editVal = intent.getSerializableExtra("editMode");
            if(editVal != null) {
                editMode = (boolean)editVal;
            }
            Object themeIDVal = intent.getSerializableExtra("themeID");
            if(themeIDVal != null) {
                editThemeID = (Long)themeIDVal;
            }
        }

        setContentView(R.layout.activity_create_theme);

        back_btn = findViewById(R.id.btn_back);
        back_btn.setOnClickListener(this);

        tlIdWord = findViewById(R.id.tlIdPalavra);
        imageWord = findViewById(R.id.imageView7);

        tlIdTheme = findViewById(R.id.tlIdTheme2);
        imageTheme = findViewById(R.id.imageView72);

        recyclerView = findViewById(R.id.palavrasRows);
        layManager = new GridLayoutManager(getApplicationContext(), 2);


        findViewById(R.id.areaImagemTema).setOnClickListener(v -> {
            showOptionsForSelectImage(1);
        });

        findViewById(R.id.areaImagemPalavra).setOnClickListener(v -> {
            showOptionsForSelectImage(2);
        });

        findViewById(R.id.buttonAddPalavra).setOnClickListener(v -> {
            String palavra = tlIdWord.getEditText().getText().toString();

            if(palavra == null || palavra.length() == 0) {
                Toast.makeText(getApplicationContext(), "Insira uma Palavra", Toast.LENGTH_LONG).show();
                return;
            }
            if(urlImageWord == null || urlImageWord.length() == 0) {
                Toast.makeText(getApplicationContext(), "Insira uma Imagem para a palavra", Toast.LENGTH_LONG).show();
                return;
            }

            if(this.editWordMode) {
                editWord.setImageUrl(urlImageWord);
                editWord.setWord(palavra.trim());
                updateEditModeWord(false);
                recyclerView.getAdapter().notifyItemChanged(theme.getChallenges().indexOf(editWord));
                Toast.makeText(getApplicationContext(), "Palavra \""+ editWord.getWord()+"\" foi atualizada.", Toast.LENGTH_LONG).show();
            } else {
                Challenge palavaNova = new Challenge(palavra.trim(), null, null, urlImageWord);
                theme.getChallenges().add(palavaNova);
                this.words_Add.add(palavaNova);
                recyclerView.getAdapter().notifyItemInserted(theme.getChallenges().indexOf(palavaNova));
                Toast.makeText(getApplicationContext(), "Palavra \""+palavaNova.getWord()+"\" foi adicionada.", Toast.LENGTH_LONG).show();
            }
            tlIdWord.getEditText().setText(null);
            urlImageWord = null;
            updateWordInfo();
        });

        findViewById(R.id.buttonAddPalavra2).setOnClickListener(v -> {
            updateEditModeWord(false);
        });

        if(this.editMode) {
            // Titulo para modo de ediçao
            AlphaTextView title = findViewById(R.id.textView19);
            title.setText("Editar Tema");

            // recuperar Tema da base de dados
            this.theme = themeSqlService.get(editThemeID);
            List<Challenge> storedChallenges = challengeSqlService.getRelatedChallenges(this.theme.getId());
            if(storedChallenges != null) {
                this.theme.setChallenges(storedChallenges);
            }

            Log.i(TAG, "Edit tema: "+ this.theme.toString());
        }

        updateThemeInfo();

        fillRecycleView();
    }

    /**
     * Action for Show alert option of select image for theme or word
     * @param TAG
     */
    public void showOptionsForSelectImage(int TAG) {

        AlertDialog alertDialog = new AlertDialog.Builder(CreateThemeActivity.this).create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

        Window win = alertDialog.getWindow();
        win.setContentView(R.layout.alert_image_options);

        win.setBackgroundDrawableResource(R.drawable.fundoazul);

        View web_btn = (View)win.findViewById(R.id.areaWeb);
        web_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadSearchForImage(TAG);
                alertDialog.dismiss();
            }
        });

        View cam_btn = (View)win.findViewById(R.id.areaCamera);
        cam_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImagePicker.with(CreateThemeActivity.this)
                        .cameraOnly()
                        .crop()
                        .compress(420)
                        .start(TAG);
                alertDialog.dismiss();
            }
        });

        View biblioteca_btn = (View)win.findViewById(R.id.areaBiblioteca);
        biblioteca_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImagePicker.with(CreateThemeActivity.this)
                        .galleryOnly()
                        .crop()
                        .compress(420)
                        .start(TAG);
                alertDialog.dismiss();
            }
        });

        View close_btn = (View)win.findViewById(R.id.areaClose);
        close_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * Load name and open fragment for search image result
     * @param TAG
     */
    public void loadSearchForImage(int TAG) {
        String nomeParaBuscar = null;

        TextInputLayout textIn = TAG==1?this.tlIdTheme :this.tlIdWord;
        if(textIn.getEditText().getText() != null && textIn.getEditText().getText().toString().length() > 0) {
            nomeParaBuscar = textIn.getEditText().getText().toString();
        }

        if(nomeParaBuscar != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment searchF = new SearchFragment(nomeParaBuscar, TAG);
            ft.replace(R.id.frameAuxPhotoFragment, searchF);
            ft.addToBackStack(null);
            ft.commit();
        } else {
            Toast.makeText(getApplicationContext(), "Preencha o campo para pesquisar imagens.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Action for update current theme info into views
     */
    public void updateThemeInfo()
    {
        if(this.theme != null) {
            tlIdTheme.getEditText().setText(this.theme.getName());
            urlImageTheme = this.theme.getImageUrl();
            if(urlImageTheme == null) {
                imageTheme.setImageResource(android.R.drawable.ic_menu_gallery);
            } else {
                updateImageTheme();
            }
        }
    }

    /**
     * Action for update current word info into views
     */
    public void updateWordInfo()
    {
        if(urlImageWord == null) {
            imageWord.setImageResource(android.R.drawable.ic_menu_gallery);
        } else {
            updateImageWord();
        }
    }

    /**
     * Action for setup the current edited word
     * @param challenge
     */
    public void editWord(Challenge challenge)
    {
        tlIdWord.requestFocus();

        editWord = challenge;
        tlIdWord.getEditText().setText(challenge.getWord());
        urlImageWord = challenge.getImageUrl();
        updateEditModeWord(true);
    }

    /**
     * Update the current mode of edition of word between edit or create
     * @param edit
     */
    public void updateEditModeWord(boolean edit)
    {
        this.editWordMode = edit;

        if(this.editWordMode) {
            Button addBt2 = findViewById(R.id.buttonAddPalavra2);
            addBt2.setVisibility(View.GONE);
            addBt2.setVisibility(View.VISIBLE);
            Button addBt = findViewById(R.id.buttonAddPalavra);
            addBt.setText("OK");
            addBt.setVisibility(View.GONE);
            addBt.setVisibility(View.VISIBLE);
        } else {
            Button addBt2 = findViewById(R.id.buttonAddPalavra2);
            addBt2.setVisibility(View.GONE);
            Button addBt = findViewById(R.id.buttonAddPalavra);
            addBt.setText("Adicionar Palavra");
            addBt.setVisibility(View.GONE);
            addBt.setVisibility(View.VISIBLE);

            tlIdWord.getEditText().setText(null);
            urlImageWord = null;
        }
        updateWordInfo();
    }

    /**
     * Action for remove an word
     * @param challenge
     */
    public void removeWord(Challenge challenge)
    {
        this.theme.getChallenges().remove(challenge);
        this.words_Add.remove(challenge);
        if(challenge.getId() != null) {
            wordsID_Rem.add(challenge.getId());
        }
        if(this.editWordMode && challenge == this.editWord) {
            updateEditModeWord(false);
        }
        Toast.makeText(getApplicationContext(), "Palavra \""+challenge.getWord()+"\" foi removida", Toast.LENGTH_LONG).show();
    }

    /**
     * Function for copy file
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void copyFile(File src, File dst) throws IOException
    {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    /**
     * Get image of an object at runtime
     * @param object
     * @return
     */
    public String getImagePathFromObject(Object object)
    {
        String path = imagePathToObjectMap.get(object);
        return path;
    }

    /**
     * Save temp image to cache dir
     * @param image
     * @param object
     */
    public void setImageToObject(File image, Object object)
    {
        if(object == null || image == null) {
            return;
        }
        String imagePathTemp = null;
        try {
            File outputDir = getApplicationContext().getCacheDir();
            File outputFile = File.createTempFile("image", ".jpg", outputDir);
            imagePathTemp = outputFile.getPath();
            copyFile(image, outputFile);
            // delete image in old app file
            image.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(imagePathTemp != null) {
            imagePathToObjectMap.put(object, imagePathTemp);
        }
    }

    /**
     * Save image to app files
     * @param pathTempImage
     * @return
     */
    public String saveImageToFiles(String pathTempImage)
    {
        String pathRet = null;
        if(pathTempImage != null) {
            if(pathTempImage.startsWith(this.imageSavePath)) {
                return pathTempImage;
            }
            try {
                File dirPath = new File(this.imageSavePath);
                if (!dirPath.exists()) {
                    dirPath.mkdirs();
                }
                File imagePath = File.createTempFile("image", ".jpg", dirPath);
                File imageFileTemp = new File(pathTempImage);
                copyFile(imageFileTemp, imagePath);
                // delete temp image cache dir
                imageFileTemp.delete();
                pathRet = imagePath.getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pathRet;
    }

    /**
     * Save button action
     * @param v
     */
    public void saveChanges(View v)
    {
        String temaNome = tlIdTheme.getEditText().getText().toString();

        if(temaNome == null || temaNome.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insira um nome para o Tema", Toast.LENGTH_LONG).show();
            return;
        }

        if(urlImageTheme == null || urlImageTheme.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insira uma Imagem para o Tema", Toast.LENGTH_LONG).show();
            return;
        }

        if(theme.getChallenges().size() == 0) {
            Toast.makeText(getApplicationContext(), "Adicione algumas palavras ao Tema", Toast.LENGTH_LONG).show();
            return;
        }

        v.setEnabled(false);

        saveActualTheme();

        Intent intent = new Intent(CreateThemeActivity.this, ThemeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Save image at runtime
     * @param object
     */
    public void saveImageToObject(Object object)
    {
        String origImagePath = null;
        String newImagePath = null;
        if(object != null) {
            if (object instanceof Theme) {
                Long id = ((Theme)object).getId();
                if(id!=null) {
                    Theme temaGet = themeSqlService.get(id);
                    origImagePath = temaGet.getImageUrl();
                }
                String ImagePath = ((Theme)object).getImageUrl();
                if(ImagePath != null && ImagePath.startsWith("/")) {
                    newImagePath = saveImageToFiles(ImagePath);
                    ((Theme)object).setImageUrl(newImagePath);
                }
            } else if (object instanceof Challenge) {
                Long id = ((Challenge)object).getId();
                if(id!=null) {
                    Challenge palavraGet = challengeSqlService.get(id);
                    origImagePath = palavraGet.getImageUrl();
                }
                String ImagePath = ((Challenge)object).getImageUrl();
                if(ImagePath != null && ImagePath.startsWith("/")) {
                    newImagePath = saveImageToFiles(ImagePath);
                    ((Challenge)object).setImageUrl(newImagePath);
                }
            }
        }
        if(origImagePath!=null && newImagePath!=null && !origImagePath.equals(newImagePath)) {
            File oldImage = new File(origImagePath);
            oldImage.delete();
        }
    }

    /**
     * Action for proceed to save the actual changes of theme in local database
     */
    public void saveActualTheme()
    {
        this.theme.setName(tlIdTheme.getEditText().getText().toString().trim());
        this.theme.setImageUrl(urlImageTheme);
        this.theme.setRemovable(true);

        if(this.editMode) {
            try {

                // update theme
                saveImageToObject(this.theme);
                this.themeSqlService.update(this.theme);

                // create words
                for(Challenge palavraNow : this.words_Add) {
                    saveImageToObject(palavraNow);
                }
                this.themeSqlService.insertThemeRelatedChallenges(this.theme.getId(), this.words_Add);

                // update words
                for(Challenge palavraNow : this.theme.getChallenges()) {
                    saveImageToObject(palavraNow);
                    if(palavraNow.getId() == null) {
                        continue;
                    }
                    this.challengeSqlService.update(palavraNow);
                }

                // remove words
                for(Long palavraIDNow : this.wordsID_Rem) {
                    this.challengeSqlService.deleteById(palavraIDNow);
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro ao atualizar Tema: "+theme.getName(), Toast.LENGTH_LONG).show();
            } finally {
                Toast.makeText(CreateThemeActivity.this, String.format("Alterações do Tema '%s' Salva com Sucesso!", this.theme.getName()), Toast.LENGTH_SHORT).show();
            }
        } else {
            try {

                // save image to app files
                saveImageToObject(this.theme);
                for(Challenge palavraNow : this.theme.getChallenges()) {
                    saveImageToObject(palavraNow);
                }

                this.themeSqlService.insert(this.theme, this.theme.getChallenges());

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro ao salvar Tema: "+theme.getName(), Toast.LENGTH_LONG).show();
            } finally {
                Toast.makeText(CreateThemeActivity.this, String.format("Tema '%s' Salvo com Sucesso!", this.theme.getName()), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Listen screen touches for auto close the keyboard when user touch or fade up/down the screen
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    /**
     * Activity result callback from user selected image in album from the picker
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            Uri dataUri = data.getData();
            File dataFile = null;
            try {
                dataFile = new File(new URI(dataUri.toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Log.w(TAG, "Image path file: "+ dataFile.toString());

            sendImage(dataFile, requestCode);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Load image icon from the current word link
     */
    public void updateImageWord()
    {
        ImageLoadUtil.getInstance().loadImage(urlImageWord, imageWord, getApplicationContext());
    }

    /**
     * Load image icon from the current theme link
     */
    public void updateImageTheme()
    {
        ImageLoadUtil.getInstance().loadImage(urlImageTheme, imageTheme, getApplicationContext());
    }

    /**
     * store the image at runtime
     * @param fileImage
     * @param requestCode
     */
    public void sendImage(File fileImage, int requestCode)
    {
        if(requestCode == 1) {
            setImageToObject(fileImage, theme);
            urlImageTheme = getImagePathFromObject(theme);
            updateImageTheme();
        } else if(requestCode == 2) {
            setImageToObject(fileImage, editWord);
            urlImageWord = getImagePathFromObject(editWord);
            updateImageWord();
        }


    }

    /**
     * Populate View with list of words
     */
    public void fillRecycleView()
    {
        recyclerView.setLayoutManager(layManager);
        recyclerView.setAdapter(new CreateWordAdapter(this, getApplicationContext(), true, true));
    }

    /**
     * Listen to detect click on any view in the activity
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.btn_back):
                finish();
                break;
        }
    }

    /**
     * Action call for Back button press
     */
    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }


}
