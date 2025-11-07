package com.unir.bib_unir.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//Classe responsável pela criação e gerenciamento do banco de dados
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bibunir";
    private static final int DATABASE_VERSION = 1;
    private final String CREATE_TABLE_LIVRO = "CREATE TABLE livro (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "titulo TEXT," +
            "autor TEXT ," +
            "ano INTEGER);";


    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Executa o SQL de criação quando o banco não existe
        sqLiteDatabase.execSQL(CREATE_TABLE_LIVRO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Estratégia simples de atualização: destrói e recria
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS livro");
        onCreate(sqLiteDatabase);
    }
}
