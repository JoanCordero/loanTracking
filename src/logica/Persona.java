package logica;

import java.util.HashMap;
import java.util.Map;

public class Persona {

    private String nombre;
    private String telefono;
    private String correo;
    private Map<Integer, Prestamo> prestamosDeLaPersona;

    public Persona(String nombre, String telefono, String correo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.prestamosDeLaPersona = new HashMap<Integer, Prestamo>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Map<Integer, Prestamo> getPrestamos() {
        return prestamosDeLaPersona;
    }

    public boolean agregarPrestamo(Prestamo prestamoNuevo) {
        if (prestamoNuevo == null) {
            return false;
        }

        if (prestamosDeLaPersona.containsKey(prestamoNuevo.getNumero())) {
            return false;
        }

        prestamosDeLaPersona.put(prestamoNuevo.getNumero(), prestamoNuevo);
        return true;
    }

    public boolean eliminarPrestamo(int numeroPrestamo) {
        if (prestamosDeLaPersona.containsKey(numeroPrestamo)) {
            prestamosDeLaPersona.remove(numeroPrestamo);
            return true;
        }

        return false;
    }

    public boolean tienePrestamosActivos() {
        for (Prestamo prestamoActual : prestamosDeLaPersona.values()) {
            if (prestamoActual.isFinalizado() == false) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return nombre + " | " + telefono + " | " + correo;
    }
}