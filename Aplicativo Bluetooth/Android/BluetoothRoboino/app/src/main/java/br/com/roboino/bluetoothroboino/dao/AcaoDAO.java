package br.com.roboino.bluetoothroboino.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.roboino.bluetoothroboino.modelo.Acao;

/**
 * Created by Diego on 01/03/2017.
 */

public class AcaoDAO extends SQLiteOpenHelper{

    public AcaoDAO(Context context) {
        super(context, "RoboinoBT", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Acoes (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, dado TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Acoes";
        db.execSQL(sql);
        onCreate(db);
    }

    private ContentValues PegaDadosAcao(Acao acao) {
        ContentValues dados = new ContentValues();
        dados.put("nome", acao.getNome());
        dados.put("dado", acao.getDado());
        return dados;
    }

    public void altera(Acao acao) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = PegaDadosAcao(acao);
        String parametros[] = {String.valueOf(acao.getId())};
        db.update("Acoes", dados, "id=?", parametros);
    }

    public void insere(Acao acao) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = PegaDadosAcao(acao);
        db.insert("Acoes", null, dados);
    }

    public List<Acao> buscaAcoes() {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM Acoes;";
        Cursor c = db.rawQuery(sql, null);

        List<Acao> acoes = new ArrayList<Acao>();

        while(c.moveToNext()){
            Acao acao = new Acao();
            acao.setId(c.getLong(c.getColumnIndex("id")));
            acao.setNome(c.getString(c.getColumnIndex("nome")));
            acao.setDado(c.getString(c.getColumnIndex("dado")));

            acoes.add(acao);
        }

        c.close();

        return acoes;
    }

    public void deleta(Acao acao) {
        SQLiteDatabase db = getWritableDatabase();
        String[] parametros = {String.valueOf(acao.getId())};
        db.delete("Acoes", "id = ?", parametros);
    }
}
