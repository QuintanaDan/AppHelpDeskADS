package com.quintana.helpdeskads.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

        // Receber email do login
        usuarioEmail = getIntent().getStringExtra("usuario_email");

        // DEBUG: Verificar se o email foi recebido
        Log.d("MainActivity", "Email do usuário logado: '" + usuarioEmail + "'");

        // Se não recebeu, tentar pegar das preferências
        if (usuarioEmail == null || usuarioEmail.isEmpty()) {
            SharedPreferences prefs = getSharedPreferences("HelpDeskPrefs", MODE_PRIVATE);
            usuarioEmail = prefs.getString("usuario_email", "");
            Log.d("MainActivity", "Email das preferências: '" + usuarioEmail + "'");
        }

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

        // Card Meus Chamados - CORRIGIDO
        cardMeusChamados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Abrindo MEUS CHAMADOS", Toast.LENGTH_SHORT).show();
                Intent intentMeusChamados = new Intent(MainActivity.this, MeusChamadosActivity.class);
                intentMeusChamados.putExtra("usuario_email", usuarioEmail);
                Log.d("MainActivity", "Passando email para MeusChamados: '" + usuarioEmail + "'");
                startActivity(intentMeusChamados);
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
