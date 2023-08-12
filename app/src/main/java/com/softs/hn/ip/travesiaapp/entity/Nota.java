package com.softs.hn.ip.travesiaapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notas_table")
public class Nota implements Serializable{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Integer idNota;
    @NonNull
    @ColumnInfo(name = "lugar")
    private String lugar;

    @ColumnInfo(name = "fecha")
    private String fecha;

    @ColumnInfo(name = "comentario")
    private String comentario;

    @ColumnInfo(name = "acompaniante")
    private String acompaniante;

    @NonNull
    @ColumnInfo(name = "latitud")
    private String latitud;

    @ColumnInfo(name = "longitud")
    private String longitud;

    @ColumnInfo(name = "img")
    private int img;

    public Nota(@NonNull String lugar, String fecha, String comentario, String acompaniante, @NonNull String latitud, String longitud, int img) {
        this.lugar = lugar;
        this.fecha = fecha;
        this.comentario = comentario;
        this.acompaniante = acompaniante;
        this.latitud = latitud;
        this.longitud = longitud;
        this.img = img;
    }

    public Integer getIdNota() {
        return idNota;
    }

    public void setIdNota(Integer idNota) {
        this.idNota = idNota;
    }

    @NonNull
    public String getLugar() {
        return lugar;
    }

    public void setLugar(@NonNull String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getAcompaniante() {
        return acompaniante;
    }

    public void setAcompaniante(String acompaniante) {
        this.acompaniante = acompaniante;
    }

    @NonNull
    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(@NonNull String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
