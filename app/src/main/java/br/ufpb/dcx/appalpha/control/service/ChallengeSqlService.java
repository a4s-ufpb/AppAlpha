package br.ufpb.dcx.appalpha.control.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.ufpb.dcx.appalpha.control.dbhelper.DbHelper;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;

public class ChallengeSqlService {
    private final String TAG = "ChallengeApiService";
    private static ChallengeSqlService instance;
    private DbHelper db;
    private SQLiteDatabase writableDb;
    private SQLiteDatabase readableDb;

    public static ChallengeSqlService getInstance(Context context) {
        if (instance == null) {
            instance = new ChallengeSqlService(context);
        }
        return instance;
    }

    private ChallengeSqlService(Context context) {
        this.db = new DbHelper(context);
        this.writableDb = db.getWritableDatabase();
        this.readableDb = db.getReadableDatabase();
    }

    public Long insert(Challenge challenge) {
        ContentValues cv = new ContentValues();
        Long id = -1L;
        try {
            cv.put("word", challenge.getWord());
            cv.put("soundUrl", challenge.getSoundUrl());
            cv.put("videoUrl", challenge.getVideoUrl());
            cv.put("imageUrl", challenge.getImageUrl());

            id = this.writableDb.insert(DbHelper.CHALLENGES_TABLE, null, cv);
            Log.i(TAG, challenge.getWord() + " added in db!");

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            cv.clear();
        }

        return id;
    }

    public Challenge get(Long id)
    {
        String word, soundUrl, videoUrl, imageUrl;
        word = soundUrl = videoUrl = imageUrl = "";

        String selectQuery = "SELECT word, soundUrl, videoUrl, imageUrl FROM " + DbHelper.CHALLENGES_TABLE + " WHERE id = ?";
        Cursor cursor = readableDb.rawQuery(selectQuery, new String[]{Long.toString(id)});
        if (cursor.moveToFirst()) {
            word = cursor.getString(0);
            soundUrl = cursor.getString(1);
            videoUrl = cursor.getString(2);
            imageUrl = cursor.getString(3);
        }
        cursor.close();
        Challenge c = new Challenge(id, word, soundUrl, videoUrl, imageUrl);
        return c;
    }

    public List<Challenge> getRelatedChallenges(Long theme_id) {
        String word, soundUrl, videoUrl, imageUrl;
        Long id;
        word = soundUrl = videoUrl = imageUrl = "";

        Log.i(TAG, "" + theme_id);

        List<Challenge> challenges = new ArrayList<>();

        String selectQuery = "SELECT " + DbHelper.CHALLENGES_TABLE + ".id, " + DbHelper.CHALLENGES_TABLE + ".word, " + DbHelper.CHALLENGES_TABLE + ".soundUrl, " + DbHelper.CHALLENGES_TABLE + ".videoUrl, " + DbHelper.CHALLENGES_TABLE + ".imageUrl FROM "
                + DbHelper.CHALLENGES_TABLE +
                " INNER JOIN " +
                DbHelper.CHALLENGE_THEME_TABLE + " CT ON "
                + DbHelper.CHALLENGES_TABLE + ".id = CT.challenge_id" +
                " AND CT.theme_id = " + theme_id;

        Cursor cursor = readableDb.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getLong(0);
                word = cursor.getString(1);
                soundUrl = cursor.getString(2);
                videoUrl = cursor.getString(3);
                imageUrl = cursor.getString(4);

                Challenge c = new Challenge(id, word, soundUrl, videoUrl, imageUrl);
                challenges.add(c);
            } while (cursor.moveToNext());
        }

        cursor.close();
        Log.i(TAG, "TAMANHO " + challenges.size());
        return challenges;
    }

    public void deleteById(Long id)
    {
        // delete saved local image file
        Challenge palavra = get(id);
        if(palavra!=null && palavra.getImageUrl()!=null) {
            String imageSt = palavra.getImageUrl();
            if(imageSt.startsWith("/")) {
                File imageFile = new File(imageSt);
                imageFile.delete();
            }
        }

        String deleteQuery = "DELETE FROM " + DbHelper.CHALLENGES_TABLE + " WHERE id = ?";
        Cursor cursor = writableDb.rawQuery(deleteQuery, new String[]{Long.toString(id)});
        if (cursor.moveToFirst()) {
            Integer value = cursor.getInt(0);
            Log.i(TAG, value + "");
            cursor.close();
        }

        // delete chalange from relation
        deleteQuery = "DELETE FROM " + DbHelper.CHALLENGE_THEME_TABLE + " WHERE challenge_id = ?";
        cursor = writableDb.rawQuery(deleteQuery, new String[]{Long.toString(id)});
        if (cursor.moveToFirst()) {
            Integer value = cursor.getInt(0);
            Log.i(TAG, value + "");
            cursor.close();
        }
    }

    public void update(Challenge challenge)
    {
        ContentValues cv = new ContentValues();
        try {
            cv.put("word", challenge.getWord());
            cv.put("soundUrl", challenge.getSoundUrl());
            cv.put("videoUrl", challenge.getVideoUrl());
            cv.put("imageUrl", challenge.getImageUrl());

            this.writableDb.update(DbHelper.CHALLENGES_TABLE, cv, "id="+challenge.getId(), null);
            Log.i(TAG, challenge.getWord() + " updated in db!");

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            cv.clear();
        }
    }
}
