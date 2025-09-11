package com.quintana.helpdeskads.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Informações do Banco de Dados
    private static final String DATABASE_NAME = "HelpDeskADS.db";
    private static final int DATABASE_VERSION = 1;

    // Nomes das Tabelas
    private static final String TABLE_USUARIO = "Usuario";
    private static final String TABLE_CHAMADO = "Chamado";
    private static final String TABLE_TECNICO = "Tecnico";

    // Construtor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criar tabela Usuario
        String CREATE_USUARIO_TABLE = "CREATE TABLE " + TABLE_USUARIO + "("
                + "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome_completo TEXT NOT NULL,"
                + "email TEXT NOT NULL UNIQUE,"
                + "senha TEXT NOT NULL,"
                + "cargo TEXT NOT NULL,"
                + "telefone TEXT,"
                + "status TEXT DEFAULT 'ATIVO',"
                + "data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

        // Criar tabela Tecnico
        String CREATE_TECNICO_TABLE = "CREATE TABLE " + TABLE_TECNICO + "("
                + "id_tecnico INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "email TEXT NOT NULL,"
                + "especialidade TEXT NOT NULL,"
                + "disponibilidade TEXT DEFAULT 'DISPONIVEL',"
                + "telefone TEXT"
                + ")";

        // Criar tabela Chamado
        String CREATE_CHAMADO_TABLE = "CREATE TABLE " + TABLE_CHAMADO + "("
                + "id_chamado INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "titulo TEXT NOT NULL,"
                + "descricao TEXT NOT NULL,"
                + "categoria TEXT NOT NULL,"
                + "prioridade TEXT DEFAULT 'MEDIA',"
                + "status TEXT DEFAULT 'ABERTO',"
                + "data_abertura DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "data_fechamento DATETIME NULL,"
                + "solucao TEXT NULL,"
                + "id_usuario INTEGER NOT NULL,"
                + "id_tecnico INTEGER NULL,"
                + "FOREIGN KEY(id_usuario) REFERENCES " + TABLE_USUARIO + "(id_usuario),"
                + "FOREIGN KEY(id_tecnico) REFERENCES " + TABLE_TECNICO + "(id_tecnico)"
                + ")";

        // Executar criação das tabelas
        db.execSQL(CREATE_USUARIO_TABLE);
        db.execSQL(CREATE_TECNICO_TABLE);
        db.execSQL(CREATE_CHAMADO_TABLE);

        // Inserir dados iniciais
        inserirDadosIniciais(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Apagar tabelas antigas se existirem
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAMADO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TECNICO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);

        // Recriar tabelas
        onCreate(db);
    }

    private void inserirDadosIniciais(SQLiteDatabase db) {
        // Inserir usuário teste
        String insertUsuario = "INSERT INTO " + TABLE_USUARIO +
                " (nome_completo, email, senha, cargo, telefone) VALUES " +
                "('Bruce Admin', 'bruce@helpdesk.com', '123456', 'Administrador', '(11) 99999-9999')";

        // Inserir técnico teste
        String insertTecnico = "INSERT INTO " + TABLE_TECNICO +
                " (nome, email, especialidade, telefone) VALUES " +
                "('João Silva', 'joao@helpdesk.com', 'Hardware e Software', '(11) 88888-8888')";

        db.execSQL(insertUsuario);
        db.execSQL(insertTecnico);
    }

    // Método para testar e mostrar dados do banco
    public void mostrarDadosBanco() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Verificar usuários
        Cursor cursorUsuarios = db.rawQuery("SELECT * FROM Usuario", null);
        Log.d("DATABASE", "=== USUÁRIOS ===");
        if (cursorUsuarios.moveToFirst()) {
            do {
                Log.d("DATABASE", "ID: " + cursorUsuarios.getInt(0) +
                        " | Nome: " + cursorUsuarios.getString(1) +
                        " | Email: " + cursorUsuarios.getString(2));
            } while (cursorUsuarios.moveToNext());
        }
        cursorUsuarios.close();

        // Verificar técnicos
        Cursor cursorTecnicos = db.rawQuery("SELECT * FROM Tecnico", null);
        Log.d("DATABASE", "=== TÉCNICOS ===");
        if (cursorTecnicos.moveToFirst()) {
            do {
                Log.d("DATABASE", "ID: " + cursorTecnicos.getInt(0) +
                        " | Nome: " + cursorTecnicos.getString(1) +
                        " | Email: " + cursorTecnicos.getString(2));
            } while (cursorTecnicos.moveToNext());
        }
        cursorTecnicos.close();

        // Verificar chamados
        Cursor cursorChamados = db.rawQuery("SELECT * FROM Chamado", null);
        Log.d("DATABASE", "=== CHAMADOS ===");
        Log.d("DATABASE", "Total de chamados: " + cursorChamados.getCount());
        cursorChamados.close();
    }

}
