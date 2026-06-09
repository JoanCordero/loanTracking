package controladora;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import logica.Categoria;
import logica.Item;
import logica.Persona;
import logica.Prestamo;
import logica.Tipo;

public class ControladoraPrestamos {

    private Map<String, Persona> personasRegistradas;
    private Map<String, Categoria> categoriasRegistradas;
    private Map<String, Tipo> tiposRegistrados;
    private Map<String, Item> itemsRegistrados;
    private Map<Integer, Prestamo> prestamosRegistrados;

    private int consecutivoPrestamo;
    private Tipo tipoGenerico;

    public ControladoraPrestamos() {
        personasRegistradas = new HashMap<String, Persona>();
        categoriasRegistradas = new HashMap<String, Categoria>();
        tiposRegistrados = new HashMap<String, Tipo>();
        itemsRegistrados = new HashMap<String, Item>();
        prestamosRegistrados = new HashMap<Integer, Prestamo>();

        consecutivoPrestamo = 1;

        tipoGenerico = new Tipo("General", true);
        tiposRegistrados.put(tipoGenerico.getNombre(), tipoGenerico);
    }

    public boolean crearPersona(String nombre, String telefono, String correo) {
        if (nombre == null || telefono == null || correo == null) {
            return false;
        }

        if (nombre.equals("") || telefono.equals("") || correo.equals("")) {
            return false;
        }

        if (existePersona(correo)) {
            return false;
        }

        Persona personaNueva = new Persona(nombre, telefono, correo);
        personasRegistradas.put(correo, personaNueva);

        return true;
    }

    public Persona buscarPersona(String correo) {
        if (correo == null) {
            return null;
        }

        if (personasRegistradas.containsKey(correo)) {
            return personasRegistradas.get(correo);
        }

        return null;
    }

    public boolean modificarPersona(String correo, String nombre, String telefono) {
        Persona personaEncontrada = buscarPersona(correo);

        if (personaEncontrada == null) {
            return false;
        }

        if (nombre == null || telefono == null) {
            return false;
        }

        if (nombre.equals("") || telefono.equals("")) {
            return false;
        }

        personaEncontrada.setNombre(nombre);
        personaEncontrada.setTelefono(telefono);

        return true;
    }

    public boolean borrarPersona(String correo) {
        if (existePersona(correo) == false) {
            return false;
        }

        if (personaTienePrestamoActivo(correo)) {
            return false;
        }

        personasRegistradas.remove(correo);
        return true;
    }

    public ArrayList<Persona> listarPersonas() {
        ArrayList<Persona> listaPersonas = new ArrayList<Persona>();

        for (Persona personaActual : personasRegistradas.values()) {
            listaPersonas.add(personaActual);
        }

        return listaPersonas;
    }
    
    public boolean crearCategoria(String nombre) {
        if (nombre == null) {
            return false;
        }

        if (nombre.equals("")) {
            return false;
        }

        if (existeCategoria(nombre)) {
            return false;
        }

        Categoria categoriaNueva = new Categoria(nombre);
        categoriasRegistradas.put(nombre, categoriaNueva);

        return true;
    }

    public Categoria buscarCategoria(String nombre) {
        if (nombre == null) {
            return null;
        }

        if (categoriasRegistradas.containsKey(nombre)) {
            return categoriasRegistradas.get(nombre);
        }

        return null;
    }

    public boolean modificarCategoria(String nombreActual, String nombreNuevo) {
        if (nombreActual == null || nombreNuevo == null) {
            return false;
        }

        if (nombreActual.equals("") || nombreNuevo.equals("")) {
            return false;
        }

        Categoria categoriaEncontrada = buscarCategoria(nombreActual);

        if (categoriaEncontrada == null) {
            return false;
        }

        if (!nombreActual.equals(nombreNuevo) && existeCategoria(nombreNuevo)) {
            return false;
        }

        categoriasRegistradas.remove(nombreActual);

        categoriaEncontrada.setNombre(nombreNuevo);

        categoriasRegistradas.put(nombreNuevo, categoriaEncontrada);

        for (Item itemActual : itemsRegistrados.values()) {
            if (itemActual.perteneceACategoria(nombreActual)) {
                itemActual.eliminarCategoria(nombreActual);
                itemActual.agregarCategoria(categoriaEncontrada);
            }
        }

        return true;
    }

    public boolean borrarCategoria(String nombre) {
        if (existeCategoria(nombre) == false) {
            return false;
        }

        categoriasRegistradas.remove(nombre);

        for (Item itemActual : itemsRegistrados.values()) {
            itemActual.eliminarCategoria(nombre);
        }

        return true;
    }

    public ArrayList<Categoria> listarCategorias() {
        ArrayList<Categoria> listaCategorias = new ArrayList<Categoria>();

        for (Categoria categoriaActual : categoriasRegistradas.values()) {
            listaCategorias.add(categoriaActual);
        }

        return listaCategorias;
    }
    
    public boolean crearTipo(String nombre, boolean esGenerico) {
        if (nombre == null) {
            return false;
        }

        if (nombre.equals("")) {
            return false;
        }

        if (existeTipo(nombre)) {
            return false;
        }

        Tipo tipoNuevo = new Tipo(nombre, esGenerico);

        if (esGenerico == true) {
            tipoGenerico.setEsGenerico(false);
            tipoGenerico = tipoNuevo;
        }

        tiposRegistrados.put(nombre, tipoNuevo);

        return true;
    }

    public Tipo buscarTipo(String nombre) {
        if (nombre == null) {
            return null;
        }

        if (tiposRegistrados.containsKey(nombre)) {
            return tiposRegistrados.get(nombre);
        }

        return null;
    }

    public boolean modificarTipo(String nombreActual, String nombreNuevo) {
        if (nombreActual == null || nombreNuevo == null) {
            return false;
        }

        if (nombreActual.equals("") || nombreNuevo.equals("")) {
            return false;
        }

        Tipo tipoEncontrado = buscarTipo(nombreActual);

        if (tipoEncontrado == null) {
            return false;
        }

        if (!nombreActual.equals(nombreNuevo) && existeTipo(nombreNuevo)) {
            return false;
        }

        tiposRegistrados.remove(nombreActual);

        tipoEncontrado.setNombre(nombreNuevo);

        tiposRegistrados.put(nombreNuevo, tipoEncontrado);

        return true;
    }

    public boolean borrarTipo(String nombre) {
        if (existeTipo(nombre) == false) {
            return false;
        }

        Tipo tipoEncontrado = buscarTipo(nombre);

        if (tipoEncontrado.isEsGenerico() == true) {
            return false;
        }

        reasignarItemsATipoGenerico(nombre);

        tiposRegistrados.remove(nombre);

        return true;
    }

    public ArrayList<Tipo> listarTipos() {
        ArrayList<Tipo> listaTipos = new ArrayList<Tipo>();

        for (Tipo tipoActual : tiposRegistrados.values()) {
            listaTipos.add(tipoActual);
        }

        return listaTipos;
    }

    private boolean existePersona(String correo) {
        if (correo == null) {
            return false;
        }

        return personasRegistradas.containsKey(correo);
    }
    
    
    private boolean existeCategoria(String nombre) {
        if (nombre == null) {
            return false;
        }

        return categoriasRegistradas.containsKey(nombre);
    }
    

    private boolean personaTienePrestamoActivo(String correo) {
        Persona personaEncontrada = buscarPersona(correo);

        if (personaEncontrada == null) {
            return false;
        }

        return personaEncontrada.tienePrestamosActivos();
    }
    
    private boolean existeTipo(String nombre) {
        if (nombre == null) {
            return false;
        }

        return tiposRegistrados.containsKey(nombre);
    }

    private void reasignarItemsATipoGenerico(String nombreTipo) {
        for (Item itemActual : itemsRegistrados.values()) {
            if (itemActual.getTipo().getNombre().equals(nombreTipo)) {
                itemActual.setTipo(tipoGenerico);
            }
        }
    }
    
    
}