package subsistema.ordenes.interfaz;

import dtos.DetalleOrdenDTO;
import dtos.OrdenDTO;
import java.util.List;

public interface IOrdenes {
    OrdenDTO crearOrden(int idUsuario, List<DetalleOrdenDTO> detalles);

    List<OrdenDTO> obtenerOrdenesPendientes();

    boolean aceptarOrden(int idOrden);

    boolean rechazarOrden(int idOrden);

    boolean tienePendiente(int idUsuario);
}