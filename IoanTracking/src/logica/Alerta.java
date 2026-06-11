package logica;

import java.time.LocalDateTime;
import java.io.Serializable;

public class Alerta implements Serializable {

    private LocalDateTime horaActivacion;
    private String tipoAlerta;
    private String mensaje;
    private boolean activa;
    private boolean recurrente;

    public Alerta(LocalDateTime horaActivacion, String tipoAlerta, String mensaje, boolean recurrente) {
        this.horaActivacion = horaActivacion;
        this.tipoAlerta = tipoAlerta;
        this.mensaje = mensaje;
        this.recurrente = recurrente;
        this.activa = true;
    }

    public LocalDateTime getHoraActivacion() {
        return horaActivacion;
    }

    public void setHoraActivacion(LocalDateTime horaActivacion) {
        this.horaActivacion = horaActivacion;
    }

    public String getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(String tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isRecurrente() {
        return recurrente;
    }

    public void setRecurrente(boolean recurrente) {
        this.recurrente = recurrente;
    }

    public void activar() {
        activa = true;
    }

    public void desactivar() {
        activa = false;
    }

    public boolean debeMostrarse(LocalDateTime fechaActual) {
        if (activa == false) {
            return false;
        }

        if (fechaActual == null || horaActivacion == null) {
            return false;
        }

        if (fechaActual.isAfter(horaActivacion) || fechaActual.isEqual(horaActivacion)) {
            return true;
        }

        return false;
    }

    public void marcarComoMostrada() {
        if (recurrente == false) {
            activa = false;
        }
    }

    @Override
    public String toString() {
        return tipoAlerta + ": " + mensaje;
    }
}