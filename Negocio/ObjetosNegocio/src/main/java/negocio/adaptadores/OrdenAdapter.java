package negocio.adaptadores;
import dtos.*;
import persistencia.entidades.Orden;
import persistencia.entidades.DetalleOrden;
import persistencia.entidades.Libro;
import java.util.ArrayList;
import java.util.List;

public class OrdenAdapter {
    

    public static OrdenDTO toDTO(Orden entidad) {
        if (entidad == null) {
            return null;
        }
        OrdenDTO dto = new OrdenDTO();
        dto.setIdOrden(entidad.getIdOrden());
        dto.setFecha(entidad.getFecha());
        dto.setTotal(entidad.getTotal());
        dto.setEstado(entidad.getEstado());
        // Convertir detalles
        List<DetalleOrdenDTO> detallesDTOs = new ArrayList<>();
        if (entidad.getDetalles() != null) {
            for (DetalleOrden detalle : entidad.getDetalles()) {
                DetalleOrdenDTO detalleDTO = new DetalleOrdenDTO();
                detalleDTO.setIdDetalle(detalle.getIdLibro());
                detalleDTO.setTituloLibro(detalle.getTituloLibro());
                detalleDTO.setAutorLibro(detalle.getAutorLibro());
                detalleDTO.setCantidad(detalle.getCantidad());
                detalleDTO.setSubtotal(detalle.getSubtotal());
                detallesDTOs.add(detalleDTO);
            }
        }
        dto.setDetalles(detallesDTOs);
        return dto;
    }
    

    public static Orden toEntity(OrdenDTO dto, int idUsuario) {
        if (dto == null) {
            return null;
        }
        Orden entidad = new Orden();
        entidad.setIdOrden(dto.getIdOrden());
        entidad.setIdUsuario(idUsuario);
        entidad.setFecha(dto.getFecha());
        entidad.setTotal(dto.getTotal());
        entidad.setEstado(dto.getEstado());
        // Convertir detalles
        List<DetalleOrden> detallesEntidad = new ArrayList<>();
        if (dto.getDetalles() != null) {
            for (DetalleOrdenDTO detalleDTO : dto.getDetalles()) {
                DetalleOrden detalle = new DetalleOrden();
                detalle.setIdLibro(detalleDTO.getIdDetalle());
                detalle.setTituloLibro(detalleDTO.getTituloLibro());
                detalle.setAutorLibro(detalleDTO.getAutorLibro());
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setSubtotal(detalleDTO.getSubtotal());
                detallesEntidad.add(detalle);
            }
        }
        entidad.setDetalles(detallesEntidad);
        return entidad;
    }
    

    public static DetalleOrden crearDetalle(Libro libro, int cantidad) {
        if (libro == null || cantidad <= 0) {
            return null;
        }
        DetalleOrden detalle = new DetalleOrden();
        detalle.setIdLibro(libro.getIdLibro());
        detalle.setTituloLibro(libro.getTitulo());
        detalle.setAutorLibro(libro.getAutor());
        detalle.setCantidad(cantidad);
        detalle.setSubtotal(libro.getPrecio() * cantidad);
        return detalle;
    }
}