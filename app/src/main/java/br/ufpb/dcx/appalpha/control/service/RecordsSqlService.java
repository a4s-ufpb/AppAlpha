package br.ufpb.dcx.appalpha.control.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import br.ufpb.dcx.appalpha.control.dbhelper.DbHelper;
import br.ufpb.dcx.appalpha.model.bean.Record;
import br.ufpb.dcx.appalpha.model.dao.RecordesDao;

/**
 * Class to manage Scores points to local database
 */
public class RecordsSqlService implements RecordesDao {
    private static RecordsSqlService instance;
    private SQLiteDatabase escreve;
    private SQLiteDatabase ler;

    /**
     * Initialize the shared instance
     * @param context
     * @return
     */
    public static RecordsSqlService getInstance(Context context){
        if(instance == null){
            instance = new RecordsSqlService(context);
        }

        return instance;
    }

    /**
     * Alloc instance and setup local database variables
     * @param context
     */
    private RecordsSqlService(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        ler = db.getReadableDatabase();
    }

    /**
     * Save an new Record score to local database
     * @param points
     * @param name
     */
    @Override
    public void cadastrarNovoRecorde(double points, String name) {
        ContentValues cv = new ContentValues();

        try {
            cv.put("name", name);
            cv.put("points", points);

            escreve.insert(DbHelper.RECORDS_TABLE, null, cv);
            Log.i("insert info", "os dados entraram");
        } catch(Exception e) {
            Log.i("insert info", "deu merda hein "+ e.getMessage());
        }
    }

    /**
     * Get list of all Record stored in local database
     * @return
     */
    @Override
    public ArrayList<Record> getRecordistas() {
        ArrayList<Record> records = new ArrayList<>();
        String name;
        double points;
        Record r1;

        Cursor cursor = ler.rawQuery("SELECT name, points FROM " + DbHelper.RECORDS_TABLE, null);

         // Pegando indices da tabela
        int indiceNome = cursor.getColumnIndex("name");
        int indicePontuacao = cursor.getColumnIndex("points");


        if(cursor.moveToFirst() && cursor.getCount() >= 1) {
            do {
                name = cursor.getString(indiceNome);
                points = cursor.getDouble(indicePontuacao);

                r1 = new Record(name, points);
                records.add(r1);
            } while(cursor.moveToNext());
        }

        Collections.sort(records);

        cursor.close();

        return records;
    }
}
