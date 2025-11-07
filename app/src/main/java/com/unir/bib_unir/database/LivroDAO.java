package com.unir.bib_unir.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LivroDAO implements ILivroDAO{

    private SQLiteDatabase leitura;
    private SQLiteDatabase escrita;
    private DbHelper dbHelper;

    public LivroDAO(Context context){
        dbHelper = new DbHelper(context);
        this.leitura = dbHelper.getReadableDatabase();
        this.escrita = dbHelper.getWritableDatabase();
    }

    @SuppressLint("Range")
    private List<ContentValues> pesquisar(String sql, String where[]){
        List<ContentValues> lista = new ArrayList<ContentValues>();
        Cursor cursor = this.leitura.rawQuery(sql,where);

        if (cursor.moveToFirst()){
            do{
                ContentValues cv = new ContentValues();
                // Recupera os índices das colunas e depois seus valores
                cv.put("id", cursor.getInt(cursor.getColumnIndex("id")));
                cv.put("titulo", cursor.getString(cursor.getColumnIndex("titulo")));
                cv.put("autor", cursor.getString(cursor.getColumnIndex("autor")));
                cv.put("ano", cursor.getInt(cursor.getColumnIndex("ano")));
                lista.add(cv);
            }while (cursor.moveToNext());
        }
        cursor.close(); // É importante fechar o cursor após o uso
        return lista;
    }

    @SuppressLint("Range")
    @Override
    public ContentValues pesquisarPorId(int id) {
        ContentValues cv = new ContentValues();
        String sql = "SELECT * FROM livro WHERE id = ?";
        String where [] = new String[]{String.valueOf(id)};
        Cursor cursor = this.leitura.rawQuery(sql,where);
        if(cursor.moveToFirst()){
            cv.put("id", cursor.getInt(cursor.getColumnIndex("id")));
            cv.put("titulo", cursor.getString(cursor.getColumnIndex("titulo")));
            cv.put("autor", cursor.getString(cursor.getColumnIndex("autor")));
            cv.put("ano", cursor.getInt(cursor.getColumnIndex("ano")));
        }
        cursor.close();
        return cv;
    }

    @Override
    public List<ContentValues> pesquisarPorTitulo(String titulo) {
        String sql = "SELECT * FROM livro WHERE titulo LIKE ?";
        // O uso de % permite pesquisar por partes do título
        String where [] = new String[]{"%"+titulo+"%"};
        return pesquisar(sql, where);
    }

    @Override
    public List<ContentValues> pesquisarPorAno(int ano) {
        String sql = "SELECT * FROM livro WHERE ano = ?";
        String where [] = new String[]{String.valueOf(ano)};
        return pesquisar(sql, where);
    }

    @Override
    public List<ContentValues> pesquisarPorTodos() {
        String sql = "SELECT * FROM livro ORDER BY id";
        String where [] = null;
        return pesquisar(sql, where);
    }

    @Override
    public long inserir(ContentValues cv) {
        // Retorna o ID do novo registro ou -1 em caso de erro
        long id = this.escrita.insert("livro", null, cv);
        return id;
    }

    @Override
    public void alterarRegistro(int id, String titulo, String autor, int ano) {
        ContentValues valores = new ContentValues();
        String where = "id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        valores.put("titulo", titulo);
        valores.put("autor", autor);
        valores.put("ano", ano);
        this.escrita.update("livro", valores, where, whereArgs);
        // Nota: Evite fechar o banco (this.escrita.close()) se for usar o DAO novamente na mesma tela.
    }

    @Override
    public void deletarRegistro(int id) {
        String where = "id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        this.escrita.delete("livro", where, whereArgs);
    }
}
