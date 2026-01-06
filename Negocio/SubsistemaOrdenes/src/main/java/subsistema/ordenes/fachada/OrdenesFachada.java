package subsistema.ordenes.fachada;

import dtos.DetalleOrdenDTO;
import dtos.OrdenDTO;
import subsistema.ordenes.control.ControlOrdenes;
import subsistema.ordenes.interfaz.IOrdenes;
import java.util.List;

// Fachada: Punto de entrada al subsistema de ordenes
public class OrdenesFachada implements IOrdenes {
    private final ControlOrdenes control;

    public OrdenesFachada() {
        this.control = new ControlOrdenes();
    }

    @Override
    public OrdenDTO crearOrden(int idUsuario, List<DetalleOrdenDTO> detalles) {
        return control.crearOrden(idUsuario, detalles);
    }

    @Override
    public List<OrdenDTO> obtenerOrdenesPendientes() {
        return control.obtenerOrdenesPendientes();
    }

    @Override
    public boolean aceptarOrden(int idOrden) {
        return control.aceptarOrden(idOrden);
    }

    @Override
    public boolean rechazarOrden(int idOrden) {
        return control.rechazarOrden(idOrden);
    }

    @Override
    public boolean tienePendiente(int idUsuario) {
        return control.tienePendiente(idUsuario);
    }
}