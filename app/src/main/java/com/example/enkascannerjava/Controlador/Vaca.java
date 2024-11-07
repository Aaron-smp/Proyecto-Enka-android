package com.example.enkascannerjava.Controlador;

import java.io.Serializable;

public class Vaca implements Serializable {
    public String nIde;
    public int imagen;
    public String raza;
    private String fechaNac;
    private String momenRepro;
    private String Sexo;
    private String vendido;

    public Vaca(String nIde, int imagen, String raza, String fechaNac, String momenRepro, String sexo, String vendido) {
        this.nIde = nIde;
        this.imagen = imagen;
        this.raza = raza;
        this.fechaNac = fechaNac;
        this.momenRepro = momenRepro;
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

    public String getMomenRepro() {
        return momenRepro;
    }

    public void setMomenRepro(String momenRepro) {
        this.momenRepro = momenRepro;
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
