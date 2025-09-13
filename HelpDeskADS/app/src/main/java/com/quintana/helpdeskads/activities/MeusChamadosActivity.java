package com.quintana.helpdeskads.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.quintana.helpdeskads.R;
import com.quintana.helpdeskads.adapters.ChamadosAdapter;
import com.quintana.helpdeskads.database.DatabaseHelper;
import com.quintana.helpdeskads.models.Chamado;
import java.util.ArrayList;
import java.util.List;

public class MeusChamadosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChamados;
    private LinearLayout layoutSemChamados;
    private ChamadosAdapter chamadosAdapter;
    private List<Chamado> listaChamados;
    private DatabaseHelper databaseHelper;
    private String usuarioEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Log.d("MeusChamados", "Iniciando onCreate...");
            setContentView(R.layout.activity_meus_chamados);

            // Receber dados do Intent
            usuarioEmail = getIntent().getStringExtra("usuario_email");
            Log.d("MeusChamados", "Usuario email: " + usuarioEmail);

            // Inicializar banco de dados
            databaseHelper = new DatabaseHelper(this);

            // Inicializar componentes
            inicializarComponentes();

            // Configurar RecyclerView
            configurarRecyclerView();

            // Carregar dados do banco
            carregarChamadosDoBanco();

        } catch (Exception e) {
            Log.e("MeusChamados", "ERRO: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "ERRO: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar dados sempre que a tela voltar ao foco
        carregarChamadosDoBanco();
    }

    private void inicializarComponentes() {
        recyclerViewChamados = findViewById(R.id.recyclerViewChamados);
        layoutSemChamados = findViewById(R.id.layoutSemChamados);
        listaChamados = new ArrayList<>();
    }

    private void configurarRecyclerView() {
        recyclerViewChamados.setLayoutManager(new LinearLayoutManager(this));
        chamadosAdapter = new ChamadosAdapter(listaChamados, this);
        recyclerViewChamados.setAdapter(chamadosAdapter);
    }

    private void carregarChamadosDoBanco() {
        try {
            // Limpar lista atual
            listaChamados.clear();

            // DEBUG: Verificar o email recebido
            Log.d("MeusChamados", "Email recebido: '" + usuarioEmail + "'");

            if (usuarioEmail == null || usuarioEmail.isEmpty()) {
                Toast.makeText(this, "Email não foi passado corretamente!", Toast.LENGTH_LONG).show();
                return;
            }

            // Buscar usuário pelo email para pegar o ID
            int usuarioId = buscarUsuarioIdPorEmail(usuarioEmail);
            Log.d("MeusChamados", "Usuario ID encontrado: " + usuarioId);

            if (usuarioId == -1) {
                Toast.makeText(this, "Usuário não encontrado para email: " + usuarioEmail, Toast.LENGTH_LONG).show();
                // DEBUG: Mostrar todos os usuários do banco
                mostrarTodosUsuarios();
                return;
            }

            // ... resto do código igual
        } catch (Exception e) {
            Log.e("MeusChamados", "Erro ao carregar chamados: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Erro ao carregar chamados: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    private int buscarUsuarioIdPorEmail(String email) {
        try {
            Cursor cursor = databaseHelper.buscarUsuarioPorEmail(email);
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"));
                cursor.close();
                return id;
            }
        } catch (Exception e) {
            Log.e("MeusChamados", "Erro ao buscar usuário: " + e.getMessage());
        }
        return -1;
    }

    private void mostrarTodosUsuarios() {
        try {
            Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT * FROM Usuario", null);
            Log.d("MeusChamados", "=== TODOS OS USUÁRIOS ===");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"));
                    String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome_completo"));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                    Log.d("MeusChamados", "ID: " + id + " | Nome: " + nome + " | Email: '" + email + "'");

                } while (cursor.moveToNext());
                cursor.close();
            } else {
                Log.d("MeusChamados", "Nenhum usuário encontrado no banco!");
            }

        } catch (Exception e) {
            Log.e("MeusChamados", "Erro ao mostrar usuários: " + e.getMessage());
        }
    }


}
