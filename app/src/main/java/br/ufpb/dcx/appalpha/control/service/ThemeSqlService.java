package br.ufpb.dcx.appalpha.control.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

import br.ufpb.dcx.appalpha.control.dbhelper.DbHelper;
import br.ufpb.dcx.appalpha.control.service.interfaces.ChallengeApiService;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;

public class ThemeSqlService {
    private final String TAG = "ThemeSqlService";
    private static ThemeSqlService instance;
    private DbHelper db;
    private SQLiteDatabase writableDb;
    private SQLiteDatabase readableDb;
    private ChallengeSqlService challengeSqlService;

    public static ThemeSqlService getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeSqlService(context);
        }
        return instance;
    }

    private ThemeSqlService(Context context) {
        this.db = new DbHelper(context);
        this.writableDb = db.getWritableDatabase();
        this.readableDb = db.getReadableDatabase();
        this.challengeSqlService = ChallengeSqlService.getInstance(context);
    }

    public Long insert(Theme theme, @Nullable List<Challenge> relatedChallenges) {
        ContentValues cv = new ContentValues();
        Long id = -1l;
        try {
            cv.put("name", theme.getName());
            cv.put("soundUrl", theme.getSoundUrl());
            cv.put("videoUrl", theme.getVideoUrl());
            cv.put("imageUrl", theme.getImageUrl());
            cv.put("apiId", theme.getApiId());
            cv.put("deletavel", theme.getDeletavel());

            id = this.writableDb.insert(DbHelper.THEMES_TABLE, null, cv);
            Log.i(TAG, theme.getName() + " added in db!");

            if (relatedChallenges != null && !relatedChallenges.isEmpty()) {
                insertThemeRelatedChallenges(id, relatedChallenges);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            cv.clear();
        }

        return id;
    }

    public void insertThemeRelatedChallenges(Long theme_id, List<Challenge> relatedChallenges) {
        ContentValues cv = new ContentValues();

        for (Challenge c : relatedChallenges) {
            try {
                cv.put("challenge_id", challengeSqlService.insert(c));
                cv.put("theme_id", theme_id);
                this.writableDb.insert(DbHelper.CHALLENGE_THEME_TABLE, null, cv);

                Log.i(TAG, c.getWord() + " added relationship by theme id " + theme_id + ".");

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            cv.clear();
        }
    }

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
        theme.setDeletavel(deletavel==1);

        return theme;
    }

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
                t.setDeletavel(deletavel==1);
                themes.add(t);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return themes;
    }

    public boolean existsByApiId(long id) {
        String selectQuery = "SELECT exists(SELECT id FROM " + DbHelper.THEMES_TABLE + " WHERE apiId = ? LIMIT 1)";

        Cursor cursor = readableDb.rawQuery(selectQuery, new String[]{Long.toString(id)});

        if (cursor.moveToFirst()) {
            Integer value = cursor.getInt(0);
            cursor.close();
            return value == 1 ? true : false;
        } else {
            return false;
        }
    }

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

    public void update(Theme theme)
    {
        ContentValues cv = new ContentValues();
        try {
            cv.put("name", theme.getName());
            cv.put("soundUrl", theme.getSoundUrl());
            cv.put("videoUrl", theme.getVideoUrl());
            cv.put("imageUrl", theme.getImageUrl());
            cv.put("apiId", theme.getApiId());
            cv.put("deletavel", theme.getDeletavel());

            this.writableDb.update(DbHelper.THEMES_TABLE, cv, "id="+theme.getId(), null);
            Log.i(TAG, theme.getName() + " updated in db!");

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            cv.clear();
        }
    }
}
