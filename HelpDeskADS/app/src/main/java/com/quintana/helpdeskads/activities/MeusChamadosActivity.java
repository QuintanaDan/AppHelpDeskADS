package com.quintana.helpdeskads.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.quintana.helpdeskads.R;
import com.quintana.helpdeskads.adapters.ChamadosAdapter;
import com.quintana.helpdeskads.models.Chamado;
import java.util.ArrayList;
import java.util.List;

public class MeusChamadosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChamados;
    private LinearLayout layoutSemChamados;
    private ChamadosAdapter chamadosAdapter;
    private List<Chamado> listaChamados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Log.d("MeusChamados", "Iniciando onCreate...");
            setContentView(R.layout.activity_meus_chamados);
            Log.d("MeusChamados", "Layout definido com sucesso!");

            // Inicializar componentes
            inicializarComponentes();

            // Configurar RecyclerView
            configurarRecyclerView();

            // Carregar dados (simulados)
            carregarDados();

            Toast.makeText(this, "Tela carregada com sucesso!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("MeusChamados", "ERRO: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "ERRO: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
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

    private void carregarDados() {
        // Ordem correta: id, titulo, descricao, categoria, prioridade, status, dataCriacao, usuarioId
        listaChamados.add(new Chamado(
                1,                                    // id (int)
                "Problema no computador",             // titulo (String)
                "O computador está muito lento",      // descricao (String)
                "Hardware",                           // categoria (String)
                "Alta",                              // prioridade (String)
                "Aberto",                            // status (String)
                "11/09/2024 14:30",                  // dataCriacao (String)
                123                                  // usuarioId (int)
        ));

        listaChamados.add(new Chamado(
                2,                                   // id (int)
                "Erro no sistema",                   // titulo (String)
                "Sistema travando constantemente",   // descricao (String)
                "Software",                         // categoria (String)
                "Media",                            // prioridade (String)
                "Em Andamento",                     // status (String)
                "10/09/2024 09:15",                 // dataCriacao (String)
                123                                 // usuarioId (int)
        ));

        listaChamados.add(new Chamado(
                3,                                  // id (int)
                "Impressora não funciona",          // titulo (String)
                "Impressora não está respondendo",  // descricao (String)
                "Hardware",                         // categoria (String)
                "Baixa",                           // prioridade (String)
                "Fechado",                         // status (String)
                "09/09/2024 16:45",                // dataCriacao (String)
                123                                // usuarioId (int)
        ));

        listaChamados.add(new Chamado(
                4,                                 // id (int)
                "Internet lenta",                  // titulo (String)
                "Conexão muito instável",          // descricao (String)
                "Rede",                           // categoria (String)
                "Urgente",                        // prioridade (String)
                "Aberto",                         // status (String)
                "11/09/2024 10:20",               // dataCriacao (String)
                123                               // usuarioId (int)
        ));

        // Atualizar adapter
        chamadosAdapter.notifyDataSetChanged();

        // Mostrar/ocultar mensagem de sem chamados
        if (listaChamados.isEmpty()) {
            layoutSemChamados.setVisibility(android.view.View.VISIBLE);
            recyclerViewChamados.setVisibility(android.view.View.GONE);
        } else {
            layoutSemChamados.setVisibility(android.view.View.GONE);
            recyclerViewChamados.setVisibility(android.view.View.VISIBLE);
        }
    }


}
