package presentacion.control;

import control.ControlPrincipal;
import dtos.*;
import presentacion.FrmPrincipal;
import presentacion.dialogos.LoginDialog;
import presentacion.dialogos.RegistroDialog;
import javax.swing.*;
import java.util.List;
import presentacion.paneles.*;
import presentacion.dialogos.*;

// Controlador de la capa de presentacion
// Conecta la UI con el ControlPrincipal (Mediator)
public class ControlPresentacion {
    private final ControlPrincipal controlPrincipal;
    private FrmPrincipal framePrincipal;

    public ControlPresentacion() {
        this.controlPrincipal = new ControlPrincipal();
    }

    public void setFramePrincipal(FrmPrincipal frame) {
        this.framePrincipal = frame;
    }

    // =============== AUTENTICACION REAL ===============
    public boolean haySesionActiva() {
        return controlPrincipal.haySesionActiva();
    }

    public UsuarioDTO getUsuarioEnSesion() {
        return controlPrincipal.getUsuarioEnSesion();
    }

    public boolean esAdministrador() {
        return controlPrincipal.esAdministrador();
    }

    public UsuarioDTO iniciarSesion(String correo, String password) {
        UsuarioDTO usuario = controlPrincipal.iniciarSesion(correo, password);
        if (usuario != null && framePrincipal != null) {
            framePrincipal.actualizarBarraSesion();
        }
        return usuario;
    }

    public UsuarioDTO registrarUsuario(String nombre, String correo, String password) {
        return controlPrincipal.registrarUsuario(nombre, correo, password);
    }

    public void cerrarSesion() {
        controlPrincipal.cerrarSesion();
        if (framePrincipal != null) {
            framePrincipal.actualizarBarraSesion();
        }
    }

    public void mostrarLogin() {
        if (framePrincipal != null) {
            LoginDialog dialog = new LoginDialog(framePrincipal, this);
            dialog.setVisible(true);
        }
    }

    public void mostrarRegistro() {
        if (framePrincipal != null) {
            RegistroDialog dialog = new RegistroDialog(framePrincipal, this);
            dialog.setVisible(true);
        }
    }

    public List<LibroDTO> obtenerCatalogo() {
        return controlPrincipal.obtenerCatalogo();
    }

    public LibroDTO buscarLibro(int idLibro) {
        return controlPrincipal.buscarLibro(idLibro);
    }

    public LibroDTO agregarLibro(String titulo, String autor, double precio, int stock, String imagen) {
        return controlPrincipal.agregarLibro(titulo, autor, precio, stock, imagen);
    }

    // =============== ORDENES ===============
    public OrdenDTO crearOrden(List<DetalleOrdenDTO> detalles) {
        return controlPrincipal.crearOrden(detalles);
    }

    public List<OrdenDTO> obtenerOrdenesPendientes() {
        return controlPrincipal.obtenerOrdenesPendientes();
    }

    public boolean aceptarOrden(int idOrden) {
        return controlPrincipal.aceptarOrden(idOrden);
    }

    public boolean rechazarOrden(int idOrden) {
        return controlPrincipal.rechazarOrden(idOrden);
    }

    public boolean tienePendiente() {
        return controlPrincipal.tieneOrdenPendiente();
    }

    // =============== UI HELPERS ===============
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(framePrincipal, mensaje);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(framePrincipal, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(framePrincipal, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    // =============== NAVEGACION ===============
    public void mostrarDetalleLibro(LibroDTO libro) {
        if (framePrincipal != null) {
            presentacion.paneles.DetalleLibroPanel panel = new presentacion.paneles.DetalleLibroPanel(this);
            panel.mostrarDetalles(libro);
            framePrincipal.cambiarPanel(panel);
        }
    }

    public void mostrarCatalogo() {
        if (framePrincipal != null) {
            framePrincipal.mostrarCatalogo();
        }
    }

    public void mostrarConfirmacionOrden(List<DetalleOrdenDTO> detalles) {
        if (framePrincipal != null) {
            presentacion.dialogos.ConfirmacionOrdenDialog dialog = new presentacion.dialogos.ConfirmacionOrdenDialog(
                    framePrincipal, this);
            dialog.mostrarResumen(detalles);
            dialog.setVisible(true);
            if (dialog.isConfirmado()) {
                crearOrden(detalles);
            }
        }
    }

    public void mostrarCarrito(java.util.Map<Integer, Integer> carrito, presentacion.paneles.CatalogoPanel catalogo) {
        if (framePrincipal != null) {
            presentacion.paneles.CarritoPanel panel = new presentacion.paneles.CarritoPanel(this, carrito, catalogo);
            framePrincipal.cambiarPanel(panel);
        }
    }

    public void mostrarDetalleOrden(OrdenDTO orden) {
        if (framePrincipal != null) {
            presentacion.dialogos.DetalleOrdenDialog dialog = new presentacion.dialogos.DetalleOrdenDialog(
                    framePrincipal, this);
            dialog.mostrarDetalles(orden);
            dialog.setVisible(true);
        }
    }
}