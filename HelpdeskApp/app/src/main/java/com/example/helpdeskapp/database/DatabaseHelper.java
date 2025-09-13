package com.example.helpdeskapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "helpdesk.db";
    private static final int DATABASE_VERSION = 2; // Incrementar versão para recriar tabelas

    // Tabela Usuários
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_SENHA = "senha";
    public static final String COLUMN_USER_NOME = "nome";
    public static final String COLUMN_USER_TIPO = "tipo";

    // Tabela Chamados
    public static final String TABLE_CHAMADOS = "chamados";
    public static final String COLUMN_CHAMADO_ID = "id";
    public static final String COLUMN_CHAMADO_NUMERO = "numero";
    public static final String COLUMN_CHAMADO_TITULO = "titulo";
    public static final String COLUMN_CHAMADO_DESCRICAO = "descricao";
    public static final String COLUMN_CHAMADO_STATUS = "status";
    public static final String COLUMN_CHAMADO_PRIORIDADE = "prioridade";
    public static final String COLUMN_CHAMADO_CLIENTE_ID = "cliente_id";
    public static final String COLUMN_CHAMADO_CREATED_AT = "created_at";
    public static final String COLUMN_CHAMADO_UPDATED_AT = "updated_at";

    // SQL Create Tables
    private static final String CREATE_TABLE_USUARIOS =
            "CREATE TABLE " + TABLE_USUARIOS + "(" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL," +
                    COLUMN_USER_SENHA + " TEXT NOT NULL," +
                    COLUMN_USER_NOME + " TEXT NOT NULL," +
                    COLUMN_USER_TIPO + " INTEGER DEFAULT 1" +
                    ")";

    private static final String CREATE_TABLE_CHAMADOS =
            "CREATE TABLE " + TABLE_CHAMADOS + "(" +
                    COLUMN_CHAMADO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_CHAMADO_NUMERO + " TEXT UNIQUE NOT NULL," +
                    COLUMN_CHAMADO_TITULO + " TEXT NOT NULL," +
                    COLUMN_CHAMADO_DESCRICAO + " TEXT NOT NULL," +
                    COLUMN_CHAMADO_STATUS + " INTEGER DEFAULT 1," +
                    COLUMN_CHAMADO_PRIORIDADE + " INTEGER DEFAULT 2," +
                    COLUMN_CHAMADO_CLIENTE_ID + " INTEGER NOT NULL," +
                    COLUMN_CHAMADO_CREATED_AT + " TEXT DEFAULT ''," +
                    COLUMN_CHAMADO_UPDATED_AT + " TEXT DEFAULT ''" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DatabaseHelper inicializado");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(TAG, "Criando tabelas do banco de dados");

            Log.d(TAG, "Criando tabela usuarios: " + CREATE_TABLE_USUARIOS);
            db.execSQL(CREATE_TABLE_USUARIOS);

            Log.d(TAG, "Criando tabela chamados: " + CREATE_TABLE_CHAMADOS);
            db.execSQL(CREATE_TABLE_CHAMADOS);

            Log.d(TAG, "Tabelas criadas com sucesso");

        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar tabelas: ", e);
            throw e;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.d(TAG, "Atualizando banco de dados da versão " + oldVersion + " para " + newVersion);

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAMADOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
            onCreate(db);

            Log.d(TAG, "Banco atualizado com sucesso");

        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar banco: ", e);
            throw e;
        }
    }
}
