package com.example.manzreport_depa.model;

import com.google.firebase.Timestamp;

public class terminado {
    String titulo;
    String ubicacion;
    Timestamp date;
    public terminado(){

    }

    public terminado(String titulo, String ubicacion,Timestamp date) {
        this.titulo = titulo;
        this.ubicacion = ubicacion;
        this.date = date;


    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
