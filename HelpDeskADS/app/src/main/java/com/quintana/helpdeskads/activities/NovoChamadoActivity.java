package com.quintana.helpdeskads.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.quintana.helpdeskads.R;
import com.quintana.helpdeskads.database.DatabaseHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NovoChamadoActivity extends AppCompatActivity {

    private EditText editTextTitulo, editTextDescricao;
    private Spinner spinnerCategoria, spinnerPrioridade;
    private Button buttonSalvarChamado, buttonCancelar;
    private DatabaseHelper databaseHelper;
    private String usuarioEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_chamado);

        // Receber email do usuário
        usuarioEmail = getIntent().getStringExtra("usuario_email");

        // Inicializar componentes
        inicializarComponentes();

        // Inicializar banco de dados
        databaseHelper = new DatabaseHelper(this);

        // Configurar spinners
        configurarSpinners();

        // Configurar listeners
        configurarListeners();
    }

    private void inicializarComponentes() {
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerPrioridade = findViewById(R.id.spinnerPrioridade);
        buttonSalvarChamado = findViewById(R.id.buttonSalvarChamado);
        buttonCancelar = findViewById(R.id.buttonCancelar);
    }

    private void configurarSpinners() {
        // Categorias
        String[] categorias = {
                "Selecione a categoria...",
                "Hardware",
                "Software",
                "Rede/Internet",
                "Email",
                "Sistema",
                "Impressora",
                "Telefonia",
                "Outros"
        };

        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categorias);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategoria);

        // Prioridades
        String[] prioridades = {
                "Selecione a prioridade...",
                "BAIXA - Não afeta o trabalho",
                "MEDIA - Afeta Parcialmente",
                "ALTA - Afeta Significativamente",
                "URGENTE - Sistema Parado"
        };

        ArrayAdapter<String> adapterPrioridade = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, prioridades);
        adapterPrioridade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrioridade.setAdapter(adapterPrioridade);
    }

    private void configurarListeners() {
        buttonSalvarChamado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarChamado();
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void salvarChamado() {
        String titulo = editTextTitulo.getText().toString().trim();
        String descricao = editTextDescricao.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String prioridade = spinnerPrioridade.getSelectedItem().toString();

        // Validações
        if (titulo.isEmpty()) {
            editTextTitulo.setError("Título é obrigatório");
            editTextTitulo.requestFocus();
            return;
        }

        if (descricao.isEmpty()) {
            editTextDescricao.setError("Descrição é obrigatória");
            editTextDescricao.requestFocus();
            return;
        }

        if (categoria.equals("Selecione a categoria...")) {
            Toast.makeText(this, "Selecione uma categoria", Toast.LENGTH_SHORT).show();
            return;
        }

        if (prioridade.equals("Selecione a prioridade...")) {
            Toast.makeText(this, "Selecione uma prioridade", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obter ID do usuário
        int usuarioId = obterIdUsuario(usuarioEmail);
        if (usuarioId == -1) {
            Toast.makeText(this, "Erro: Usuário não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Salvar no banco
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("titulo", titulo);
        values.put("descricao", descricao);
        values.put("categoria", categoria);
        values.put("prioridade", prioridade);
        values.put("status", "ABERTO");
        values.put("usuario_id", usuarioId);

        // Data atual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dataAtual = sdf.format(new Date());
        values.put("data_criacao", dataAtual);

        long resultado = db.insert("Chamado", null, values);
        db.close();

        if (resultado != -1) {
            Toast.makeText(this, "Chamado criado com sucesso!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Para atualizar a tela anterior
            finish();
        } else {
            Toast.makeText(this, "Erro ao criar chamado", Toast.LENGTH_SHORT).show();
        }
    }

    private int obterIdUsuario(String email) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(
                "SELECT id FROM Usuario WHERE email = ?", new String[]{email});

        int usuarioId = -1;
        if (cursor.moveToFirst()) {
            usuarioId = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return usuarioId;
    }
}
