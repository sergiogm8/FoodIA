package com.example.foodia;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Registro {

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("calorias")
    private String calorias;

    @SerializedName("azucares")
    private String azucares;

    @SerializedName("fibra")
    private String fibra;

    @SerializedName("proteinas")
    private String proteinas;

    @SerializedName("grasas")
    private String grasas;

    @SerializedName("carbohidratos")
    private String carbohidratos;

    @SerializedName("imagen")
    private String imagen;

    public Registro(String nombre, String calorias, String azucares, String fibra, String proteinas,
                    String grasas, String carbohidratos, String imagen) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.azucares = azucares;
        this.fibra = fibra;
        this.proteinas = proteinas;
        this.grasas = grasas;
        this.carbohidratos = carbohidratos;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCalorias() {
        return calorias;
    }

    public String getAzucares() {
        return azucares;
    }

    public String getFibra() {
        return fibra;
    }

    public String getProteinas() {
        return proteinas;
    }

    public String getGrasas() {
        return grasas;
    }

    public String getCarbohidratos() {
        return carbohidratos;
    }

    public String getImagen() {
        return imagen;
    }
}
