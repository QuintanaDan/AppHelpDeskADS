package com.quintana.helpdeskads.models;

public class Usuario {
    private int idUsuario;
    private String nomeCompleto;
    private String email;
    private String senha;
    private String cargo;
    private String telefone;
    private String status;
    private String dataCadastro;

    // Construtores
    public Usuario() {}

    public Usuario(String nomeCompleto, String email, String senha, String cargo, String telefone) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.telefone = telefone;
    }

    // Getters e Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
}
