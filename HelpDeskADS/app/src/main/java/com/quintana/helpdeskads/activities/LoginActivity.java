package com.quintana.helpdeskads.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.quintana.helpdeskads.R;
import com.quintana.helpdeskads.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextSenha;
    private Button buttonLogin, buttonTeste;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar componentes
        inicializarComponentes();

        // Inicializar banco
        databaseHelper = new DatabaseHelper(this);

        // Configurar listeners
        configurarListeners();
    }

    private void inicializarComponentes() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonTeste = findViewById(R.id.buttonTeste);
    }

    private void configurarListeners() {
        // Botão Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarLogin();
            }
        });

        // Botão Teste (preenche dados automaticamente)
        buttonTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextEmail.setText("bruce@helpdesk.com");
                editTextSenha.setText("123456");
                realizarLogin();
            }
        });
    }

    private void realizarLogin() {
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();

        // Validações básicas
        if (email.isEmpty()) {
            editTextEmail.setError("Email é obrigatório");
            editTextSenha.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            editTextSenha.setError("Senha é obrigatória");
            editTextSenha.requestFocus();
            return;
        }

        // Verificar login no banco de dados
        if (verificarCredenciais(email, senha)) {
            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

            // Salvar email nas preferências
            getSharedPreferences("HelpDeskPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("usuario_email", email)
                    .apply();

            // Log para debug
            Log.d("LOGIN", "Email salvo: '" + email + "'");

            // Ir para a tela principal
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("usuario_email", email);
            startActivity(intent);
            finish(); // Fecha a tela de login

        } else {
            Toast.makeText(this, "Email ou senha incorretos!", Toast.LENGTH_SHORT).show();
            editTextSenha.setText(""); // Limpa a senha
        }
    }


    private boolean verificarCredenciais(String email, String senha) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query = "SELECT * FROM Usuario WHERE email = ? AND senha = ? AND status = 'ATIVO'";
        Cursor cursor = db.rawQuery(query, new String[]{email, senha});

        boolean loginValido = cursor.getCount() > 0;

        if (loginValido && cursor.moveToFirst()) {
            String nomeUsuario = cursor.getString(1); // Coluna nome_completo
            Log.d("LOGIN", "Login realizado por: " + nomeUsuario);
        }

        cursor.close();
        db.close();

        return loginValido;
    }
}
