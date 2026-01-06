package control;

import dtos.*;
import subsistema.catalogo.fachada.CatalogoFachada;
import subsistema.ordenes.fachada.OrdenesFachada;
import subsistema.iniciosesion.fachada.InicioSesionFachada;
import java.util.List;

// Mediator: Coordina todos los subsistemas
public class ControlPrincipal {
    private final CatalogoFachada catalogoFachada;
    private final OrdenesFachada ordenesFachada;
    private final InicioSesionFachada inicioSesionFachada;
    // Sesion actual
    private UsuarioDTO usuarioEnSesion;

    public ControlPrincipal() {
        this.catalogoFachada = new CatalogoFachada();
        this.ordenesFachada = new OrdenesFachada();
        this.inicioSesionFachada = new InicioSesionFachada();
        this.usuarioEnSesion = null;
    }

    // =============== AUTENTICACION ===============
    public UsuarioDTO iniciarSesion(String correo, String password) {
        UsuarioDTO usuario = inicioSesionFachada.iniciarSesion(correo, password);
        if (usuario != null) {
            this.usuarioEnSesion = usuario;
        }
        return usuario;
    }

    public UsuarioDTO registrarUsuario(String nombre, String correo, String password) {
        return inicioSesionFachada.registrarUsuario(nombre, correo, password);
    }

    public void cerrarSesion() {
        if (usuarioEnSesion != null) {
            inicioSesionFachada.cerrarSesion(usuarioEnSesion.getIdUsuario());
            usuarioEnSesion = null;
        }
    }

    public boolean haySesionActiva() {
        return usuarioEnSesion != null;
    }

    public UsuarioDTO getUsuarioEnSesion() {
        return usuarioEnSesion;
    }

    public boolean esAdministrador() {
        return usuarioEnSesion != null && "ADMIN".equalsIgnoreCase(usuarioEnSesion.getRol());
    }

    // =============== CATALOGO ===============
    public List<LibroDTO> obtenerCatalogo() {
        return catalogoFachada.obtenerCatalogo();
    }

    public LibroDTO buscarLibro(int idLibro) {
        return catalogoFachada.buscarLibroPorId(idLibro);
    }

    public LibroDTO agregarLibro(String titulo, String autor, double precio, int stock, String imagen) {
        if (!esAdministrador()) {
            System.err.println("Solo administradores pueden agregar libros");
            return null;
        }
        return catalogoFachada.agregarLibro(titulo, autor, precio, stock, imagen);
    }

    public boolean actualizarStock(int idLibro, int nuevoStock) {
        if (!esAdministrador()) {
            System.err.println("Solo administradores pueden actualizar stock");
            return false;
        }
        return catalogoFachada.actualizarStock(idLibro, nuevoStock);
    }

    // =============== ORDENES ===============
    public OrdenDTO crearOrden(List<DetalleOrdenDTO> detalles) {
        if (!haySesionActiva()) {
            System.err.println("Debes iniciar sesion para crear una orden");
            return null;
        }
        return ordenesFachada.crearOrden(usuarioEnSesion.getIdUsuario(), detalles);
    }

    public List<OrdenDTO> obtenerOrdenesPendientes() {
        if (!esAdministrador()) {
            System.err.println("Solo administradores pueden ver ordenes pendientes");
            return null;
        }
        return ordenesFachada.obtenerOrdenesPendientes();
    }

    public boolean aceptarOrden(int idOrden) {
        if (!esAdministrador()) {
            System.err.println("Solo administradores pueden aceptar ordenes");
            return false;
        }
        return ordenesFachada.aceptarOrden(idOrden);
    }

    public boolean rechazarOrden(int idOrden) {
        if (!esAdministrador()) {
            System.err.println("Solo administradores pueden rechazar ordenes");
            return false;
        }
        return ordenesFachada.rechazarOrden(idOrden);
    }

    public boolean tieneOrdenPendiente() {
        if (!haySesionActiva()) {
            return false;
        }
        return ordenesFachada.tienePendiente(usuarioEnSesion.getIdUsuario());
    }
}