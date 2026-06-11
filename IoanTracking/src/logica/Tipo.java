package logica;

import java.io.Serializable;

public class Tipo implements Serializable {

    private String nombre;
    private boolean esGenerico;

    public Tipo(String nombre, boolean esGenerico) {
        this.nombre = nombre;
        this.esGenerico = esGenerico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEsGenerico() {
        return esGenerico;
    }

    public void setEsGenerico(boolean esGenerico) {
        this.esGenerico = esGenerico;
    }

    @Override
    public String toString() {
        return nombre;
    }
}