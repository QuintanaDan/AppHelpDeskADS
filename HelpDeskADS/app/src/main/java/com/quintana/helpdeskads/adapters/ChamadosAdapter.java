package com.quintana.helpdeskads.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.quintana.helpdeskads.R;
import com.quintana.helpdeskads.models.Chamado;
import java.util.List;

public class ChamadosAdapter extends RecyclerView.Adapter<ChamadosAdapter.ChamadoViewHolder> {

    private List<Chamado> listaChamados;
    private Context context;

    public ChamadosAdapter(List<Chamado> listaChamados, Context context) {
        this.listaChamados = listaChamados;
        this.context = context;
    }

    @NonNull
    @Override
    public ChamadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chamado, parent, false);
        return new ChamadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChamadoViewHolder holder, int position) {
        Chamado chamado = listaChamados.get(position);

        // Definir dados
        holder.textTitulo.setText(chamado.getTitulo());
        holder.textDescricao.setText(chamado.getDescricao());
        holder.textCategoria.setText(chamado.getCategoria());
        holder.textPrioridade.setText(chamado.getPrioridade());
        holder.textStatus.setText(chamado.getStatus());
        holder.textData.setText(chamado.getDataCriacao());

        // Definir cores do status
        switch (chamado.getStatus().toLowerCase()) {
            case "aberto":
                holder.textStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_bright));
                break;
            case "em_andamento":
                holder.textStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));
                break;
            case "fechado":
                holder.textStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
                break;
            default:
                holder.textStatus.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                break;
        }

        // Definir cores da prioridade
        switch (chamado.getPrioridade().toLowerCase()) {
            case "alta":
                holder.textPrioridade.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                break;
            case "media":
                holder.textPrioridade.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                break;
            case "baixa":
                holder.textPrioridade.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                break;
        }

        // Click listener do card
        holder.cardChamado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aqui você pode abrir uma tela de detalhes do chamado
                // Por enquanto, só um toast
                android.widget.Toast.makeText(context, "Chamado: " + chamado.getTitulo(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaChamados.size();
    }

    public static class ChamadoViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardChamado;
        TextView textTitulo, textDescricao, textCategoria, textPrioridade, textStatus, textData;

        public ChamadoViewHolder(@NonNull View itemView) {
            super(itemView);
            cardChamado = itemView.findViewById(R.id.cardChamado);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textCategoria = itemView.findViewById(R.id.textCategoria);
            textPrioridade = itemView.findViewById(R.id.textPrioridade);
            textStatus = itemView.findViewById(R.id.textStatus);
            textData = itemView.findViewById(R.id.textData);
        }
    }
}
