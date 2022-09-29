package br.ufpb.dcx.appalpha.control.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.ufpb.dcx.appalpha.control.dbhelper.DbHelper;
import br.ufpb.dcx.appalpha.model.bean.Challenge;

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
}
