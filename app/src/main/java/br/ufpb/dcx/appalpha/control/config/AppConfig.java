package br.ufpb.dcx.appalpha.control.config;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Class to store and retrieve App Settings
 */
public class AppConfig {
    private static AppConfig instance;
    SharedPreferences sPreferences = null;

    public static final String CASUAL = "casual";
    public static final String CURSIVA = "cursiva";
    public static final String BASTAO = "bastão";
    public static final String UPPER = "maiúsculas";
    public static final String LOWER = "minúsculas";

    private String currentLetterType;
    private String currentLetterCase;

    /**
     * Alloc instance loading saved settings to variables
     *
     * @param appContext
     */
    private AppConfig(Context appContext) {
        try {
            if (sPreferences == null) {
                sPreferences = appContext.getSharedPreferences("configs", MODE_PRIVATE);
            }
            this.currentLetterType = sPreferences.getString("letter_type", "casual");
            this.currentLetterCase = sPreferences.getString("letter_case", "maiúsculas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get shared instance
     *
     * @param appContext
     * @return
     */
    public static AppConfig getInstance(Context appContext) {
        if (instance == null) {
            instance = new AppConfig(appContext);
        }
        return instance;
    }

    /**
     * Get the font type
     *
     * @return
     */
    public String getCurrentLetterType() {
        return currentLetterType;
    }

    /**
     * Set the font type
     *
     * @param currentLetterType
     */
    public void setCurrentLetterType(String currentLetterType) {
        this.currentLetterType = currentLetterType.toLowerCase();
    }

    /**
     * Get letter case
     *
     * @return
     */
    public String getCurrentLetterCase() {
        return currentLetterCase;
    }

    /**
     * Set the letter case
     *
     * @param currentLetterCase
     */
    public void setCurrentLetterCase(String currentLetterCase) {
        this.currentLetterCase = currentLetterCase.toLowerCase();
    }

    /**
     * Save current variables to settings
     */
    public void saveAllChange() {
        try {
            SharedPreferences.Editor edit = sPreferences.edit();
            edit.putString("letter_type", this.currentLetterType);
            edit.putString("letter_case", this.currentLetterCase);
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
