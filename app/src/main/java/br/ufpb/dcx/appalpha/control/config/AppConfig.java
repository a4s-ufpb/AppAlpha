package br.ufpb.dcx.appalpha.control.config;

import android.content.Context;
import android.util.Log;

import br.ufpb.dcx.appalpha.control.json.JsonManager;

import org.json.JSONException;
import org.json.JSONObject;

public class AppConfig extends JsonManager {
    private static AppConfig instance;
    public static final String CASUAL = "casual";
    public static final String CURSIVA = "cursiva";
    public static final String BASTAO = "bastão";
    public static final String UPPER = "maiúsculas";
    public static final String LOWER = "minúsculas";
    private String currentLetterType;
    private String currentLetterCase;
    private JSONObject jsonObjConfig;

    private AppConfig(Context appContext){
        this.jsonObjConfig = super.getJsonObjectOfArchive(appContext);
        try {
            this.currentLetterType = (String) this.jsonObjConfig.get("letter_type");
            this.currentLetterCase = (String) this.jsonObjConfig.get("letter_case");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static AppConfig getInstance(Context appContext){
        if(instance == null){
            instance = new AppConfig(appContext);
        }

        return instance;
    }

    @Override
    protected String getDiretory() {
        return "configs/";
    }

    @Override
    protected String getJsonFileName() {
        return "configs.json";
    }

    public String getCurrentLetterType() {
        return currentLetterType;
    }

    public void setCurrentLetterType(String currentLetterType) {
        this.currentLetterType = currentLetterType.toLowerCase();
    }

    public String getCurrentLetterCase() {
        return currentLetterCase;
    }

    public void setCurrentLetterCase(String currentLetterCase) {
        this.currentLetterCase = currentLetterCase.toLowerCase();
    }

    public void saveAllChange(Context appContext){
        JSONObject newJsonObjConfig = new JSONObject();
        try {
            newJsonObjConfig.put("letter_type", this.currentLetterType);
            newJsonObjConfig.put("letter_case", this.currentLetterCase);
            this.jsonObjConfig = newJsonObjConfig;
            super.writeJsonObject(appContext, this.jsonObjConfig);
            Log.i("Json - AppConfig", "All changes have been saved");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
