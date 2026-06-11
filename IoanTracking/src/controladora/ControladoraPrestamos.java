package controladora;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import logica.Alerta;
import logica.Categoria;
import logica.Item;
import logica.Persona;
import logica.Prestamo;
import logica.Tipo;


public class ControladoraPrestamos implements Serializable {

	private static ControladoraPrestamos instance = new ControladoraPrestamos();
	
    private Map<String, Persona> personasRegistradas;
    private Map<String, Categoria> categoriasRegistradas;
    private Map<String, Tipo> tiposRegistrados;
    private Map<String, Item> itemsRegistrados;
    private Map<Integer, Prestamo> prestamosRegistrados;

    private int consecutivoPrestamo;
    private Tipo tipoGenerico;
    
    public static ControladoraPrestamos getInstance() {
	    return instance;
	}

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
    
    public boolean crearItem(String nombre, String codigo, String descripcion, String nombreTipo) {
        if (nombre == null || codigo == null || descripcion == null || nombreTipo == null) {
            return false;
        }

        if (nombre.equals("") || codigo.equals("") || descripcion.equals("") || nombreTipo.equals("")) {
            return false;
        }

        if (existeItem(codigo)) {
            return false;
        }

        Tipo tipoDelItem = buscarTipo(nombreTipo);

        if (tipoDelItem == null) {
            return false;
        }

        Item itemNuevo = new Item(nombre, codigo, descripcion, tipoDelItem);
        itemsRegistrados.put(codigo, itemNuevo);

        return true;
    }

    public Item buscarItem(String codigo) {
        if (codigo == null) {
            return null;
        }

        if (itemsRegistrados.containsKey(codigo)) {
            return itemsRegistrados.get(codigo);
        }

        return null;
    }

    public boolean modificarItem(String codigo, String nombre, String descripcion, String nombreTipo) {
        if (codigo == null || nombre == null || descripcion == null || nombreTipo == null) {
            return false;
        }

        if (codigo.equals("") || nombre.equals("") || descripcion.equals("") || nombreTipo.equals("")) {
            return false;
        }

        Item itemEncontrado = buscarItem(codigo);

        if (itemEncontrado == null) {
            return false;
        }

        Tipo tipoEncontrado = buscarTipo(nombreTipo);

        if (tipoEncontrado == null) {
            return false;
        }

        itemEncontrado.setNombre(nombre);
        itemEncontrado.setDescripcion(descripcion);
        itemEncontrado.setTipo(tipoEncontrado);

        return true;
    }

    public boolean borrarItem(String codigo) {
        Item itemEncontrado = buscarItem(codigo);

        if (itemEncontrado == null) {
            return false;
        }

        if (itemEncontrado.isPrestado() == true) {
            return false;
        }

        itemsRegistrados.remove(codigo);

        return true;
    }

    public boolean agregarCategoriaAItem(String codigoItem, String nombreCategoria) {
        Item itemEncontrado = buscarItem(codigoItem);
        Categoria categoriaEncontrada = buscarCategoria(nombreCategoria);

        if (itemEncontrado == null || categoriaEncontrada == null) {
            return false;
        }

        return itemEncontrado.agregarCategoria(categoriaEncontrada);
    }

    public boolean eliminarCategoriaDeItem(String codigoItem, String nombreCategoria) {
        Item itemEncontrado = buscarItem(codigoItem);

        if (itemEncontrado == null) {
            return false;
        }

        return itemEncontrado.eliminarCategoria(nombreCategoria);
    }

    public ArrayList<Item> listarItems() {
        ArrayList<Item> listaItems = new ArrayList<Item>();

        for (Item itemActual : itemsRegistrados.values()) {
            listaItems.add(itemActual);
        }

        return listaItems;
    }
    
    public Prestamo hacerPrestamo(String correoPersona) {
        if (correoPersona == null) {
            return null;
        }

        Persona personaEncontrada = buscarPersona(correoPersona);

        if (personaEncontrada == null) {
            return null;
        }

        if (personaTienePrestamoActivo(correoPersona)) {
            return null;
        }

        Prestamo prestamoNuevo = new Prestamo(consecutivoPrestamo, personaEncontrada);

        prestamosRegistrados.put(consecutivoPrestamo, prestamoNuevo);
        personaEncontrada.agregarPrestamo(prestamoNuevo);

        consecutivoPrestamo++;

        return prestamoNuevo;
    }

    public boolean agregarItemAPrestamo(int numeroPrestamo, String codigoItem) {
        Prestamo prestamoEncontrado = buscarPrestamo(numeroPrestamo);
        Item itemEncontrado = buscarItem(codigoItem);

        if (prestamoEncontrado == null || itemEncontrado == null) {
            return false;
        }

        if (prestamoEncontrado.isFinalizado() == true) {
            return false;
        }

        return prestamoEncontrado.agregarItem(itemEncontrado);
    }

    public boolean eliminarItemDePrestamo(int numeroPrestamo, String codigoItem) {
        Prestamo prestamoEncontrado = buscarPrestamo(numeroPrestamo);

        if (prestamoEncontrado == null) {
            return false;
        }

        if (prestamoEncontrado.isFinalizado() == true) {
            return false;
        }

        return prestamoEncontrado.eliminarItem(codigoItem);
    }

    public Prestamo buscarPrestamo(int numeroPrestamo) {
        if (prestamosRegistrados.containsKey(numeroPrestamo)) {
            return prestamosRegistrados.get(numeroPrestamo);
        }

        return null;
    }

    public boolean retornarItem(int numeroPrestamo, String codigoItem) {
        Prestamo prestamoEncontrado = buscarPrestamo(numeroPrestamo);

        if (prestamoEncontrado == null) {
            return false;
        }

        if (prestamoEncontrado.isFinalizado() == true) {
            return false;
        }

        return prestamoEncontrado.retornarItem(codigoItem);
    }

    public boolean finalizarPrestamo(int numeroPrestamo) {
        Prestamo prestamoEncontrado = buscarPrestamo(numeroPrestamo);

        if (prestamoEncontrado == null) {
            return false;
        }

        if (prestamoEncontrado.isFinalizado() == true) {
            return false;
        }

        if (prestamoEncontrado.getAlerta() != null) {
            prestamoEncontrado.getAlerta().desactivar();
        }

        prestamoEncontrado.finalizarPrestamo();

        return true;
    }

    public ArrayList<Prestamo> listarPrestamos() {
        ArrayList<Prestamo> listaPrestamos = new ArrayList<Prestamo>();

        for (Prestamo prestamoActual : prestamosRegistrados.values()) {
            listaPrestamos.add(prestamoActual);
        }

        return listaPrestamos;
    }
    
    public boolean agregarAlertaAPrestamo(int numeroPrestamo, LocalDateTime hora, String tipoAlerta, String mensaje, boolean recurrente) {
        if (hora == null || tipoAlerta == null || mensaje == null) {
            return false;
        }

        if (tipoAlerta.equals("") || mensaje.equals("")) {
            return false;
        }

        Prestamo prestamoEncontrado = buscarPrestamo(numeroPrestamo);

        if (prestamoEncontrado == null) {
            return false;
        }

        if (prestamoEncontrado.isFinalizado() == true) {
            return false;
        }

        Alerta alertaNueva = new Alerta(hora, tipoAlerta, mensaje, recurrente);
        prestamoEncontrado.setAlerta(alertaNueva);

        return true;
    }

    public boolean eliminarAlertaDePrestamo(int numeroPrestamo) {
        Prestamo prestamoEncontrado = buscarPrestamo(numeroPrestamo);

        if (prestamoEncontrado == null) {
            return false;
        }

        if (prestamoEncontrado.getAlerta() == null) {
            return false;
        }

        prestamoEncontrado.setAlerta(null);

        return true;
    }

    public ArrayList<Alerta> consultarAlertasActivas() {
        ArrayList<Alerta> listaAlertas = new ArrayList<Alerta>();
        LocalDateTime fechaActual = LocalDateTime.now();

        for (Prestamo prestamoActual : prestamosRegistrados.values()) {
            if (prestamoActual.isFinalizado() == false) {
                Alerta alertaActual = prestamoActual.getAlerta();

                if (alertaActual != null) {
                    if (alertaActual.debeMostrarse(fechaActual)) {
                        listaAlertas.add(alertaActual);
                        alertaActual.marcarComoMostrada();
                    }
                }
            }
        }

        return listaAlertas;
    }
    
    public String reportePorUsuario(String correo) {
        Persona personaEncontrada = buscarPersona(correo);

        if (personaEncontrada == null) {
            return "No se encontro una persona registrada con ese correo.";
        }

        String reporte = "";

        reporte += "REPORTE POR USUARIO\n";
        reporte += "Nombre: " + personaEncontrada.getNombre() + "\n";
        reporte += "Telefono: " + personaEncontrada.getTelefono() + "\n";
        reporte += "Correo: " + personaEncontrada.getCorreo() + "\n\n";

        if (personaEncontrada.getPrestamos().isEmpty()) {
            reporte += "La persona no tiene prestamos registrados.\n";
            return reporte;
        }

        reporte += "Prestamos registrados:\n";

        for (Prestamo prestamoActual : personaEncontrada.getPrestamos().values()) {
            reporte += "\nPrestamo numero: " + prestamoActual.getNumero() + "\n";
            reporte += "Fecha: " + prestamoActual.getFecha() + "\n";

            if (prestamoActual.isFinalizado() == true) {
                reporte += "Estado: Finalizado\n";
                reporte += "Fecha de finalizacion: " + prestamoActual.getFechaFinalizacion() + "\n";
            } else {
                reporte += "Estado: Activo\n";
            }

            reporte += "Cantidad de items: " + prestamoActual.cantidadItems() + "\n";

            if (prestamoActual.getItems().isEmpty()) {
                reporte += "No tiene items actualmente.\n";
            } else {
                reporte += "Items:\n";

                for (Item itemActual : prestamoActual.getItems().values()) {
                    reporte += "- " + itemActual.getCodigo() + " | " + itemActual.getNombre() + "\n";
                }
            }
        }

        return reporte;
    }

    public String reportePorItem(String codigoItem) {
        Item itemEncontrado = buscarItem(codigoItem);

        if (itemEncontrado == null) {
            return "No se encontro un item registrado con ese codigo.";
        }

        String reporte = "";

        reporte += "REPORTE POR ITEM\n";
        reporte += "Codigo: " + itemEncontrado.getCodigo() + "\n";
        reporte += "Nombre: " + itemEncontrado.getNombre() + "\n";
        reporte += "Descripcion: " + itemEncontrado.getDescripcion() + "\n";
        reporte += "Tipo: " + itemEncontrado.getTipo().getNombre() + "\n";

        if (itemEncontrado.isPrestado() == true) {
            reporte += "Estado: Prestado\n";
        } else {
            reporte += "Estado: Disponible\n";
        }

        if (itemEncontrado.getCategorias().isEmpty()) {
            reporte += "Categorias: Sin categorias asignadas\n";
        } else {
            reporte += "Categorias:\n";

            for (Categoria categoriaActual : itemEncontrado.getCategorias().values()) {
                reporte += "- " + categoriaActual.getNombre() + "\n";
            }
        }

        reporte += "\nPrestamos donde aparece actualmente:\n";

        boolean itemApareceEnPrestamo = false;

        for (Prestamo prestamoActual : prestamosRegistrados.values()) {
            if (prestamoActual.contieneItem(codigoItem)) {
                reporte += "- Prestamo numero " + prestamoActual.getNumero();
                reporte += " | Persona: " + prestamoActual.getPersona().getNombre() + "\n";
                itemApareceEnPrestamo = true;
            }
        }

        if (itemApareceEnPrestamo == false) {
            reporte += "El item no aparece en prestamos activos actualmente.\n";
        }

        return reporte;
    }

    public String reportePorCategoria(String nombreCategoria) {
        Categoria categoriaEncontrada = buscarCategoria(nombreCategoria);

        if (categoriaEncontrada == null) {
            return "No se encontro una categoria registrada con ese nombre.";
        }

        String reporte = "";

        reporte += "REPORTE POR CATEGORIA\n";
        reporte += "Categoria: " + categoriaEncontrada.getNombre() + "\n\n";
        reporte += "Items relacionados:\n";

        boolean encontroItems = false;

        for (Item itemActual : itemsRegistrados.values()) {
            if (itemActual.perteneceACategoria(nombreCategoria)) {
                reporte += "- " + itemActual.getCodigo() + " | " + itemActual.getNombre();

                if (itemActual.isPrestado() == true) {
                    reporte += " | Prestado\n";
                } else {
                    reporte += " | Disponible\n";
                }

                encontroItems = true;
            }
        }

        if (encontroItems == false) {
            reporte += "No hay items registrados en esta categoria.\n";
        }

        return reporte;
    }

    public String reportePorTipo(String nombreTipo) {
        Tipo tipoEncontrado = buscarTipo(nombreTipo);

        if (tipoEncontrado == null) {
            return "No se encontro un tipo registrado con ese nombre.";
        }

        String reporte = "";

        reporte += "REPORTE POR TIPO\n";
        reporte += "Tipo: " + tipoEncontrado.getNombre() + "\n";

        if (tipoEncontrado.isEsGenerico() == true) {
            reporte += "Este tipo esta marcado como tipo generico.\n";
        }

        reporte += "\nItems relacionados:\n";

        boolean encontroItems = false;

        for (Item itemActual : itemsRegistrados.values()) {
            if (itemActual.getTipo().getNombre().equals(nombreTipo)) {
                reporte += "- " + itemActual.getCodigo() + " | " + itemActual.getNombre();

                if (itemActual.isPrestado() == true) {
                    reporte += " | Prestado\n";
                } else {
                    reporte += " | Disponible\n";
                }

                encontroItems = true;
            }
        }

        if (encontroItems == false) {
            reporte += "No hay items registrados con este tipo.\n";
        }

        return reporte;
    }   
    
    public static void guardarDatos() throws IOException {
        FileOutputStream file = new FileOutputStream("DatosPrestamos.dat");
        ObjectOutputStream stream = new ObjectOutputStream(file);

        stream.writeObject(instance);

        stream.close();
        file.close();
    }

    public static void cargarDatos() throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream("DatosPrestamos.dat");
        ObjectInputStream stream = new ObjectInputStream(file);

        instance = (ControladoraPrestamos) stream.readObject();

        stream.close();
        file.close();
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
    
    private boolean existeItem(String codigo) {
        if (codigo == null) {
            return false;
        }

        return itemsRegistrados.containsKey(codigo);
    }
    
}