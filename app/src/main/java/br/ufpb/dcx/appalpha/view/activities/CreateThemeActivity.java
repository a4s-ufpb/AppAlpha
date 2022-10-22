package br.ufpb.dcx.appalpha.view.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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


public class CreateThemeActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "CreateThemeActivity";
    private ImageButton back_btn;

    private ThemeSqlService themeSqlService;
    private ChallengeSqlService challengeSqlService;

    private RecyclerView recyclerView;
    private GridLayoutManager layManager;

    public Theme tema;

    private TextInputLayout tlIdTema;
    private String urlImageTema;
    private ImageView imagemTema;

    private boolean editMode = false;
    private Long editThemeID;

    private TextInputLayout tlIdPalavra;
    private String urlImagePalavra;
    private ImageView imagemPalavra;

    public boolean editPalavraMode;
    public Challenge editPalavra;
    public List<Long> palavrasID_Remover;
    public List<Challenge> palavras_Adicionar;

    public HashMap<Object, String> imagePathToObjectMap;
    public String imageSavePath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.palavrasID_Remover = new ArrayList<Long>();
        this.palavras_Adicionar = new ArrayList<Challenge>();
        this.imagePathToObjectMap = new HashMap<Object, String>();

        this.imageSavePath = getFilesDir().getAbsolutePath() + File.separator + "images";

        this.tema = new Theme(null, null, null, null);
        this.editPalavra = new Challenge(null, null,null,null,null);

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

        tlIdPalavra = findViewById(R.id.tlIdPalavra);
        imagemPalavra = findViewById(R.id.imageView7);

        tlIdTema = findViewById(R.id.tlIdTheme2);
        imagemTema = findViewById(R.id.imageView72);

        recyclerView = findViewById(R.id.palavrasRows);
        layManager = new GridLayoutManager(getApplicationContext(), 2);

        imagemTema.setOnClickListener(v -> {
            ImagePicker.with(CreateThemeActivity.this)
                        .crop()
                        .compress(420)
                        .start(1);
        });

        imagemPalavra.setOnClickListener(v -> {
            ImagePicker.with(CreateThemeActivity.this)
                    .crop()
                    .compress(420)
                    .start(2);
        });

        findViewById(R.id.buttonAddPalavra).setOnClickListener(v -> {
            String palavra = tlIdPalavra.getEditText().getText().toString();

            if(palavra == null || palavra.length() == 0) {
                Toast.makeText(getApplicationContext(), "Insira uma Palavra", Toast.LENGTH_LONG).show();
                return;
            }
            if(urlImagePalavra == null || urlImagePalavra.length() == 0) {
                Toast.makeText(getApplicationContext(), "Insira uma Imagem para a palavra", Toast.LENGTH_LONG).show();
                return;
            }

            if(this.editPalavraMode) {
                editPalavra.setImageUrl(urlImagePalavra);
                editPalavra.setWord(palavra.trim());
                updateEditModePalavra(false);
                recyclerView.getAdapter().notifyItemChanged(tema.getChallenges().indexOf(editPalavra));
                Toast.makeText(getApplicationContext(), "Palavra \""+editPalavra.getWord()+"\" foi atualizada.", Toast.LENGTH_LONG).show();
            } else {
                Challenge palavaNova = new Challenge(palavra.trim(), null, null, urlImagePalavra);
                tema.getChallenges().add(palavaNova);
                this.palavras_Adicionar.add(palavaNova);
                recyclerView.getAdapter().notifyItemInserted(tema.getChallenges().indexOf(palavaNova));
                Toast.makeText(getApplicationContext(), "Palavra \""+palavaNova.getWord()+"\" foi adicionada.", Toast.LENGTH_LONG).show();
            }
            tlIdPalavra.getEditText().setText(null);
            urlImagePalavra = null;
            updatePalavraInfo();
        });

        findViewById(R.id.buttonAddPalavra2).setOnClickListener(v -> {
            updateEditModePalavra(false);
        });

        if(this.editMode) {
            // Titulo para modo de ediçao
            AlphaTextView title = findViewById(R.id.textView19);
            title.setText("Editar Tema");

            // recuperar Tema da base de dados
            this.tema = themeSqlService.get(editThemeID);
            List<Challenge> storedChallenges = challengeSqlService.getRelatedChallenges(this.tema.getId());
            if(storedChallenges != null) {
                this.tema.setChallenges(storedChallenges);
            }

            Log.i(TAG, "Edit tema: "+ this.tema.toString());
        }

        updateTemaInfo();

        fillRecycleView();
    }

    public void updateTemaInfo()
    {
        if(this.tema != null) {
            tlIdTema.getEditText().setText(this.tema.getName());
            urlImageTema = this.tema.getImageUrl();
            if(urlImageTema == null) {
                imagemTema.setImageResource(android.R.drawable.ic_menu_gallery);
            } else {
                updateImagemTema();
            }
        }
    }

    public void updatePalavraInfo()
    {
        if(urlImagePalavra == null) {
            imagemPalavra.setImageResource(android.R.drawable.ic_menu_gallery);
        } else {
            updateImagemPalavra();
        }
    }

    public void editPalavra(Challenge challenge)
    {
        tlIdPalavra.requestFocus();

        editPalavra = challenge;
        tlIdPalavra.getEditText().setText(challenge.getWord());
        urlImagePalavra = challenge.getImageUrl();
        updateEditModePalavra(true);
    }

    public void updateEditModePalavra(boolean edit)
    {
        this.editPalavraMode = edit;

        if(this.editPalavraMode) {
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

            tlIdPalavra.getEditText().setText(null);
            urlImagePalavra = null;
        }
        updatePalavraInfo();
    }

    public void removePalavra(Challenge challenge)
    {
        this.tema.getChallenges().remove(challenge);
        this.palavras_Adicionar.remove(challenge);
        if(challenge.getId() != null) {
            palavrasID_Remover.add(challenge.getId());
        }
        if(this.editPalavraMode && challenge == this.editPalavra) {
            updateEditModePalavra(false);
        }
        Toast.makeText(getApplicationContext(), "Palavra \""+challenge.getWord()+"\" foi removida", Toast.LENGTH_LONG).show();
    }


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

    public String getImagePathFromObject(Object object)
    {
        String path = imagePathToObjectMap.get(object);
        return path;
    }

    // save temp image to cache dir
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

    // save image to app files
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

    public void saveChanges(View v)
    {
        String temaNome = tlIdTema.getEditText().getText().toString();

        if(temaNome == null || temaNome.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insira um nome para o Tema", Toast.LENGTH_LONG).show();
            return;
        }

        if(urlImageTema == null || urlImageTema.length() == 0) {
            Toast.makeText(getApplicationContext(), "Insira uma Imagem para o Tema", Toast.LENGTH_LONG).show();
            return;
        }

        if(tema.getChallenges().size() == 0) {
            Toast.makeText(getApplicationContext(), "Adicione algumas palavras ao Tema", Toast.LENGTH_LONG).show();
            return;
        }

        v.setEnabled(false);

        salvarTemaAtual();

        Intent intent = new Intent(CreateThemeActivity.this, ThemeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

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

    public void salvarTemaAtual()
    {
        this.tema.setName(tlIdTema.getEditText().getText().toString().trim());
        this.tema.setImageUrl(urlImageTema);
        this.tema.setDeletavel(true);

        if(this.editMode) {

            // atualizar theme
            saveImageToObject(this.tema);
            this.themeSqlService.update(this.tema);

            // adicionar palavras
            for(Challenge palavraNow : this.palavras_Adicionar) {
                saveImageToObject(palavraNow);
            }
            this.themeSqlService.insertThemeRelatedChallenges(this.tema.getId(), this.palavras_Adicionar);

            //atualizar palavras
            for(Challenge palavraNow : this.tema.getChallenges()) {
                saveImageToObject(palavraNow);
                if(palavraNow.getId() == null) {
                    continue;
                }
                this.challengeSqlService.update(palavraNow);
            }

            //remover palavras
            for(Long palavraIDNow : this.palavrasID_Remover) {
                this.challengeSqlService.deleteById(palavraIDNow);
            }

            Log.i(TAG, "Alterações do Tema Salva com sucesso!");
            Toast.makeText(CreateThemeActivity.this, String.format("Alterações do Tema '%s' Salva com Sucesso!", this.tema.getName()), Toast.LENGTH_SHORT).show();
        } else {
            // save image to app files
            saveImageToObject(this.tema);
            for(Challenge palavraNow : this.tema.getChallenges()) {
                saveImageToObject(palavraNow);
            }
            this.themeSqlService.insert(this.tema, this.tema.getChallenges());
            Toast.makeText(CreateThemeActivity.this, String.format("Tema '%s' Salvo com Sucesso!", this.tema.getName()), Toast.LENGTH_SHORT).show();
        }
    }

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

    public void updateImagemPalavra()
    {
        ImageLoadUtil.getInstance().loadImage(urlImagePalavra, imagemPalavra, getApplicationContext());
    }

    public void updateImagemTema()
    {
        ImageLoadUtil.getInstance().loadImage(urlImageTema, imagemTema, getApplicationContext());
    }

    public void sendImage(File fileImage, int requestCode)
    {
        if(requestCode == 1) {
            setImageToObject(fileImage, tema);
            urlImageTema = getImagePathFromObject(tema);
            updateImagemTema();
        } else if(requestCode == 2) {
            setImageToObject(fileImage, editPalavra);
            urlImagePalavra = getImagePathFromObject(editPalavra);
            updateImagemPalavra();
        }
    }

    public void fillRecycleView()
    {
        recyclerView.setLayoutManager(layManager);
        recyclerView.setAdapter(new CreatePalavraAdapter(this, getApplicationContext(), true, true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.btn_back):
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
