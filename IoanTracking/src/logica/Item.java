package logica;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class Item implements Serializable {

    private String nombre;
    private String codigo;
    private String descripcion;
    private boolean prestado;
    private Tipo tipo;
    private Map<String, Categoria> categoriasDelItem;

    public Item(String nombre, String codigo, String descripcion, Tipo tipo) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.prestado = false;
        this.categoriasDelItem = new HashMap<String, Categoria>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isPrestado() {
        return prestado;
    }

    public void setPrestado(boolean prestado) {
        this.prestado = prestado;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Map<String, Categoria> getCategorias() {
        return categoriasDelItem;
    }

    public boolean agregarCategoria(Categoria categoriaNueva) {
        if (categoriaNueva == null) {
            return false;
        }

        if (categoriasDelItem.containsKey(categoriaNueva.getNombre())) {
            return false;
        }

        categoriasDelItem.put(categoriaNueva.getNombre(), categoriaNueva);
        return true;
    }

    public boolean eliminarCategoria(String nombreCategoria) {
        if (categoriasDelItem.containsKey(nombreCategoria)) {
            categoriasDelItem.remove(nombreCategoria);
            return true;
        }

        return false;
    }

    public boolean perteneceACategoria(String nombreCategoria) {
        if (nombreCategoria == null) {
        	return false;
        }
    	return categoriasDelItem.containsKey(nombreCategoria);
    }

    public void marcarComoPrestado() {
        prestado = true;
    }

    public void marcarComoDisponible() {
        prestado = false;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}