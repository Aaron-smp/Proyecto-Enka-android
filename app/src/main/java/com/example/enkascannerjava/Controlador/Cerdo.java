package com.example.enkascannerjava.Controlador;

import java.io.Serializable;

public class Cerdo implements Serializable {
    public String nIde;
    public int imagen;
    public String raza;
    private String fechaNac;
    private String Sexo;
    private String vendido;

    public Cerdo(String nIde, int imagen, String raza, String fechaNac, String sexo, String vendido) {
        this.nIde = nIde;
        this.imagen = imagen;
        this.raza = raza;
        this.fechaNac = fechaNac;
        Sexo = sexo;
        this.vendido = vendido;
    }

    public String getnIde() {
        return nIde;
    }

    public void setnIde(String nIde) {
        this.nIde = nIde;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }

    public String getVendido() {
        return vendido;
    }

    public void setVendido(String vendido) {
        this.vendido = vendido;
    }
}
