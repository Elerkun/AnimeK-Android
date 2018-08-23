package com.example.falle.myapplication;

public class Usuario {

    String nombre_usuario;
    String foto;
    String idUsuario;


    public Usuario() {
    }

    public Usuario(String nombre_usuario, String foto, String idUsuario) {
        this.nombre_usuario = nombre_usuario;
        this.foto = foto;
        this.idUsuario=idUsuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}