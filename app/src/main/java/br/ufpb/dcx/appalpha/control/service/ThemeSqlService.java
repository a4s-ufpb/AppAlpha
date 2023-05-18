package br.ufpb.dcx.appalpha.control.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.ufpb.dcx.appalpha.control.dbhelper.DbHelper;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;

/**
 * Classe for CRUD of Theme in the local database.
 */
public class ThemeSqlService {
    private final String TAG = "ThemeSqlService";
    private static ThemeSqlService instance;
    private DbHelper db;
    private SQLiteDatabase writableDb;
    private SQLiteDatabase readableDb;
    private ChallengeSqlService challengeSqlService;

    /**
     * Initialize the shared instance
     * @param context
     * @return
     */
    public static ThemeSqlService getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeSqlService(context);
        }
        return instance;
    }

    /**
     * Alloc instance and setup local database variables
     * @param context
     */
    private ThemeSqlService(Context context) {
        this.db = new DbHelper(context);
        this.writableDb = db.getWritableDatabase();
        this.readableDb = db.getReadableDatabase();
        this.challengeSqlService = ChallengeSqlService.getInstance(context);
    }

    /**
     * Insert/Save an new Theme Object with List of related Challenge to the local database
     * @param theme
     * @param relatedChallenges
     * @return
     */
    public Long insert(Theme theme, @Nullable List<Challenge> relatedChallenges) throws Exception {
        Long id = -1l;

        ContentValues cv = new ContentValues();
        cv.put("name", theme.getName());
        cv.put("soundUrl", theme.getSoundUrl());
        cv.put("videoUrl", theme.getVideoUrl());
        cv.put("imageUrl", theme.getImageUrl());
        cv.put("apiId", theme.getApiId());
        cv.put("deletavel", theme.getRemovable());

        id = this.writableDb.insert(DbHelper.THEMES_TABLE, null, cv);
        if (relatedChallenges != null && !relatedChallenges.isEmpty()) {
            insertThemeRelatedChallenges(id, relatedChallenges);
        }
        cv.clear();

        return id;
    }

    /**
     * Add an relation between Theme and an list of Challenge
     * @param theme_id
     * @param relatedChallenges
     */
    public void insertThemeRelatedChallenges(Long theme_id, List<Challenge> relatedChallenges) throws Exception {
        ContentValues cv = new ContentValues();
        for (Challenge c : relatedChallenges) {
            cv.put("challenge_id", challengeSqlService.insert(c));
            cv.put("theme_id", theme_id);
            this.writableDb.insert(DbHelper.CHALLENGE_THEME_TABLE, null, cv);
            cv.clear();
        }
    }

    /**
     * Get an stored Theme Object by Id from the local database
     * @param id
     * @return
     */
    public Theme get(Long id) {
        String name, soundUrl, videoUrl, imageUrl, apiId;
        name = soundUrl = videoUrl = imageUrl = apiId = "";
        int deletavel = 0;
        String selectQuery = "SELECT name, soundUrl, videoUrl, imageUrl, apiId, deletavel FROM " + DbHelper.THEMES_TABLE + " WHERE id = ?";

        Cursor cursor = readableDb.rawQuery(selectQuery, new String[]{Long.toString(id)});

        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
            soundUrl = cursor.getString(1);
            videoUrl = cursor.getString(2);
            imageUrl = cursor.getString(3);
            apiId = cursor.getString(4);
            deletavel = cursor.getInt(5);
        }
        cursor.close();

        Theme theme = new Theme(name, imageUrl, soundUrl, videoUrl);
        theme.setId(id);
        theme.setApiId(apiId != null ? Long.parseLong(apiId) : null);
        theme.setRemovable(deletavel==1);

        return theme;
    }

    /**
     * Get list of stored Theme's from local database
     * @return
     */
    public List<Theme> getAll() {
        String name, soundUrl, videoUrl, imageUrl;
        Long id;
        String apiId;
        int deletavel = 0;
        List<Theme> themes = new ArrayList<>();

        String selectQuery = "SELECT id, name, soundUrl, videoUrl, imageUrl, apiId, deletavel FROM " + DbHelper.THEMES_TABLE;

        Cursor cursor = readableDb.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getLong(0);
                name = cursor.getString(1);
                soundUrl = cursor.getString(2);
                videoUrl = cursor.getString(3);
                imageUrl = cursor.getString(4);
                apiId = cursor.getString(5);
                deletavel = cursor.getInt(6);
                List<Challenge> relatedChallenges = challengeSqlService.getRelatedChallenges(id);
                Theme t = new Theme(id, name, imageUrl, soundUrl, videoUrl, relatedChallenges,
                        apiId != null ? Long.parseLong(apiId) : null);
                t.setRemovable(deletavel==1);
                themes.add(t);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return themes;
    }

    /**
     * Check if an Theme with an apiId exist
     * @param id
     * @return
     */
    public boolean existsByApiId(long id) {
        String selectQuery = "SELECT exists(SELECT id FROM " + DbHelper.THEMES_TABLE + " WHERE apiId = ? LIMIT 1)";

        Cursor cursor = readableDb.rawQuery(selectQuery, new String[]{Long.toString(id)});

        if (cursor.moveToFirst()) {
            int value = cursor.getInt(0);
            cursor.close();
            return value == 1;
        } else {
            return false;
        }
    }

    /**
     * Delete an stored Theme by Id in the local database
     * @param id
     */
    public void deleteById(Long id)
    {
        // delete saved local image file
        Theme tema = get(id);
        if(tema!=null && tema.getImageUrl()!=null) {
            String imageSt = tema.getImageUrl();
            if(imageSt.startsWith("/")) {
                File imageFile = new File(imageSt);
                imageFile.delete();
            }
        }

        // delete all chalenges of theme
        for(Challenge palavras : challengeSqlService.getRelatedChallenges(id)) {
            challengeSqlService.deleteById(palavras.getId());
        }

        String deleteQuery = "DELETE FROM " + DbHelper.THEMES_TABLE + " WHERE id = ?";
        Cursor cursor = writableDb.rawQuery(deleteQuery, new String[]{Long.toString(id)});
        if (cursor.moveToFirst()) {
            Integer value = cursor.getInt(0);
            Log.i(TAG, value + "");
            cursor.close();
        }

        // delete all from theme relation
        deleteQuery = "DELETE FROM " + DbHelper.CHALLENGE_THEME_TABLE + " WHERE theme_id = ?";
        cursor = writableDb.rawQuery(deleteQuery, new String[]{Long.toString(id)});
        if (cursor.moveToFirst()) {
            Integer value = cursor.getInt(0);
            Log.i(TAG, value + "");
            cursor.close();
        }
    }

    /**
     * Update the saved Theme that we got previously from local database
     * @param theme
     */
    public void update(Theme theme) throws Exception {
        ContentValues cv = new ContentValues();
        cv.put("name", theme.getName());
        cv.put("soundUrl", theme.getSoundUrl());
        cv.put("videoUrl", theme.getVideoUrl());
        cv.put("imageUrl", theme.getImageUrl());
        cv.put("apiId", theme.getApiId());
        cv.put("deletavel", theme.getRemovable());
        this.writableDb.update(DbHelper.THEMES_TABLE, cv, "id="+theme.getId(), null);
        cv.clear();
    }
}
