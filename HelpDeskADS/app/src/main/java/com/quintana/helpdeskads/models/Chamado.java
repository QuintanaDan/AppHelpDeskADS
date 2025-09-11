package com.quintana.helpdeskads.models;

public class Chamado {
    private int id;
    private String titulo;
    private String descricao;
    private String categoria;
    private String prioridade;
    private String status;
    private String dataCriacao;
    private int usuarioId;

    // Construtor vazio
    public Chamado() {}

    // Construtor completo
    public Chamado(int id, String titulo, String descricao, String categoria,
                   String prioridade, String status, String dataCriacao, int usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.prioridade = prioridade;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    // Método para formatar data
    public String getDataFormatada() {
        if (dataCriacao == null || dataCriacao.isEmpty()) return "";

        try {
            // Formato: 2024-09-11 14:30:00 → 11/09/2024 14:30
            String[] partes = dataCriacao.split(" ");
            if (partes.length >= 2) {
                String[] data = partes[0].split("-");
                String hora = partes[1].substring(0, 5); // HH:MM
                return data[2] + "/" + data[1] + "/" + data[0] + " " + hora;
            }
        } catch (Exception e) {
            return dataCriacao;
        }

        return dataCriacao;
    }

    // Método para cor da prioridade
    public int getCorPrioridade() {
        switch (prioridade.toUpperCase()) {
            case "BAIXA": return 0xFF4CAF50; // Verde
            case "MEDIA": return 0xFFFF9800; // Laranja
            case "ALTA": return 0xFFFF5722; // Vermelho Claro
            case "URGENTE": return 0xFFD32F2F; // Vermelho Escuro
            default: return 0xFF757575; // Cinza
        }
    }

    // Método para cor do status
    public int getCorStatus() {
        switch (status.toUpperCase()) {
            case "ABERTO": return 0xFF2196F3; // Azul
            case "EM ANDAMENTO": return 0xFFFF9800; // Laranja
            case "FECHADO": return 0xFF4CAF50; // Verde
            case "CANCELADO": return 0xFF757575; // Cinza
            default: return 0xFF757575;
        }
    }
}
