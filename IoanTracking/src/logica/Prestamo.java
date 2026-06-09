package logica;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Prestamo {

    private int numero;
    private LocalDateTime fecha;
    private LocalDateTime fechaFinalizacion;
    private boolean finalizado;
    private Persona persona;
    private Alerta alerta;
    private Map<String, Item> itemsDelPrestamo;

    public Prestamo(int numero, Persona persona) {
        this.numero = numero;
        this.persona = persona;
        this.fecha = LocalDateTime.now();
        this.fechaFinalizacion = null;
        this.finalizado = false;
        this.alerta = null;
        this.itemsDelPrestamo = new HashMap<String, Item>();
    }

    public int getNumero() {
        return numero;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Map<String, Item> getItems() {
        return itemsDelPrestamo;
    }

    public Alerta getAlerta() {
        return alerta;
    }

    public void setAlerta(Alerta alerta) {
        this.alerta = alerta;
    }

    public boolean agregarItem(Item itemNuevo) {
        if (itemNuevo == null) {
            return false;
        }

        if (finalizado == true) {
            return false;
        }

        if (itemNuevo.isPrestado() == true) {
            return false;
        }

        if (itemsDelPrestamo.containsKey(itemNuevo.getCodigo())) {
            return false;
        }

        itemsDelPrestamo.put(itemNuevo.getCodigo(), itemNuevo);
        itemNuevo.marcarComoPrestado();

        return true;
    }

    public boolean eliminarItem(String codigoItem) {
        if (itemsDelPrestamo.containsKey(codigoItem)) {
            Item itemEncontrado = itemsDelPrestamo.get(codigoItem);
            itemEncontrado.marcarComoDisponible();

            itemsDelPrestamo.remove(codigoItem);
            return true;
        }

        return false;
    }

    public boolean contieneItem(String codigoItem) {
        return itemsDelPrestamo.containsKey(codigoItem);
    }

    public boolean retornarItem(String codigoItem) {
        if (itemsDelPrestamo.containsKey(codigoItem)) {
            Item itemDevuelto = itemsDelPrestamo.get(codigoItem);
            itemDevuelto.marcarComoDisponible();

            itemsDelPrestamo.remove(codigoItem);
            return true;
        }

        return false;
    }

    public void finalizarPrestamo() {
        for (Item itemActual : itemsDelPrestamo.values()) {
            itemActual.marcarComoDisponible();
        }

        itemsDelPrestamo.clear();
        finalizado = true;
        fechaFinalizacion = LocalDateTime.now();
    }

    public int cantidadItems() {
        return itemsDelPrestamo.size();
    }

    public boolean estaVacio() {
        return itemsDelPrestamo.isEmpty();
    }

    @Override
    public String toString() {
        return "Prestamo numero " + numero + " - Persona: " + persona.getNombre();
    }
}