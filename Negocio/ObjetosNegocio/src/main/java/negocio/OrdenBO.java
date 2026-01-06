package negocio;
import dtos.*;
import persistencia.entidades.Orden;
import persistencia.entidades.DetalleOrden;
import persistencia.entidades.Libro;
import negocio.adaptadores.OrdenAdapter;
import java.util.List;

public class OrdenBO {
    

    public static OrdenDTO toDTO(Orden orden) {
        return OrdenAdapter.toDTO(orden);
    }
    

    public static double calcularSubtotal(Libro libro, int cantidad) {
        if (libro == null || cantidad <= 0) {
            return 0.0;
        }
        return libro.getPrecio() * cantidad;
    }
    

    public static double calcularTotal(List<DetalleOrdenDTO> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            return 0.0;
        }
        return detalles.stream()
                .mapToDouble(DetalleOrdenDTO::getSubtotal)
                .sum();
    }
    

    public static boolean validarOrden(Orden orden) {
        return orden != null
                && orden.getIdUsuario() > 0
                && orden.getTotal() > 0
                && orden.getDetalles() != null
                && !orden.getDetalles().isEmpty();
    }
    

    public static DetalleOrden crearDetalle(Libro libro, int cantidad) {
        return OrdenAdapter.crearDetalle(libro, cantidad);
    }
}