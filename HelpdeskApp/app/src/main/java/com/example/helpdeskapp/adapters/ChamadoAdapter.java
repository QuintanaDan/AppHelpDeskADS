package com.example.helpdeskapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.helpdeskapp.R;
import com.example.helpdeskapp.models.Chamado;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChamadoAdapter extends RecyclerView.Adapter<ChamadoAdapter.ChamadoViewHolder> {

    private List<Chamado> chamados;
    private Context context;
    private OnChamadoClickListener listener;

    public interface OnChamadoClickListener {
        void onChamadoClick(Chamado chamado);
    }

    public ChamadoAdapter(Context context, List<Chamado> chamados) {
        this.context = context;
        this.chamados = chamados;
    }

    public void setOnChamadoClickListener(OnChamadoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChamadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chamado, parent, false);
        return new ChamadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChamadoViewHolder holder, int position) {
        Chamado chamado = chamados.get(position);

        holder.tvNumero.setText(chamado.getNumero());
        holder.tvTitulo.setText(chamado.getTitulo());
        holder.tvDescricao.setText(chamado.getDescricao());

        // Status
        String statusTexto = getStatusTexto(chamado.getStatus());
        holder.tvStatus.setText(statusTexto);
        holder.tvStatus.setBackgroundResource(getStatusBackground(chamado.getStatus()));

        // Prioridade
        String prioridadeTexto = getPrioridadeTexto(chamado.getPrioridade());
        holder.tvPrioridade.setText(prioridadeTexto);

        // Data
        if (chamado.getCreatedAt() != null && !chamado.getCreatedAt().isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String dataFormatada = outputFormat.format(inputFormat.parse(chamado.getCreatedAt()));
                holder.tvData.setText(dataFormatada);
            } catch (Exception e) {
                holder.tvData.setText(chamado.getCreatedAt());
            }
        }

        // Click listener no onBindViewHolder
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChamadoClick(chamado);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chamados.size();
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

    public static class ChamadoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumero, tvTitulo, tvDescricao, tvStatus, tvPrioridade, tvData;

        public ChamadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumero = itemView.findViewById(R.id.tvNumero);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDescricao = itemView.findViewById(R.id.tvDescricao);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPrioridade = itemView.findViewById(R.id.tvPrioridade);
            tvData = itemView.findViewById(R.id.tvData);
        }
    }
}
