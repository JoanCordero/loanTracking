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

    private boolean existePersona(String correo) {
        if (correo == null) {
            return false;
        }

        return personasRegistradas.containsKey(correo);
    }

    private boolean personaTienePrestamoActivo(String correo) {
        Persona personaEncontrada = buscarPersona(correo);

        if (personaEncontrada == null) {
            return false;
        }

        return personaEncontrada.tienePrestamosActivos();
    }
}