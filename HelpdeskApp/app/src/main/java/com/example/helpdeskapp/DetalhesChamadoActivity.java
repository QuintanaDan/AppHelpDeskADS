package com.example.helpdeskapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpdeskapp.dao.ChamadoDAO;
import com.example.helpdeskapp.models.Chamado;
import com.example.helpdeskapp.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetalhesChamadoActivity extends AppCompatActivity {

    private TextView tvHeaderChamado, tvNumeroDetalhe, tvStatusDetalhe, tvTituloDetalhe,
            tvDescricaoDetalhe, tvPrioridadeDetalhe, tvDataAberturaDetalhe,
            tvUltimaAtualizacaoDetalhe, tvHistorico;

    private LinearLayout layoutAcoesCliente, layoutAcoesAdmin;

    private Button btnAtualizarChamado, btnCancelarChamado, btnIniciarAtendimento,
            btnResolverChamado, btnFecharChamado;

    private SessionManager sessionManager;
    private ChamadoDAO chamadoDAO;
    private Chamado chamado;
    private long chamadoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_chamado);

        // Configurar ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detalhes do Chamado");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Obter ID do chamado
        chamadoId = getIntent().getLongExtra("chamado_id", -1);
        if (chamadoId == -1) {
            Toast.makeText(this, "Erro: Chamado não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        inicializarComponentes();

        sessionManager = new SessionManager(this);
        chamadoDAO = new ChamadoDAO(this);

        carregarDadosChamado();
        configurarVisibilidadeBotoes();
        configurarEventos();
    }

    private void inicializarComponentes() {
        tvHeaderChamado = findViewById(R.id.tvHeaderChamado);
        tvNumeroDetalhe = findViewById(R.id.tvNumeroDetalhe);
        tvStatusDetalhe = findViewById(R.id.tvStatusDetalhe);
        tvTituloDetalhe = findViewById(R.id.tvTituloDetalhe);
        tvDescricaoDetalhe = findViewById(R.id.tvDescricaoDetalhe);
        tvPrioridadeDetalhe = findViewById(R.id.tvPrioridadeDetalhe);
        tvDataAberturaDetalhe = findViewById(R.id.tvDataAberturaDetalhe);
        tvUltimaAtualizacaoDetalhe = findViewById(R.id.tvUltimaAtualizacaoDetalhe);
        tvHistorico = findViewById(R.id.tvHistorico);

        layoutAcoesCliente = findViewById(R.id.layoutAcoesCliente);
        layoutAcoesAdmin = findViewById(R.id.layoutAcoesAdmin);

        btnAtualizarChamado = findViewById(R.id.btnAtualizarChamado);
        btnCancelarChamado = findViewById(R.id.btnCancelarChamado);
        btnIniciarAtendimento = findViewById(R.id.btnIniciarAtendimento);
        btnResolverChamado = findViewById(R.id.btnResolverChamado);
        btnFecharChamado = findViewById(R.id.btnFecharChamado);
    }

    private void carregarDadosChamado() {
        chamadoDAO.open();
        chamado = chamadoDAO.buscarPorId(chamadoId);
        chamadoDAO.close();

        if (chamado == null) {
            Toast.makeText(this, "Chamado não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        preencherDados();
    }

    private void preencherDados() {
        tvHeaderChamado.setText("🎫 " + chamado.getNumero());
        tvNumeroDetalhe.setText(chamado.getNumero());
        tvTituloDetalhe.setText(chamado.getTitulo());
        tvDescricaoDetalhe.setText(chamado.getDescricao());

        // Status
        String statusTexto = getStatusTexto(chamado.getStatus());
        tvStatusDetalhe.setText(statusTexto);
        tvStatusDetalhe.setBackgroundResource(getStatusBackground(chamado.getStatus()));

        // Prioridade
        String prioridadeTexto = getPrioridadeTexto(chamado.getPrioridade());
        tvPrioridadeDetalhe.setText(prioridadeTexto);

        // Datas
        if (chamado.getCreatedAt() != null && !chamado.getCreatedAt().isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                String dataFormatada = outputFormat.format(inputFormat.parse(chamado.getCreatedAt()));
                tvDataAberturaDetalhe.setText(dataFormatada);
            } catch (Exception e) {
                tvDataAberturaDetalhe.setText(chamado.getCreatedAt());
            }
        }

        if (chamado.getUpdatedAt() != null && !chamado.getUpdatedAt().isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                String dataFormatada = outputFormat.format(inputFormat.parse(chamado.getUpdatedAt()));
                tvUltimaAtualizacaoDetalhe.setText(dataFormatada);
            } catch (Exception e) {
                tvUltimaAtualizacaoDetalhe.setText(chamado.getUpdatedAt());
            }
        }

        // Histórico simples
        String historico = "• " + tvDataAberturaDetalhe.getText() + " - Chamado aberto pelo cliente";
        if (chamado.getStatus() == Chamado.STATUS_EM_ANDAMENTO) {
            historico += "\n• Em atendimento";
        } else if (chamado.getStatus() == Chamado.STATUS_RESOLVIDO) {
            historico += "\n• Marcado como resolvido";
        } else if (chamado.getStatus() == Chamado.STATUS_FECHADO) {
            historico += "\n• Chamado fechado";
        }
        tvHistorico.setText(historico);
    }

    private void configurarVisibilidadeBotoes() {
        boolean isAdmin = sessionManager.isAdmin();
        long userId = sessionManager.getUserId();
        boolean isMeuChamado = (chamado.getClienteId() == userId);
        boolean chamadoAberto = (chamado.getStatus() == Chamado.STATUS_ABERTO ||
                chamado.getStatus() == Chamado.STATUS_EM_ANDAMENTO);

        if (isAdmin) {
            layoutAcoesCliente.setVisibility(View.GONE);
            layoutAcoesAdmin.setVisibility(View.VISIBLE);

            // Configurar botões admin baseado no status
            btnIniciarAtendimento.setVisibility(
                    chamado.getStatus() == Chamado.STATUS_ABERTO ? View.VISIBLE : View.GONE);
            btnResolverChamado.setVisibility(
                    chamado.getStatus() == Chamado.STATUS_EM_ANDAMENTO ? View.VISIBLE : View.GONE);
            btnFecharChamado.setVisibility(
                    chamado.getStatus() == Chamado.STATUS_RESOLVIDO ? View.VISIBLE : View.GONE);

        } else {
            layoutAcoesAdmin.setVisibility(View.GONE);
            layoutAcoesCliente.setVisibility(isMeuChamado && chamadoAberto ? View.VISIBLE : View.GONE);
        }
    }

    private void configurarEventos() {
        btnCancelarChamado.setOnClickListener(v -> mostrarConfirmacaoCancelamento());
        btnIniciarAtendimento.setOnClickListener(v -> iniciarAtendimento());
        btnResolverChamado.setOnClickListener(v -> resolverChamado());
        btnFecharChamado.setOnClickListener(v -> fecharChamado());
    }

    private void mostrarConfirmacaoCancelamento() {
        new AlertDialog.Builder(this)
                .setTitle("Cancelar Chamado")
                .setMessage("Tem certeza que deseja cancelar este chamado?")
                .setPositiveButton("Sim", (dialog, which) -> cancelarChamado())
                .setNegativeButton("Não", null)
                .show();
    }

    private void cancelarChamado() {
        chamadoDAO.open();
        boolean sucesso = chamadoDAO.atualizarStatus(chamadoId, Chamado.STATUS_FECHADO);
        chamadoDAO.close();

        if (sucesso) {
            Toast.makeText(this, "Chamado cancelado com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao cancelar chamado", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarAtendimento() {
        chamadoDAO.open();
        boolean sucesso = chamadoDAO.atualizarStatus(chamadoId, Chamado.STATUS_EM_ANDAMENTO);
        chamadoDAO.close();

        if (sucesso) {
            Toast.makeText(this, "Atendimento iniciado", Toast.LENGTH_SHORT).show();
            carregarDadosChamado();
            configurarVisibilidadeBotoes();
        } else {
            Toast.makeText(this, "Erro ao iniciar atendimento", Toast.LENGTH_SHORT).show();
        }
    }

    private void resolverChamado() {
        chamadoDAO.open();
        boolean sucesso = chamadoDAO.atualizarStatus(chamadoId, Chamado.STATUS_RESOLVIDO);
        chamadoDAO.close();

        if (sucesso) {
            Toast.makeText(this, "Chamado marcado como resolvido", Toast.LENGTH_SHORT).show();
            carregarDadosChamado();
            configurarVisibilidadeBotoes();
        } else {
            Toast.makeText(this, "Erro ao resolver chamado", Toast.LENGTH_SHORT).show();
        }
    }

    private void fecharChamado() {
        chamadoDAO.open();
        boolean sucesso = chamadoDAO.atualizarStatus(chamadoId, Chamado.STATUS_FECHADO);
        chamadoDAO.close();

        if (sucesso) {
            Toast.makeText(this, "Chamado fechado", Toast.LENGTH_SHORT).show();
            carregarDadosChamado();
            configurarVisibilidadeBotoes();
        } else {
            Toast.makeText(this, "Erro ao fechar chamado", Toast.LENGTH_SHORT).show();
        }
    }

    private String getStatusTexto(int status) {
        switch (status) {
            case Chamado.STATUS_ABERTO: return "ABERTO";
            case Chamado.STATUS_EM_ANDAMENTO: return "EM ANDAMENTO";
            case Chamado.STATUS_RESOLVIDO: return "RESOLVIDO";
            case Chamado.STATUS_FECHADO: return "FECHADO";
            default: return "DESCONHECIDO";
        }
    }

    private int getStatusBackground(int status) {
        switch (status) {
            case Chamado.STATUS_ABERTO: return R.drawable.status_aberto;
            case Chamado.STATUS_EM_ANDAMENTO: return R.drawable.status_progresso;
            case Chamado.STATUS_RESOLVIDO: return R.drawable.status_resolvido;
            case Chamado.STATUS_FECHADO: return R.drawable.status_fechado;
            default: return R.drawable.status_aberto;
        }
    }

    private String getPrioridadeTexto(int prioridade) {
        switch (prioridade) {
            case Chamado.PRIORIDADE_BAIXA: return "🟢 BAIXA";
            case Chamado.PRIORIDADE_MEDIA: return "🟡 MÉDIA";
            case Chamado.PRIORIDADE_ALTA: return "🟠 ALTA";
            case Chamado.PRIORIDADE_CRITICA: return "🔴 CRÍTICA";
            default: return "🟡 MÉDIA";
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
