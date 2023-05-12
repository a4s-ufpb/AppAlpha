package br.ufpb.dcx.appalpha.model.dao;

import br.ufpb.dcx.appalpha.model.bean.Record;

import java.util.ArrayList;

/**
 * Interface with default methods of Record
 */
public interface RecordsDao {

    void createNewRecord(double pontuacao, String nome);

    ArrayList<Record> getAllRecords();
}
