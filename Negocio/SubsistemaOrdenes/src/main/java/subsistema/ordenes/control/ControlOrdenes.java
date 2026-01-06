package subsistema.ordenes.control;

import dtos.*;
import persistencia.factory.DAOFactory;
import persistencia.interfaces.IOrdenDAO;
import persistencia.interfaces.ILibroDAO;
import subsistema.ordenes.interfaz.IOrdenes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Control principal del modulo de ordenes
public class ControlOrdenes implements IOrdenes {
    private final IOrdenDAO ordenDAO;
    private final ILibroDAO libroDAO;

    public ControlOrdenes() {
        this.ordenDAO = DAOFactory.crearOrdenDAO();
        this.libroDAO = DAOFactory.crearLibroDAO();
    }

    @Override
    public OrdenDTO crearOrden(int idUsuario, List<DetalleOrdenDTO> detalles) {
        try {
            // Validar que no tenga orden pendiente
            if (ordenDAO.tienePendiente(idUsuario)) {
                System.err.println("Usuario ya tiene orden pendiente");
                return null;
            }
            // Validar que tenga productos
            if (detalles == null || detalles.isEmpty()) {
                System.err.println("No se puede crear orden vacia");
                return null;
            }
            // Crear la orden
            persistencia.entidades.Orden orden = new persistencia.entidades.Orden();
            orden.setIdUsuario(idUsuario);
            orden.setFecha(LocalDateTime.now());
            orden.setEstado("PENDIENTE");
            double total = 0.0;
            List<persistencia.entidades.DetalleOrden> detallesOrden = new ArrayList<>();
            // Procesar cada libro del carrito
            for (DetalleOrdenDTO detalleDTO : detalles) {
                persistencia.entidades.Libro libro = libroDAO.buscarPorId(detalleDTO.getIdDetalle());
                if (libro != null) {
                    // Verificar stock disponible
                    if (libro.getStock() < detalleDTO.getCantidad()) {
                        System.err.println("Stock insuficiente para: " + libro.getTitulo());
                        return null;
                    }
                    // Calcular subtotal
                    double subtotal = libro.getPrecio() * detalleDTO.getCantidad();
                    total += subtotal;
                    // Agregar detalle
                    persistencia.entidades.DetalleOrden detalle = new persistencia.entidades.DetalleOrden();
                    detalle.setIdLibro(libro.getIdLibro());
                    detalle.setTituloLibro(libro.getTitulo());
                    detalle.setAutorLibro(libro.getAutor());
                    detalle.setCantidad(detalleDTO.getCantidad());
                    detalle.setSubtotal(subtotal);
                    detallesOrden.add(detalle);
                    // Actualizar stock
                    int nuevoStock = libro.getStock() - detalleDTO.getCantidad();
                    libroDAO.actualizarStock(libro.getIdLibro(), nuevoStock);
                }
            }
            orden.setTotal(total);
            orden.setDetalles(detallesOrden);
            // Guardar orden en BD
            int idOrden = ordenDAO.guardar(orden);
            if (idOrden > 0) {
                // Crear DTO para retornar
                OrdenDTO ordenDTO = new OrdenDTO();
                ordenDTO.setIdOrden(idOrden);
                ordenDTO.setFecha(orden.getFecha());
                ordenDTO.setTotal(total);
                ordenDTO.setEstado("PENDIENTE");
                // Convertir detalles a DTO
                List<DetalleOrdenDTO> detallesDTOs = new ArrayList<>();
                for (persistencia.entidades.DetalleOrden detalle : detallesOrden) {
                    DetalleOrdenDTO dto = new DetalleOrdenDTO();
                    dto.setIdDetalle(detalle.getIdLibro());
                    dto.setTituloLibro(detalle.getTituloLibro());
                    dto.setAutorLibro(detalle.getAutorLibro());
                    dto.setCantidad(detalle.getCantidad());
                    dto.setSubtotal(detalle.getSubtotal());
                    detallesDTOs.add(dto);
                }
                ordenDTO.setDetalles(detallesDTOs);
                return ordenDTO;
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error al crear orden: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<OrdenDTO> obtenerOrdenesPendientes() {
        List<OrdenDTO> ordenes = new ArrayList<>();
        try {
            List<persistencia.entidades.Orden> ordenesPendientes = ordenDAO.obtenerPendientes();
            for (persistencia.entidades.Orden orden : ordenesPendientes) {
                OrdenDTO dto = convertirADTO(orden);
                ordenes.add(dto);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener ordenes: " + e.getMessage());
        }
        return ordenes;
    }

    @Override
    public boolean aceptarOrden(int idOrden) {
        try {
            return ordenDAO.actualizarEstado(idOrden, "ACEPTADA");
        } catch (Exception e) {
            System.err.println("Error al aceptar orden: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean rechazarOrden(int idOrden) {
        try {
            return ordenDAO.actualizarEstado(idOrden, "RECHAZADA");
        } catch (Exception e) {
            System.err.println("Error al rechazar orden: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean tienePendiente(int idUsuario) {
        try {
            return ordenDAO.tienePendiente(idUsuario);
        } catch (Exception e) {
            return false;
        }
    }

    // Metodo helper para convertir entidad a DTO
    private OrdenDTO convertirADTO(persistencia.entidades.Orden orden) {
        OrdenDTO dto = new OrdenDTO();
        dto.setIdOrden(orden.getIdOrden());
        dto.setFecha(orden.getFecha());
        dto.setTotal(orden.getTotal());
        dto.setEstado(orden.getEstado());
        List<DetalleOrdenDTO> detallesDTOs = new ArrayList<>();
        for (persistencia.entidades.DetalleOrden detalle : orden.getDetalles()) {
            DetalleOrdenDTO detalleDTO = new DetalleOrdenDTO();
            detalleDTO.setIdDetalle(detalle.getIdLibro());
            detalleDTO.setTituloLibro(detalle.getTituloLibro());
            detalleDTO.setAutorLibro(detalle.getAutorLibro());
            detalleDTO.setCantidad(detalle.getCantidad());
            detalleDTO.setSubtotal(detalle.getSubtotal());
            detallesDTOs.add(detalleDTO);
        }
        dto.setDetalles(detallesDTOs);
        return dto;
    }
}