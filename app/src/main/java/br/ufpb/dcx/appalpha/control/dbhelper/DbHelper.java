package br.ufpb.dcx.appalpha.control.dbhelper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to manage local database
 */
public class DbHelper extends SQLiteOpenHelper {
    private final String TAG = "DbHelper";
    private static final int VERSION = 1;
    public static final String NAME = "AppAlpha.db";
    /* Table names */
    public final static String RECORDS_TABLE = "records";
    public final static String THEMES_TABLE = "themes";
    public final static String CHALLENGES_TABLE = "challenges";
    public final static String CHALLENGE_THEME_TABLE = "challenge_theme";

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    /**
     * On create database execute sql query
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String activateForeignKey = "PRAGMA foreign_keys = ON";
        String recordsSql = "CREATE TABLE IF NOT EXISTS " + RECORDS_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, points REAL)";
        String themesSql = "CREATE TABLE IF NOT EXISTS " + THEMES_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, soundUrl VARCHAR, videoUrl VARCHAR, imageUrl VARCHAR, apiId INTEGER UNIQUE, deletavel INTEGER DEFAULT 0)";
        String challengesSql = "CREATE TABLE IF NOT EXISTS " + CHALLENGES_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT, word VARCHAR, soundUrl VARCHAR, videoUrl VARCHAR, imageUrl VARCHAR)";
        String challenge_themeSql = "CREATE TABLE IF NOT EXISTS " + CHALLENGE_THEME_TABLE + "(challenge_id INTEGER, theme_id INTEGER)";

        List<String> sqls = new ArrayList<>(Arrays.asList(activateForeignKey, recordsSql, themesSql, challengesSql, challenge_themeSql));

        for(String s: sqls){
            try {
                db.execSQL(s);
                Log.i(TAG, s);
            } catch(SQLException e) {
                Log.i(TAG, e.getMessage());
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
