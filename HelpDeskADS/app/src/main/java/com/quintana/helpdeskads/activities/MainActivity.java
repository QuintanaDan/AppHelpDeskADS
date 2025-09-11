package com.quintana.helpdeskads.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.quintana.helpdeskads.R;

public class MainActivity extends AppCompatActivity {

    private CardView cardNovoChamado, cardMeusChamados, cardRelatorios, cardConfiguracoes;
    private String usuarioEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Receber dados do Intent
        usuarioEmail = getIntent().getStringExtra("usuario_email");

        // Inicializar componentes
        inicializarComponentes();

        // Configurar listeners
        configurarListeners();
    }

    private void inicializarComponentes() {
        cardNovoChamado = findViewById(R.id.cardNovoChamado);
        cardMeusChamados = findViewById(R.id.cardMeusChamados);
        cardRelatorios = findViewById(R.id.cardRelatorios);
        cardConfiguracoes = findViewById(R.id.cardConfiguracoes);
    }

    private void configurarListeners() {
        // Card Novo Chamado
        cardNovoChamado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Abrindo NOVO CHAMADO", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, NovoChamadoActivity.class);
                intent.putExtra("usuario_email", usuarioEmail);
                startActivity(intent);
            }
        });

        // Card Meus Chamados
        cardMeusChamados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Abrindo MEUS CHAMADOS", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MeusChamadosActivity.class);
                intent.putExtra("usuario_email", usuarioEmail);
                startActivity(intent);
            }
        });

        // Card Relatórios
        cardRelatorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Relatórios em desenvolvimento", Toast.LENGTH_SHORT).show();
            }
        });

        // Card Configurações
        cardConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Configurações em desenvolvimento", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
